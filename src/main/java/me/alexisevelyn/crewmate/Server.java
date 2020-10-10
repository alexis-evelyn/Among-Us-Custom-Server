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
			byte[] nameBytes = new byte[packet.getLength() - 9];
			System.arraycopy(buffer, 9, nameBytes, 0, packet.getLength() - 9);
			String name = new String(nameBytes, StandardCharsets.UTF_8);

			System.out.println("Name: " + name); // Can we assume it will always be UTF-8?

			return this.getUnderConstructionMessage(name);
		}

		// TODO: Handle when name is not set!!!
		return new byte[0];
	}

	private byte[] handlePing(DatagramPacket packet) {

		return new byte[0];
	}

	private byte[] getUnderConstructionMessage(String name) {
		// 0000   09 01 2e 00 00 08 2c 54 68 65 20 73 65 72 76 65   ......,The serve
		// 0010   72 20 63 6c 6f 73 65 64 20 74 68 65 20 72 6f 6f   r closed the roo
		// 0020   6d 20 64 75 65 20 74 6f 20 69 6e 61 63 74 69 76   m due to inactiv
		// 0030   69 74 79                                          ity

		byte[] message = ("This Server Is Under Construction, " + name + ". :P").getBytes();
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.length};

		byte[] reply = new byte[header.length + message.length];

		// Copy Header into Reply
		System.arraycopy(header, 0, reply, 0, header.length);

		// Copy Message into Reply
		System.arraycopy(message, 0, reply, header.length, message.length);

		return reply;
	}
}
