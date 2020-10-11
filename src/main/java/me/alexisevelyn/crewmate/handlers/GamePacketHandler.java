package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;

import java.net.DatagramPacket;

public class GamePacketHandler {
	// TODO: Read Game Packet Data
	public static byte[] handleGamePacket(DatagramPacket packet) {
		// Validate Packet Size
		// TODO: Figure Out Minimum Length
		// TODO: Figure Out Proper Way To Identify Game Types
		if (packet.getLength() == 11)
			return handleJoinPrivateGame(packet);
		if (packet.getLength() == 49)
			return handleCreateGame(packet);
		if (packet.getLength() == 50)
			return handleSearchPublicGame(packet);

		return new byte[0];
	}

	private static byte[] handleSearchPublicGame(DatagramPacket packet) {
		return PacketHelper.closeWithMessage("Searching For Public Games is Not Supported Yet!!!");
	}

	private static byte[] handleJoinPrivateGame(DatagramPacket packet) {
		// 0000   01 00 02 05 00 01 a3 3e e4 9b 07                  .......>...
		// 0000   01 00 02 05 00 01 37 ce 6d 97 07                  ......7.m..

		if (packet.getLength() != 11)
			return new byte[0];

		byte[] buffer = packet.getData();
		byte[] gameCodeBytes = new byte[4];

		// Extract Game Code Bytes From Buffer
		System.arraycopy(buffer, 6, gameCodeBytes, 0, 4);

		String gameCode;
		try {
			gameCode = GameCodeHelper.parseGameCode(gameCodeBytes);
		} catch (InvalidBytesException e) {
			System.out.println("Game Code Exception: " + e.getMessage());
			e.printStackTrace();

			return new byte[0];
		}

		System.out.println("Game Code: " + gameCode);

		return PacketHelper.closeWithMessage("Joining Games Is Not Supported Yet!!!");
	}

	private static byte[] handleCreateGame(DatagramPacket packet) {
		// 0000   01 00 02 2b 00 00 2a 02 0a 00 01 00 00 00 00 00   ...+..*.........
		// 0010   80 3f 00 00 40 3f 00 00 80 3f 00 00 f0 41 01 01   .?..@?...?...A..
		// 0020   03 01 00 00 00 02 00 00 00 00 00 87 00 00 00 00   ................
		// 0030   0f                                                .

		// 0000   01 00 12 05 00 01 00 00 00 00 07                  ...........

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

		String extraData = "Max Players: " + maxPlayers + "\n" +
				"Map: " + mapName + "\n" +
				"Imposters: " + imposterCount + "\n" +
				"Language: " + languageName;

		return PacketHelper.closeWithMessage(extraData);
	}
}
