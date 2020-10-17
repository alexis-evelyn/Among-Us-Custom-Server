package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.TerminalColors;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.handlers.GamePacketHandler;
import me.alexisevelyn.crewmate.handlers.HandshakeHandler;
import me.alexisevelyn.crewmate.handlers.PingHandler;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Arrays;

public class Server extends Thread {
	// https://www.scadacore.com/tools/programming-calculators/online-hex-converter/

	private final DatagramSocket socket;
	private boolean running = false;
	private final byte[] buf = new byte[256];

	public Server() throws SocketException {
		this(22023);
	}

	public Server(int port) throws SocketException {
		this.socket = new DatagramSocket(port);
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

				LogHelper.printLine(Main.getTranslationBundle().getString("server_started"));

				// For Title
				LogHelper.print(
						TerminalColors.getTitle(
								String.format(Main.getTranslationBundle().getString("server_listening_title"), this.socket.getLocalAddress().getHostAddress(), this.socket.getLocalPort())
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

	// TODO: TODO TODO - https://discord.com/channels/757425025379729459/759066383090188308/765419168466993162
	// "Yeah, lengths for Hazel messages are always 2 bytes little-endian" - codyphobe from Imposter Discord
	private void parsePacketAndReply(DatagramPacket packet) throws IOException {
		if (packet.getData().length < 1)
			return;

		SendOption sendOption = SendOption.getSendOption(packet.getData()[0]);

		// Throw Out Any Unknown Packets
		if (sendOption == null)
			return;

		byte[] replyBuffer;
		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				replyBuffer = HandshakeHandler.handleHandshake(packet);
				break;
			case ACKNOWLEDGEMENT: // Reply To Ping
			case PING: // Ping
				replyBuffer = PingHandler.handlePing(packet);
				break;
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				replyBuffer = GamePacketHandler.handleGamePacket(packet);
				break;
			case NONE: // Generic Unreliable Packet - Not Handled Yet
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
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
	}

	private DatagramPacket createSendPacket(byte[] buffer, int length, InetAddress address, int port) {
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
}
