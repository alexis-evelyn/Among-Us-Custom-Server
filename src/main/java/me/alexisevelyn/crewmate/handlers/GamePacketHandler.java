package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;

import java.net.DatagramPacket;

public class GamePacketHandler {
	// TODO: Read Game Packet Data
	public static byte[] handleGamePacket(DatagramPacket packet) {
		// Validate Packet Size
		// TODO: Figure Out Minimum Length
		// TODO: Figure Out Proper Way To Identify Game Types
		if (packet.getLength() == 11)
			return StartGame.getClientGameCode(packet);
		if (packet.getLength() == 49)
			return StartGame.getNewGameSettings(packet);
		if (packet.getLength() == -1)
			return StartGame.get(packet);
		if (packet.getLength() == 50)
			return SearchGame.handleSearchPublicGame(packet);

		return new byte[0];
	}

	public static Map[] parseMapsSearch(int mapNumber) {
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

	public static String getMapName(int map) {
		if (map == Map.SKELD.getMap())
			return "The Skeld";
		else if (map == Map.MIRA_HQ.getMap())
			return  "Mira HQ";
		else if (map == Map.POLUS.getMap())
			return  "Polus";

		return  "Unknown";
	}

	public static String getLanguageName(int language) {
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
