package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.api.Config;
import me.alexisevelyn.crewmate.api.Plugin;
import me.alexisevelyn.crewmate.api.PluginLoader;
import me.alexisevelyn.crewmate.enums.TerminalColors;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.bus.EventBus;
import me.alexisevelyn.crewmate.packethandler.HazelPacket;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.Arrays;

public class Server extends Thread {
	// Server Logger
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	// https://www.scadacore.com/tools/programming-calculators/online-hex-converter/

	private final DatagramSocket socket;
	private boolean running = false;
	private final byte[] buf = new byte[1024]; // Note: Maximum Tested Payload Size is 1024. Original Size is 256!

	private final InetAddress boundIP;
	private final int port;
	private final int maxPlayers;

	private final EventBus eventBus = new EventBus();

	private final Statistics statistics;

	public Server(Config config) throws SocketException, AccessDeniedException {
		this.statistics = new Statistics();
		this.socket = new DatagramSocket(config.getServerPort(), config.getServerAddress());

		this.port = this.socket.getLocalPort();
		this.boundIP = this.socket.getLocalAddress();
		this.maxPlayers = config.getMaxPlayers();

		// Root Directory For Server Files
		File root = config.getRootDir();

		// Create Root Folder If It Does Not Exist
		if (!root.exists() && !root.mkdirs()) {
			// https://docs.oracle.com/javase/7/docs/api/java/nio/file/AccessDeniedException.html
			throw new AccessDeniedException(String.format(config.getTranslation("root_directory_failed_creation"), root.getAbsolutePath()));
		}

		// Plugins Directory For Server Plugins
		File pluginsFolder = config.getPluginsDir();

		for (Plugin plugin : PluginLoader.loadPlugins(pluginsFolder, this)) {
			LogHelper.printLine(plugin.getID());
		}
	}

	@Override
	public void run() {
		// For Cleaning Up When Shutdown
		this.setupShutdownHook();

		running = true;
		boolean justStarted = false;

		while (this.isRunning()) {
			DatagramPacket packet = new DatagramPacket(this.buf, this.buf.length);

			if (!justStarted) {
				justStarted = true;

				LogHelper.printLine(Main.getTranslation("server_started"));

				// For Title
				LogHelper.print(
						TerminalColors.getTitle(
								String.format(Main.getTranslation("server_listening_title"), this.boundIP.getHostAddress(), this.port)
						)
				);
			}

			try {
				// This is useless as it doesn't stop the packet receiver
				if (this.isInterrupted())
					throw new InterruptedException();

				// Can't Receive Packets if Socket Is Closed
				if (this.socket.isClosed())
					this.exit();

				// Receive Packet From Client
				this.socket.receive(packet);

				// Parse Packet
				this.parsePacketAndReply(packet);

				// Clear Buffer
				this.clearBuffer();
			} catch (IOException e) {
				// This is the only way I know how to get rid of the exception output thrown when closing via normal means
				if (this.socket.isClosed())
					return;

				LogHelper.printLineErr("IOException: " + e.getMessage());
				e.printStackTrace();

				this.exit();
			} catch (InterruptedException e) {
				// This is due to recommendations to rethrow the interrupt after catching it
				// https://stackoverflow.com/a/1087504/6828099
				Thread.currentThread().interrupt();

				this.exit();
			}
		}
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public int getMaxPlayers() {
		return maxPlayers;
	}

	private void parsePacketAndReply(DatagramPacket packet) throws IOException {
		if (packet.getData().length < 1)
			return;

		// For Tracking Received Packets
		statistics.logAllReceived(packet.getData());

		byte[] replyBuffer;
		try {
			replyBuffer = HazelPacket.handlePacket(packet, this, statistics);
		} catch (Exception exception) {
			// Generic Catch All For Uncaught Exceptions
			replyBuffer = ClosePacket.closeWithMessage(Main.getTranslation("server_side_exception"));

			// Log Uncaught Exception
			LogHelper.printLineErr(String.format(Main.getTranslation("uncaught_server_side_exception"), exception.getMessage()));
			exception.printStackTrace();
		}

		// Don't Send Packet if No Data To Send
		if (replyBuffer.length == 0)
			return;

		// Received Packet Port and Address
		InetAddress address = packet.getAddress();
		int port = packet.getPort();

		// Packet to Send Back to Client
		packet = this.createSendPacket(address, port, replyBuffer.length, replyBuffer);

		// Send Reply Back
		this.sendPacket(packet);
	}

	public void sendPacket(DatagramPacket packet) {
		try {
			this.socket.send(packet);

			// For Tracking Sent Packets
			statistics.logAllSent(packet.getData());

			byte[] packetData = packet.getData();

			// Don't Send Packet if No Data To Send
			if (packetData.length == 0)
				return;

			// Check If Disconnect and Disconnect From Our End
			SendOption replyOption = SendOption.getByte(packetData[0]);

			// Sanitization
			if (replyOption == null)
				return;

			// Disconnect Client From Server End
			if (replyOption.equals(SendOption.DISCONNECT)) {
				// TODO: Figure out how to close one client's connection
				// LogHelper.printLine("Closing Connection!!!");
				// this.socket.disconnect();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DatagramPacket createSendPacket(InetAddress address, int port, int length, byte... buffer) {
		byte[] sendBuffer = new byte[length];

		if (length >= 0)
			System.arraycopy(buffer, 0, sendBuffer, 0, length);

		return new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
	}

	// To prevent leaking data
	private void clearBuffer() {
		Arrays.fill(this.buf, (byte) 0x0);
	}

	// Isn't this supposed to be overridable from Thread?
	public void exit() {
		if (this.running)
			this.shutdown();
	}

	private void shutdown() {
		this.running = false;

		LogHelper.printLine(Main.getTranslation("server_shutdown"));

		// This never runs.
		this.socket.close();
	}

	public boolean isRunning() {
		return this.running;
	}

	private void setupShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(this::exit));
	}

	public InetAddress getBoundIP() {
		return this.boundIP;
	}

	public int getPort() {
		return this.port;
	}

	// Deprecated To Remind Me To Fix
	@Deprecated
	@Nullable
	public Statistics getStatistics() {
		// TODO: Currently Clone is Broken

//		try {
//			return (Statistics) this.statistics.clone();
//		} catch (CloneNotSupportedException exception) {
//			LogHelper.printLine(exception.getMessage());
//		}

//		return null;
		return this.statistics;
	}
}
