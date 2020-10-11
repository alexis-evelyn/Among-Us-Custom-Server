package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;

import java.net.DatagramPacket;
import java.util.Arrays;

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
		// 0000   01 00 02 2c 00 10 00 2a 02 0a 00 01 00 00 01 00   ...,...*........
		// 0010   00 80 3f 00 00 80 3f 00 00 c0 3f 00 00 70 41 01   ..?...?...?..pA.
		// 0020   01 02 01 00 00 00 02 01 0f 00 00 00 78 00 00 00   ............x...
		// 0030   01 0f                                             ..

		if (packet.getLength() != 50)
			return new byte[0];

		byte[] buffer = packet.getData();

		// 0 Means Any
		int numberOfImposters = buffer[38];

		// List of Maps Being Included In Search
		Map[] maps = parseMapsSearch(buffer[14]);

		// Language To Search By
		int language = Language.convertToInt(buffer[10], buffer[11]);

		System.out.println("Number of Imposters: " + ((numberOfImposters == 0) ? "Any" : numberOfImposters));
		System.out.println("Maps: " + Arrays.toString(maps));
		System.out.println("Language: " + getLanguageName(language));

		// return PacketHelper.closeWithMessage("Searching For Public Games is Not Supported Yet!!!");
		return new byte[0];
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

		String mapName = getMapName(map);
		String languageName = getLanguageName(language);

		String extraData = "Max Players: " + maxPlayers + "\n" +
				"Map: " + mapName + "\n" +
				"Imposters: " + imposterCount + "\n" +
				"Language: " + languageName;

		return PacketHelper.closeWithMessage(extraData);
	}

	private static Map[] parseMapsSearch(int mapNumber) {
		// Surely there's some way to calculate this so I can dynamically add maps later.

		/*
		 *             1 (Skeld)
		 *             2 (Mira)
		 *     1 + 2 = 3 (Skeld + Mira)
		 *             4 (Polus)
		 *     1 + 4 = 5 (Skeld + Polus)
		 *     2 + 4 = 6 (Mira + Polus)
		 * 1 + 2 + 4 = 7 (All)
		 */

		Map[] maps;
		switch (mapNumber) {
			case 1:
				maps = new Map[] {Map.SKELD};
				break;
			case 2:
				maps = new Map[] {Map.MIRA_HQ};
				break;
			case 3:
				maps = new Map[] {Map.SKELD, Map.MIRA_HQ};
				break;
			case 4:
				maps = new Map[] {Map.POLUS};
				break;
			case 5:
				maps = new Map[] {Map.SKELD, Map.POLUS};
				break;
			case 6:
				maps = new Map[] {Map.MIRA_HQ, Map.POLUS};
				break;
			case 7:
			default:
				maps = new Map[] {Map.SKELD, Map.MIRA_HQ, Map.POLUS};
		}

		return maps;
	}

	private static String getMapName(int map) {
		if (map == Map.SKELD.getMap())
			return "The Skeld";
		else if (map == Map.MIRA_HQ.getMap())
			return  "Mira HQ";
		else if (map == Map.POLUS.getMap())
			return  "Polus";

		return  "Unknown";
	}

	private static String getLanguageName(int language) {
		if (language == Language.ARABIC.getLanguage())
			return "Arabic";
		else if (language == Language.ENGLISH.getLanguage())
			return "English";
		else if (language == Language.FILIPINO.getLanguage())
			return "Filipino";
		else if (language == Language.KOREAN.getLanguage())
			return "Korean";
		else if (language == Language.OTHER.getLanguage())
			return "Other";
		else if (language == Language.POLISH.getLanguage())
			return "Polish";
		else if (language == Language.PORTUGUESE.getLanguage())
			return "Portuguese";
		else if (language == Language.RUSSIAN.getLanguage())
			return "Russian";
		else if (language == Language.SPANISH.getLanguage())
			return "Spanish";

		return "Unknown";
	}
}
