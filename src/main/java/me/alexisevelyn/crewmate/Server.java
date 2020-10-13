package me.alexisevelyn.crewmate;

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
	private final DatagramSocket socket;
	private boolean running = false;
	private final byte[] buf = new byte[256];

	public Server() throws SocketException {
		this(22023);
	}

	public Server(int port) throws SocketException {
		socket = new DatagramSocket(port);
	}

	public void run() {
		running = true;

		while (running) {
			DatagramPacket packet = new DatagramPacket(buf, buf.length);

			try {
				// Receive Packet From Client
				socket.receive(packet);

				// Parse Packet
				this.parsePacketAndReply(packet);

				// Clear Buffer
				this.clearBuffer();
			} catch (IOException e) {
				running = false;

				System.err.println("IOException: " + e.getMessage());
				e.printStackTrace();
			}
		}

		System.out.println("Server Shutdown!!!");
		socket.close();
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
		socket.send(packet);
	}

	private DatagramPacket createSendPacket(byte[] buffer, int length, InetAddress address, int port) {
		byte[] sendBuffer = new byte[length];

		if (length >= 0)
			System.arraycopy(buffer, 0, sendBuffer, 0, length);

		return new DatagramPacket(sendBuffer, sendBuffer.length, address, port);
	}

	private void clearBuffer() {
		Arrays.fill(this.buf, (byte) 0x0);
	}
}
