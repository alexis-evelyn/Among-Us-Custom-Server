package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;

import java.net.DatagramPacket;
import java.util.ArrayList;

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
		if (packet.getLength() == 50)
			return SearchGame.handleSearchPublicGame(packet);
		if (packet.getLength() >= 173) // We don't grab the bytes here as the packet apparently gets cleared after being read.
			return StartGame.getInitialGameSettings(packet);

		return new byte[0];
	}

	public static Map[] parseMapsSearch(int mapNumber) {
		// Surely there's some way to calculate this so I can dynamically add maps later.
		// From what I'm hearing, this is a bitfield. https://discordapp.com/channels/750301084202958899/761731747762667560/765242112031064074
		// This function parses as a bitfield so if say 8 maps exist, then we don't have to have every possible map combination written in code.

		/*
		 * Math (Prior To Bitfield Knowledge)
		 *
		 *             1 (Skeld)
		 *             2 (Mira)
		 *     1 + 2 = 3 (Skeld + Mira)
		 *             4 (Polus)
		 *     1 + 4 = 5 (Skeld + Polus)
		 *     2 + 4 = 6 (Mira + Polus)
		 * 1 + 2 + 4 = 7 (All)
		 */

		/*
		 * Bitfield
		 *
		 *     ABC
		 * 1 = 001
		 * 2 = 010
		 * 3 = 011
		 * 4 = 100
		 * 5 = 101
		 * 6 = 110
		 * 7 = 111
		 *
		 * A = Polus
		 * B = Mira-HQ
		 * C = Skeld
		 */

		// What the full bytes would look like
		// 00000001 = Skeld
		// 00000010 = Mira-HQ
		// 00000100 = Polus

//		System.out.println("Skeld: " + (1 & mapNumber));
//		System.out.println("Mira-HQ: " + (2 & mapNumber));
//		System.out.println("Polus: " + (4 & mapNumber));

		int skeldBit = 0b1;
		int miraBit = 0b10;
		int polusBit = 0b100;

		ArrayList<Map> maps = new ArrayList<>();
		if ((skeldBit & mapNumber) > 0) {
			// Skeld
			maps.add(Map.SKELD);
		}

		if ((miraBit & mapNumber) > 0) {
			// Mira-HQ
			maps.add(Map.MIRA_HQ);
		}

		if ((polusBit & mapNumber) > 0) {
			// Polus
			maps.add(Map.POLUS);
		}

		// https://stackoverflow.com/a/5061692/6828099
		// Apparently it's supposed to be marked as empty now
		return maps.toArray(new Map[0]);
	}

	public static String getMapName(int map) {
		if (map == Map.SKELD.getMap())
			return "The Skeld";
		else if (map == Map.MIRA_HQ.getMap())
			return  "Mira HQ";
		else if (map == Map.POLUS.getMap())
			return  "Polus";

		return "Unknown";
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
