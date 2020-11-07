package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class HandshakePacket {
	@SuppressWarnings("SpellCheckingInspection") // I mean, I appreciate the IDE wanting to make sure I don't make a typo, but please, the whole alphabet doesn't even look like a word
	private static final char[] revisionLetters = "abcdefghijklmnopqrstuvwxyz".toCharArray();
	private static final String NAME_CHECK_ONE = "([A-Za-z0-9 ])+";
	private static final String NAME_CHECK_TWO = "([ ])+";

	/**
	 * Parse out handshake packet and register player
	 *
	 * @param handshakeBytes ?
	 * @param server Server instance
	 * @return Masters list or empty byte array
	 */
	@NotNull
	public static byte[] handleHandshake(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... handshakeBytes) {
		/*
	                  V---- Starts Here (Subtract 3)
			00 01 02 03 04 05 06 07 08 09 10 11 12 13 14
			--------------------------------------------
			08 00 01 00 46 d2 02 03 06 41 6c 65 78 69 73
			HH NO NO HV CV CV CV CV NL NT NT NT NT NT NT

			HH = Hazel Hello
			NO = Nonce
			HV = Hazel Version
			CV = Client Version
			NL = Name Length
			NT = Name Text
		 */

		if (handshakeBytes.length > 6) {
			int displayNameLength = handshakeBytes[5];

			// If either the name is missing or the name length is bigger than the what the rest of the buffer has, close connection
			if (displayNameLength <= 0 || (handshakeBytes.length - 6) < displayNameLength)
				return ClosePacket.closeWithMessage(Main.getTranslation("missing_display_name_handshake"));

			// Client only allows up to 10 characters, we are enforcing that server side
			if (displayNameLength > 10)
				return ClosePacket.closeWithMessage(Main.getTranslation("display_name_too_long"));

			// Hazel Version
			int hazelVersion = handshakeBytes[0];

			// Client Version
			byte[] clientVersionBytes = new byte[4]; // Int-32 LE
			System.arraycopy(handshakeBytes, 1, clientVersionBytes, 0, 4);

			// Client Version Info
			int clientVersionRaw = ByteBuffer.wrap(clientVersionBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

			// Player Display Name
			byte[] nameBytes = new byte[displayNameLength];

			// Copy Name Out Of Handshake
			System.arraycopy(handshakeBytes, 6, nameBytes, 0, displayNameLength);
			String name = new String(nameBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

			if (!name.matches(NAME_CHECK_ONE) || name.matches(NAME_CHECK_TWO))
				return ClosePacket.closeWithMessage(Main.getTranslation("invalid_name_close_connection"));

			// Register Player On Server
			PlayerManager.addPlayer(new Player(name, clientAddress, clientPort, hazelVersion, clientVersionRaw, server));

			// TODO: Replace this with configurable choosing of returning masters list
			return new byte[0];
		}

		// Invalid Packet Received - Close Connection
		return new byte[] {SendOption.DISCONNECT.getByte()};
	}

	/**
	 * Logs Client Version to Console For Debugging
	 *
	 * <br><br>
	 * Will be replaced by an Object to represent the client version
	 *
	 * @param raw Client Version as Int-32 LE Integer
	 */
	@Deprecated
	private static void logVersionInfo(int raw) {
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

		Map<String, Object> parameters = new HashMap<>();

		// Date
		parameters.put("month", String.format("%02d", month));
		parameters.put("day", String.format("%02d", day));
		parameters.put("year", String.format("%02d", year));

		// Other
		parameters.put("revision", revisionLetter);
		parameters.put("raw", raw);

		String logLine = LogHelper.formatNamed(Main.getTranslation("client_version_logged"), parameters);
		LogHelper.printLine(logLine);
	}
}