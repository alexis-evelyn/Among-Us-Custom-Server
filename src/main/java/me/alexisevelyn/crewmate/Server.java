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

		socket.close();
	}

	private void parsePacketAndReply(DatagramPacket packet) throws IOException {
		if (packet.getData().length < 1)
			return;

		byte[] replyBuffer;
		switch (packet.getData()[0]) {
			// I don't know how to reference this particular enum in a switch statement
			case 0x08: // SendOption.HELLO
				replyBuffer = HandshakeHandler.handleHandshake(packet);
				break;
			case 0x0a: // SendOption.ACKNOWLEDGEMENT
			case 0x0c: // SendOption.PING
				replyBuffer = PingHandler.handlePing(packet);
				break;
			case 0x01: // SendOption.RELIABLE
				replyBuffer = GamePacketHandler.handleGamePacket(packet);
				break;
			case 0x00: // SendOption.NONE (Not Handled Yet)
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
