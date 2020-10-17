package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class SearchGame {
	// https://gist.github.com/codyphobe/af35532e650ef332b14af413b6328273

	public static byte[] handleSearchPublicGame(DatagramPacket packet) {
		// Request Search
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
		StringBuilder printableMapsList = new StringBuilder();

		// Append Delimiters (Usually Comma + Space) To List and Then Remove Last Delimiter
		String delimiter = Main.getTranslationBundle().getString("list_delimiter_logged");
		for (Map map : maps) {
			printableMapsList.append(Map.getMapName(map)).append(delimiter);
		}
		printableMapsList.delete(printableMapsList.length() - delimiter.length(), printableMapsList.length());

		// Language To Search By
		Language language = Language.getLanguage(Language.convertToInt(buffer[10], buffer[11]));

		ResourceBundle translation = Main.getTranslationBundle();
		LogHelper.printLine(String.format(translation.getString("imposter_count_logged"), (numberOfImposters == 0) ? translation.getString("imposter_count_any_logged") : numberOfImposters));
		LogHelper.printLine(String.format(translation.getString("maps_logged"), printableMapsList.toString()));
		LogHelper.printLine(String.format(translation.getString("language_logged"), Language.getLanguageName(language)));

		return getFakeSearchBytes(numberOfImposters, maps, language.getLanguage());
	}

	private static byte[] getFakeSearchBytes(int numberOfImposters, Map[] maps, int language) {
		// Search Results - ff 00 = 255 In INT16 - Little Endian (BA) - 19 00 = 25 In INT16 - Little Endian (BA)
		// 0000   01 00 1c 02 01 10 ff 00 00 19 00 00 68 ed 80 75   ............h..u
		// 0010   07 56 e6 71 31 80 08 73 75 73 68 69 69 69 69 03   .V.q1..sushiiii.
		// 0020   b4 03 00 02 0a 14 00 00 c0 51 87 18 07 56 c5 94   .........Q...V..
		// 0030   13 80 03 59 6f 75 02 ed 01 00 02 0a 13 00 00 c0   ...You..........
		// 0040   51 82 3b 07 56 3b 4c 1d 80 02 42 61 06 ba 2b 00   Q.;.V;L...Ba..+.
		// 0050   02 0a 17 00 00 2d 4f 6e 6a 07 56 3a 3c 38 80 07   .....-Onj.V:<8..
		// 0060   4e 6f 74 4b 69 77 69 03 1d 00 02 07 14 00 00 2d   NotKiwi........-
		// 0070   4f 6e 19 07 56 a1 d9 07 80 03 79 75 68 09 ae 09   On..V.....yuh...
		// 0080   00 02 0a 15 00 00 2d 4f 6c 6b 07 56 29 d4 4c 80   ......-Olk.V).L.
		// 0090   04 59 6f 59 6f 03 c5 0b 00 02 0a 15 00 00 2d 4f   .YoYo.........-O
		// 00a0   14 15 07 56 48 8a 59 80 05 6a 65 6c 6c 79 02 40   ...VH.Y..jelly.@
		// 00b0   00 02 0a 1a 00 00 45 a4 c1 e9 07 56 a4 f9 48 80   ......E....V..H.
		// 00c0   09 42 75 62 62 61 62 75 74 65 05 84 0b 00 02 0a   .Bubbabute......
		// 00d0   18 00 00 2d 4f 61 87 07 56 4b 08 29 80 07 43 72   ...-Oa..VK.)..Cr
		// 00e0   69 73 74 61 6c 03 a4 02 00 02 0a 1a 00 00 ad e6   istal...........
		// 00f0   9e 08 07 56 a2 ec 2b 80 0a 41 6e 6e 65 20 46 72   ...V..+..Anne Fr
		// 0100   61 6e 6b 02 46 00 02 0a                           ank.F...

		// Search Results (One Result) - 1b 00 = 27 In INT16 - Little Endian (BA) - 18 00 = 24 In INT16 - Little Endian (BA)
		// 0000   01 00 15 1e 00 10 1b 00 00 18 00 00 c6 3a 7b 62   .............:{b
		// 0010   07 56 1d d0 01 80 08 73 68 79 20 70 69 6e 6b 01   .V.....shy pink.
		// 0020   61 01 01 04                                       a...

		// Search Results (One Result) - 19 00 = 25 In INT16 - Little Endian (BA)
		// 0000   01 00 0f 1c 00 10 19 00 00 16 00 00 ad ff ff d4   ................
		// 0010   07 56 4d e2 35 80 06 6a 61 79 64 65 6e 01 06 01   .VM.5..jayden...
		// 0020   01 0a                                             ..

		// Search Results (One Result)
		// 0000   01 00 2f 1c 00 10 19 00 00 16 00 00 ad ff ff d4   ../.............
		// 0010   07 56 4d e2 35 80 06 6a 61 79 64 65 6e 03 29 01   .VM.5..jayden.).
		// 0020   01 0a                                             ..

		// Search Results (No Results)
		// 0000   01 00 0f 03 00 10 00 00 00                        .........

		// Search Results (Two Results) - Game Code For Result 2 (EVAWJF - 10:1e:6c:8c)
		// 0000   01 00 36 36 00 10 33 00 00 16 00 00 ad ed 11 4c   ..66..3........L
		// 0010   cf 56 10 1e 6c 8c 06 4b 61 72 6f 6c 47 01 11 01   .V..l..KarolG...
		// 0020   02 0a 17 00 00 48 0e b5 a0 07 56 5f ad 25 80 06   .....H....V_.%..
		// 0030   50 6c 61 79 65 72 02 f6 0a 01 03 09               Player......

		// Search Results (One Result) - Game Code For Result (FCUBWQ - f9:51:13:80)
		// 0000   01 00 19 1c 00 10 19 00 00 16 00 00 32 74 07 64   ............2t.d
		// 0010   07 56 f9 51 13 80 05 61 66 66 61 6e 02 9b 02 01   .V.Q...affan....
		// 0020   02 08                                             ..

		// Search Results (One Result) - Game Code For Result (BGQFYQ - 4d:49:39:80) - 20 1f = 7968 In INT16 - Little Endian (BA)
		// Polus Only (0x02) - Arabic (0x20 0x00) - 2 Imposters - Name: "bunda eki" - 1 out of 7 max people
		// 0000   01 00 20 1f 00 10 1c 00 00 19 00 00 2d 38 52 d6   .. .........-8R.
		// 0010   07 56 4d 49 39 80 09 62 75 6e 64 61 20 65 6b 69   .VMI9..bunda eki
		// 0020   01 31 02 02 07                                    .1...

		// Search Results (One Result) - Game Code For Result (USCWTQ - f8:f4:2a:80) - 01 1c = 7169 In INT16 - Little Endian (BA)
		// Mira-HQ Only (0x01) - Korean (0x04 0x00) - 1 Imposter - Name: "freddy" - 1 out of 4 max people
		// 0000   01 00 01 1c 00 10 19 00 00 16 00 00 2d 21 22 14   ............-!".
		// 0010   07 56 f8 f4 2a 80 06 66 72 65 64 64 79 01 20 01   .V..*..freddy. .
		// 0020   01 04                                             ..

		// No Results - Displays Text At Bottom Showing Total Games Per Map (All 0 Games)
		// This 0x01 turns on the list ----V
		// 0000   01 00 01 12 00 10 0c 00 01 00 00 00 00 00 00 00   ................
		// 0010   00 00 00 00 00 00 00 00                           ........

		if (maps.length == 0)
			return new byte[0];

		// Temporary String - Not Translating
		// Visible Game Information
		String name = "Fake Game - " + Language.getLanguageName(Language.getLanguage(language));
		int imposterCount = (numberOfImposters != 0) ? numberOfImposters : 6;
		int playerCount = 3;
		int maxPlayerCount = 10;
		int mapID = maps[0].getMap();

		// Total Number of Games
		// TODO: Figure out how to get game list to display when showing total games!!!
		boolean showTotalGames = false;
		int skeldCount = 1;
		int miraCount = 2;
		int polusCount = 3;

		byte[] totalSkeldCount = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(skeldCount).array();
		byte[] totalMiraCount = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(miraCount).array();
		byte[] totalPolusCount = ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(polusCount).array();

		byte[] totalGames = showTotalGames ? PacketHelper.mergeBytes(totalSkeldCount, totalMiraCount, totalPolusCount) : new byte[0];

		// Age of Game - TODO: Figure out what this means!!!
		byte[] age = new byte[] {(byte) 0xa3, 0x02}; // No Idea What Format This Is In

		// IP Address and Port
		byte[] ipAddress = new byte[] {0x7f, 0x00, 0x00, 0x01}; // 127.0.0.1
		byte[] port = new byte[] {0x07, 0x56}; // 22023

		// Game Code
		byte[] gameCodeBytes = new byte[] {(byte) 0xb5, (byte) 0xf0, (byte) 0x90, (byte) 0x8a}; // b5:f0:90:8a - ALEXIS

		// Lengths
		int gameBytesLength = 17 + name.getBytes().length; // The 17 comes from the non-variable length portions of the Game Info Bytes.
		byte[] fakeGameLength = PacketHelper.convertShortToLE((short) gameBytesLength);
		byte[] messageLength = PacketHelper.convertShortToLE((short) (3 + totalGames.length + gameBytesLength)); // The 3 comes from the fact that the message is non-variable in size.

		// Header - TODO: Figure out what unknownBytes means!!!
		byte[] unknownBytes = new byte[] {0x01, 0x0f}; // https://gist.github.com/codyphobe/af35532e650ef332b14af413b6328273
		byte[] header = new byte[] {SendOption.RELIABLE.getSendOption(), 0x00, unknownBytes[0], unknownBytes[1], SearchBytes.POTENTIAL_FLAG.getSearchByte(), SearchBytes.GAME_LIST_VERSION.getSearchByte(), messageLength[0], messageLength[1],
				showTotalGames ? SearchBytes.SHOW_TOTAL_GAMES.getSearchByte() : SearchBytes.HIDE_TOTAL_GAMES.getSearchByte()};

		// Outputs Blank List, But Shows Text At Bottom As Skeld - 1, Mira - 2, Polus - 3
//		return new byte[] {0x01, 0x00, 0x01, 0x12, 0x00, 0x10, 0x0C, 0x00, 0x01,
//				0x01, 0x00, 0x00, 0x00, // Skeld
//				0x02, 0x00, 0x00, 0x00, // Mira
//				0x03, 0x00, 0x00, 0x00, // Polus
//				0x00, 0x00, 0x00};

		return PacketHelper.mergeBytes(header,
				totalGames,
				fakeGameLength,
				new byte[] {SearchBytes.POTENTIAL_FLAG.getSearchByte()},
				ipAddress,
				port,
				gameCodeBytes,
				new byte[] {(byte) name.getBytes().length},
				name.getBytes(),
				new byte[] {(byte) playerCount},
				age,
				new byte[] {(byte) mapID},
				new byte[] {(byte) imposterCount},
				new byte[] {(byte) maxPlayerCount});
	}

	public static Map[] parseMapsSearch(int mapNumber) {
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

//		LogHelper.printLine("Skeld: " + (1 & mapNumber));
//		LogHelper.printLine("Mira-HQ: " + (2 & mapNumber));
//		LogHelper.printLine("Polus: " + (4 & mapNumber));

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

	private enum SearchBytes {
		GAME_LIST_VERSION((byte) 0x10), // V2
		POTENTIAL_FLAG((byte) 0x00),
		SHOW_TOTAL_GAMES((byte) 0x01),
		HIDE_TOTAL_GAMES((byte) 0x00);

		private final byte searchByte;

		SearchBytes(byte searchByte) {
			this.searchByte = searchByte;
		}

		public byte getSearchByte() {
			return this.searchByte;
		}
	}
}
