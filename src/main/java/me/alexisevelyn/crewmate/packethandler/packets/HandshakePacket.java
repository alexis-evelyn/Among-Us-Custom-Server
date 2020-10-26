package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
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

public class HandshakePacket {
	@SuppressWarnings("SpellCheckingInspection") // I mean, I appreciate the IDE wanting to make sure I don't make a typo, but please, the whole alphabet doesn't even look like a word
	private static final char[] revisionLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();

	/**
	 * Parse out handshake packet and register player
	 *
	 * @param packet Handshake packet
	 * @param server Server instance
	 * @return Masters list or empty byte array
	 */
	public static byte[] handleHandshake(DatagramPacket packet, Server server) {
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

			// TODO: Replace this with configurable choosing of returning masters list
			return new byte[0];
		}

		// Invalid Packet Received - Close Connection
		return new byte[] {SendOption.DISCONNECT.getSendOption()};
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
		String revisionLetter = (revision >= 0 && revision < revisionLetters.length) ? String.valueOf(revisionLetters[(int) revision]) : String.valueOf(revision);

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
}
