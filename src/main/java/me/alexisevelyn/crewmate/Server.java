package me.alexisevelyn.crewmate;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.nio.charset.StandardCharsets;
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
			case 0x08:
				replyBuffer = handleHandshake(packet);
				break;
			case 0x0a:
			case 0x0c:
				replyBuffer = handlePing(packet);
				break;
			default:
				return;
		}

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

	private byte[] handleHandshake(DatagramPacket packet) {
		// 0000   08 00 01 00 46 d2 02 03 03 50 4f 4d               ....F....POM
		// 0000   08 00 01 00 00 02 18 00                           ........

		byte[] buffer = packet.getData();
		if (buffer.length > 9) {
			byte[] name = new byte[packet.getLength() - 9];
			System.arraycopy(buffer, 9, name, 0, packet.getLength() - 9);

			System.out.println("Name: " + new String(name, StandardCharsets.UTF_8)); // Can we assume it will always be UTF-8?
		}

		return new byte[0];
	}

	private byte[] handlePing(DatagramPacket packet) {

		return new byte[0];
	}
}
