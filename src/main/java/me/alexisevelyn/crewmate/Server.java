package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.api.Plugin;
import me.alexisevelyn.crewmate.api.PluginLoader;
import me.alexisevelyn.crewmate.enums.TerminalColors;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.bus.EventBus;
import me.alexisevelyn.crewmate.handlers.FragmentPacketHandler;
import me.alexisevelyn.crewmate.handlers.GamePacketHandler;
import me.alexisevelyn.crewmate.handlers.HandshakeHandler;
import me.alexisevelyn.crewmate.handlers.PingHandler;

import java.io.File;
import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.ResourceBundle;

public class Server extends Thread {
	// https://www.scadacore.com/tools/programming-calculators/online-hex-converter/

	private final DatagramSocket socket;
	private boolean running = false;
	private final byte[] buf = new byte[256];

	private final InetAddress boundIP;
	private final int port;
	private final int maxPlayers;

	private final EventBus eventBus = new EventBus();

	private final File projectFolder;
	private final File serversFolder;
	private final File root;
	private final File pluginsFolder;

	public Server() throws SocketException {
		// null means bind to any address
		this(22023, null, 15000);
	}

	public Server(int maxPlayers) throws SocketException {
		this(22023, null, maxPlayers);
	}

	public EventBus getEventBus() {
		return eventBus;
	}

	public Server(int port, InetAddress bindAddress, int maxPlayers) throws SocketException {
		this.socket = new DatagramSocket(port, bindAddress);

		this.port = this.socket.getLocalPort();
		this.boundIP = this.socket.getLocalAddress();
		this.maxPlayers = maxPlayers;

		projectFolder = new File(System.getProperty("user.dir"));
		if (!projectFolder.exists()) projectFolder.mkdirs();
		serversFolder = new File(projectFolder, "servers");
		if (!serversFolder.exists()) serversFolder.mkdirs();
		root = new File(serversFolder, port + "");
		if (!root.exists()) root.mkdirs();
		pluginsFolder = new File(root, "plugins");
		if (!pluginsFolder.exists()) pluginsFolder.mkdirs();

		for (Plugin plugin : PluginLoader.loadPlugins(pluginsFolder, this)) {
			LogHelper.printLine(plugin.getId());
		}
	}

	@Override
	public void run() {
		ResourceBundle translation = Main.getTranslationBundle();
		
		// For Cleaning Up When Shutdown
		this.setupShutdownHook();

		running = true;
		boolean justStarted = false;

		while (this.isRunning()) {
			DatagramPacket packet = new DatagramPacket(this.buf, this.buf.length);

			if (!justStarted) {
				justStarted = true;

				LogHelper.printLine(translation.getString("server_started"));

				// For Title
				LogHelper.print(
						TerminalColors.getTitle(
								String.format(translation.getString("server_listening_title"), this.boundIP.getHostAddress(), this.port)
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

	public int getMaxPlayers() {
		return maxPlayers;
	}

	// TODO: TODO TODO - https://discord.com/channels/757425025379729459/759066383090188308/765419168466993162
	// "Yeah, lengths for Hazel messages are always 2 bytes little-endian" - codyphobe from Imposter Discord
	private void parsePacketAndReply(DatagramPacket packet) throws IOException {
		if (packet.getData().length < 1)
			return;

		SendOption sendOption = SendOption.getSendOption(packet.getData()[0]);

		// Throw Out Any Unknown Packets
		// Sanitization Check
		if (sendOption == null)
			return;

		byte[] replyBuffer;
		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				replyBuffer = HandshakeHandler.handleHandshake(packet, this);
				break;
			case ACKNOWLEDGEMENT: // Reply To Ping
			case PING: // Ping
				replyBuffer = PingHandler.handlePing(packet);
				break;
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				replyBuffer = GamePacketHandler.handleReliablePacket(packet, this);
				break;
			case NONE: // Generic Unreliable Packet - Used For Movement (Unknown If Used For Anything Else)
				replyBuffer = GamePacketHandler.handleUnreliablePacket(packet);
				break;
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
				replyBuffer = FragmentPacketHandler.handleFragmentPacket(packet);
				break;
			default:
				return;
		}

		// Don't Send Packet if No Data To Send
		if (replyBuffer.length == 0)
			return;

		// Received Packet Port and Address
		InetAddress address = packet.getAddress();
		int port = packet.getPort();

		// Packet to Send Back to Client
		packet = this.createSendPacket(replyBuffer, replyBuffer.length, address, port);

		// Send Reply Back
		this.socket.send(packet);

		// TODO: Check if Reply is Disconnect and Disconnect From Our End!!!
		// SendOption.DISCONNECT...
	}

	public void sendPacket(DatagramPacket packet) {
		try {
			this.socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public DatagramPacket createSendPacket(byte[] buffer, int length, InetAddress address, int port) {
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

		LogHelper.printLine(Main.getTranslationBundle().getString("server_shutdown"));

		// This never runs.
		this.socket.close();
	}

	public boolean isRunning() {
		return this.running;
	}

	private void setupShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> Main.getServer().exit()));
	}

	public InetAddress getBoundIP() {
		return this.boundIP;
	}

	public int getPort() {
		return this.port;
	}
}
