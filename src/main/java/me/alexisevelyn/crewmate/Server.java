package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;

import java.io.IOException;
import java.net.*;
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
			case 0x01:
				replyBuffer = handleGamePacket(packet);
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
		// 0000   08 00 01 00 46 d2 02 03 06 41 6c 65 78 69 73      ....F....Alexis
		// 0000   08 00 01 00 46 d2 02 03 03 50 4f 4d               ....F....POM
		// 0000   08 00 01 00 00 02 18 00                           ........

		byte[] buffer = packet.getData();

		// buffer[8] is the length of the name immediately after
		if (buffer.length > 9 && buffer[8] != 0 && (packet.getLength() - 9) >= buffer[8]) {
			byte[] nameBytes = new byte[buffer[8]];
			System.arraycopy(buffer, 9, nameBytes, 0, buffer[8]);
			String name = new String(nameBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

			System.out.println("Name: " + name);

			// Start Ping
			return new byte[] {0x0a, 0x00, 0x01, (byte) 0xff};
			// return this.getUnderConstructionMessage(name); // this.getFakeMastersList(packet);
		}

		// Invalid Packet Received - Close Connection
		return new byte[] {0x09};
	}

	private byte[] handlePing(DatagramPacket packet) {
		byte[] buffer = packet.getData();

		if (packet.getLength() == 3 && buffer[0] == 0x0c) {
			System.out.println("Received 0x0c Ping: " + buffer[1] + " " + buffer[2]);
			return new byte[] {0x0c, buffer[1], buffer[2]};
		} else if (packet.getLength() == 4 && buffer[0] == 0x0a) {
			System.out.println("Received 0x0a Ping: " + buffer[1] + " " + buffer[2]);
			return new byte[] {0x0a, buffer[1], buffer[2], (byte) 0xff};
		}

		return new byte[0]; //this.getUnderConstructionMessage("player");
	}

	// TODO: Read Game Packet Data
	private byte[] handleGamePacket(DatagramPacket packet) {
		// 0000   01 00 02 2b 00 00 2a 02 0a 00 01 00 00 00 00 00   ...+..*.........
		// 0010   80 3f 00 00 40 3f 00 00 80 3f 00 00 f0 41 01 01   .?..@?...?...A..
		// 0020   03 01 00 00 00 02 00 00 00 00 00 87 00 00 00 00   ................
		// 0030   0f                                                .

		// 0000   01 00 12 05 00 01 00 00 00 00 07                  ...........

		// Validate Packet Size
		// TODO: Figure Out Minimum Length
		if (packet.getLength() != 49)
			return new byte[0];

		byte[] buffer = packet.getData();

		// Data
		int maxPlayers = buffer[8];
		int map = buffer[13];
		int imposterCount = buffer[37];
		int language = Language.convertToInt(buffer[9], buffer[10]);

		String mapName;
		if (map == Map.SKELD.getMap())
			mapName = "The Skeld";
		else if (map == Map.MIRA_HQ.getMap())
			mapName = "Mira HQ";
		else if (map == Map.POLUS.getMap())
			mapName = "Polus";
		else
			mapName = "Unknown";

		String languageName;
		if (language == Language.ARABIC.getLanguage())
			languageName = "Arabic";
		else if (language == Language.ENGLISH.getLanguage())
			languageName = "English";
		else if (language == Language.FILIPINO.getLanguage())
			languageName = "Filipino";
		else if (language == Language.KOREAN.getLanguage())
			languageName = "Korean";
		else if (language == Language.OTHER.getLanguage())
			languageName = "Other";
		else if (language == Language.POLISH.getLanguage())
			languageName = "Polish";
		else if (language == Language.PORTUGUESE.getLanguage())
			languageName = "Portuguese";
		else if (language == Language.RUSSIAN.getLanguage())
			languageName = "Russian";
		else if (language == Language.SPANISH.getLanguage())
			languageName = "Spanish";
		else
			languageName = "Unknown";

		StringBuilder extraData = new StringBuilder("Max Players: " + maxPlayers);
		extraData.append("\n").append("Map: ").append(mapName);
		extraData.append("\n").append("Imposters: ").append(imposterCount);
		extraData.append("\n").append("Language: ").append(languageName);

		return this.closeWithMessage(extraData.toString());
	}

	private byte[] getFakeMastersList(DatagramPacket packet) {
		// Until I Understand More About the Masters List, This Is What I'm Returning
		// TODO: Figure out what the unknown bytes are to make the client happy!!!
		// https://github.com/alexis-evelyn/Among-Us-Protocol/wiki/Master-Servers-List

		// TODO: Check if InetSocketAddress
		InetSocketAddress queriedIP = (InetSocketAddress) packet.getSocketAddress();

		String fakeMasterName = "Pseudo-Master-1";
		int numberOfMasters = 1;

		System.out.println("Queried IP: " + queriedIP.getAddress());
		System.out.println("Encoded IP: " + Arrays.toString(queriedIP.getAddress().getAddress()));

		// Represent Unknown Data
		int unknown = 0;

		byte[] endMessage = new byte[] {0x07, 0x56, (byte) unknown, (byte) unknown}; // Another Unknown if Not Last Master in List
		byte[] ipMessage = this.getCombinedReply(fakeMasterName.getBytes(), queriedIP.getAddress().getAddress());

		byte[] message = this.getCombinedReply(ipMessage, endMessage);

		// 00 38 00 0e 01 02 18
		// The + 4 from (message.length + 4) comes from starting at (byte) numberOfMasters
		byte[] header = new byte[] {0x00, (byte) (message.length + 4), 0x00, 0x0e, (byte) numberOfMasters, (byte) unknown, 0x00, 0x00, (byte) fakeMasterName.getBytes().length};

		byte[] reply = this.getCombinedReply(header, message);

		System.out.println("Masters List Bytes: " + Arrays.toString(reply));

		return reply;
	}

	private byte[] closeWithMessage(String message) {
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.getBytes().length};

		return this.getCombinedReply(header, message.getBytes());
	}

	private byte[] getUnderConstructionMessage(String extra) {
		// 0000   09 01 2e 00 00 08 2c 54 68 65 20 73 65 72 76 65   ......,The serve
		// 0010   72 20 63 6c 6f 73 65 64 20 74 68 65 20 72 6f 6f   r closed the roo
		// 0020   6d 20 64 75 65 20 74 6f 20 69 6e 61 63 74 69 76   m due to inactiv
		// 0030   69 74 79                                          ity

		StringBuilder messageBuilder = new StringBuilder("This Server Is Under Construction. :P");

		if (extra.length() > 0)
			messageBuilder.append("\n\n").append("Extra Info: ").append("\n").append(extra);

		byte[] message = messageBuilder.toString().getBytes();
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.length};

		return this.getCombinedReply(header, message);
	}

	private byte[] getCombinedReply(byte[] header, byte[] message) {
		byte[] reply = new byte[header.length + message.length];

		// Copy Header into Reply
		System.arraycopy(header, 0, reply, 0, header.length);

		// Copy Message into Reply
		System.arraycopy(message, 0, reply, header.length, message.length);

		return reply;
	}
}
