package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.PacketHelper;

import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class HandshakeHandler {
	public static byte[] handleHandshake(DatagramPacket packet) {
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

	private static byte[] getFakeMastersList(DatagramPacket packet) {
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
		byte[] ipMessage = PacketHelper.getCombinedReply(fakeMasterName.getBytes(), queriedIP.getAddress().getAddress());

		byte[] message = PacketHelper.getCombinedReply(ipMessage, endMessage);

		// 00 38 00 0e 01 02 18
		// The + 4 from (message.length + 4) comes from starting at (byte) numberOfMasters
		byte[] header = new byte[] {0x00, (byte) (message.length + 4), 0x00, 0x0e, (byte) numberOfMasters, (byte) unknown, 0x00, 0x00, (byte) fakeMasterName.getBytes().length};

		byte[] reply = PacketHelper.getCombinedReply(header, message);

		System.out.println("Masters List Bytes: " + Arrays.toString(reply));

		return reply;
	}
}
