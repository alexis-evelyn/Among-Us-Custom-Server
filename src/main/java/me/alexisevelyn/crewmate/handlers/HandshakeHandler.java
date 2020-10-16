package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

import java.math.BigInteger;
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

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("name_logged"), name));

			// Start Ping
			return new byte[] {SendOption.ACKNOWLEDGEMENT.getSendOption(), 0x00, 0x01, (byte) 0xff};
			// return this.getUnderConstructionMessage(name); // this.getFakeMastersList(packet);
			// return getFakeMastersList(packet);
		}

		// Invalid Packet Received - Close Connection
		return new byte[] {SendOption.DISCONNECT.getSendOption()};
	}

	private static byte[] getFakeMastersList(DatagramPacket packet) {
		// TODO: Figure out how to fix this!!!

		// https://github.com/alexis-evelyn/Among-Us-Protocol/wiki/Master-Servers-List
		// https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

		// TODO: Check if InetSocketAddress
		InetSocketAddress queriedIP = (InetSocketAddress) packet.getSocketAddress();

		// Convert Port to Little Endian Bytes
		short port = 22023; // TODO: Figure out how to extract port number from packet
		byte[] encodedPort = PacketHelper.convertShortToLE(port); // This is supposed to be 0x07 0x56, but for some reason is 0x07 0x86 (for port 22023)
		encodedPort = new byte[] {0x07, 0x56};

		String fakeMasterName = "Pseudo-Master-1";
		int numberOfMasters = 1;

		// LogHelper.printLine("Queried IP: " + queriedIP.getAddress());
		// LogHelper.printLine("Queried Port: " + port);

		// LogHelper.printLine("Encoded IP: " + Arrays.toString(queriedIP.getAddress().getAddress()));
		LogHelper.printLine(String.format(Main.getTranslationBundle().getString("encoded_port_logged"), Arrays.toString(encodedPort)));

		// Convert Player Count to Little Endian Bytes
		short playerCount = 257;
		byte[] playerCountBytes = PacketHelper.convertShortToLE(playerCount);

		// LogHelper.printLine("Player Count: " + Arrays.toString(playerCountBytes));

		byte[] endMessage = new byte[] {encodedPort[0], encodedPort[1], playerCountBytes[0], playerCountBytes[1]}; // Another Unknown if Not Last Master in List

		byte[] message = PacketHelper.mergeBytes(fakeMasterName.getBytes(), queriedIP.getAddress().getAddress(), endMessage);

		// 00 38 00 0e 01 02 18
		// The + 4 from (message.length + 4) comes from starting at (byte) numberOfMasters
//		LogHelper.printLine("Message Length: " + Arrays.toString(BigInteger.valueOf(255 + 4).toByteArray()));
//		LogHelper.printLine("Message Length Reversed: " + Arrays.toString(BigInteger.valueOf(Integer.reverseBytes(255 + 4)).toByteArray()));

		byte[] messageLength = BigInteger.valueOf(Integer.reverseBytes(message.length + 5)).toByteArray();
		byte[] masterBytesLength = BigInteger.valueOf(Integer.reverseBytes(fakeMasterName.getBytes().length + 5)).toByteArray();
		byte[] header = new byte[] {SendOption.NONE.getSendOption(), messageLength[0], messageLength[1], MasterBytes.FLAG.getMasterByte(), MasterBytes.UNKNOWN.getMasterByte(), (byte) numberOfMasters, masterBytesLength[0], masterBytesLength[1], MasterBytes.UNKNOWN_FLAG_TEMP.getMasterByte(), (byte) fakeMasterName.getBytes().length};

		return PacketHelper.mergeBytes(header, message);
	}

	private enum MasterBytes {
		FLAG((byte) 0x0e),
		UNKNOWN((byte) 0x01),
		UNKNOWN_FLAG_TEMP((byte) 0x00);

		private final byte masterByte;

		MasterBytes(byte masterByte) {
			this.masterByte = masterByte;
		}

		public byte getMasterByte() {
			return this.masterByte;
		}
	}
}
