package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

// Client: v2020.9.9a = vvvvvvvv (Current Release At Time of Writing) - Android
// Client: v2020.10.8i = 50518400 (Current Beta At Time of Writing) - Steam

public class HandshakePacket {
	private static char[] letters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	public static byte[] handleHandshake(DatagramPacket packet, Server server) {
		// 0000   08 00 01 00 46 d2 02 03 06 41 6c 65 78 69 73      ....F....Alexis
		// 0000   08 00 01 00 46 d2 02 03 03 50 4f 4d               ....F....POM
		// 0000   08 00 01 00 00 02 18 00                           ........
		// 0000   08 00 01 00 80 d9 02 03 06 41 6c 65 78 69 73      .........Alexis (Steam Beta - v2020.10.8i)
		// 0000   08 00 01 00 46 d2 02 03 06 41 6c 65 78 69 73      ....F....Alexis (Android Release - v2020.9.9a)

		byte[] buffer = packet.getData();

		// buffer[8] is the length of the name immediately after
		if (buffer.length > 9 && buffer[8] != 0 && (packet.getLength() - 9) >= buffer[8]) {
			// Hazel Version
			int hazelVersion = buffer[1];

			// Client Version
			byte[] clientVersionBytes = new byte[4]; // Int-32
			System.arraycopy(buffer, 4, clientVersionBytes, 0, 4);

			// Client Version Info
			int clientVersionRaw = ByteBuffer.wrap(clientVersionBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

			// Player Display Name
			byte[] nameBytes = new byte[buffer[8]];
			System.arraycopy(buffer, 9, nameBytes, 0, buffer[8]);
			String name = new String(nameBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

			// Debug Logging
			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("name_logged"), name));
			logVersionInfo(clientVersionRaw);

			PlayerManager.addPlayer(new Player(name, packet.getAddress(), packet.getPort(), hazelVersion, clientVersionRaw, server));

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
		byte[] encodedPort = PacketHelper.convertShortToLE(port);

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

	private static void logVersionInfo(long raw) {
		/* Client Version Reversal
		 *
		 * year = floor(version / 25000)
		 * version %= 25000
		 * month = floor(version / 1800)
		 * version %= 1800
		 * day = floor(version / 50)
		 * revision = version % 50
		 */

		// https://gist.github.com/codyphobe/c95a30eba17d613852aa251976382ad7
		// version = (year * 25000) + (month * 1800) + (day * 50) + revision
		// 50516551 = (2020 * 25000) + (9 * 1800) + (7 * 50) + 1

		// Temp Var
		// raw = 50516551L; // Version: 50516551 - 09/07/2020 Revision: 1
		long clientVersion = raw;

		// Year
		long year = (long) Math.floor(clientVersion / 25000.0);

		// Month
		clientVersion %= 25000;
		long month = (long) Math.floor(clientVersion / 1800.0);

		// Day
		clientVersion %= 1800;
		long day = (long) Math.floor(clientVersion / 50.0);

		// Revision Number
		long revision = clientVersion % 50;

		// Attempts to Grab Letter - If Fail, Then Use Number Instead
		String revisionLetter = (revision >= 0 && revision < letters.length) ? String.valueOf(letters[(int) revision]) : String.valueOf(revision);

		ResourceBundle translation = Main.getTranslationBundle();
		Map<String, Object> parameters = new HashMap<>();

		// Date
		parameters.put("month", String.format("%02d", month));
		parameters.put("day", String.format("%02d", day));
		parameters.put("year", String.format("%02d", year));

		// Other
		parameters.put("revision", revisionLetter);
		parameters.put("raw", raw);

		String logLine = LogHelper.formatNamed(translation.getString("client_version_logged"), parameters);
		LogHelper.printLine(logLine);
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
