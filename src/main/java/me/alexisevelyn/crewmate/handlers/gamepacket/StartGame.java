package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.handlers.GamePacketHandler;

import java.net.DatagramPacket;

public class StartGame {
	public static byte[] getNewGameSettings(DatagramPacket packet) {
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

		String mapName = GamePacketHandler.getMapName(map);
		String languageName = GamePacketHandler.getLanguageName(language);

		String extraData = "Max Players: " + maxPlayers + "\n" +
				"Map: " + mapName + "\n" +
				"Imposters: " + imposterCount + "\n" +
				"Language: " + languageName;

		System.out.println(extraData);

		return getRandomGameCode();
	}

	// For S->C
	private static byte[] getRandomGameCode() {
		// Game Code - AMLQTQ (89:5a:2a:80) - Purple - Goggles - Private - 1/10 Players
		// S->C - 0000   01 00 01 04 00 00 89 5a 2a 80                     .......Z*.

		// Game Code - SZGEYQ (c3:41:38:80) - Purple - Goggles - Private - 1/10 Players
		// S->C - 0000   01 00 01 04 00 00 c3 41 38 80                     .......A8.

		// Game Code - SIXLXQ (45:9a:17:80) - Red - Goggles - Private - 1/10 Players
		// S->C - 0000   01 00 01 04 00 00 45 9a 17 80                     ......E...

		byte[] header = new byte[] {0x01, 0x00, 0x01, 0x04, 0x00, 0x00};
		byte[] message = new byte[] {0x3b, (byte) 0xbe, 0x25, (byte) 0x8c}; // Game Code - ABCDEF (3b:be:25:8c)

		// The client will respond with a packet that triggers handleJoinPrivateGame(DatagramPacket);
		return PacketHelper.getCombinedReply(header, message);
	}

	// This gets called when the client either tries to join a game or create a game.
	// For C->S
	public static byte[] getClientGameCode(DatagramPacket packet) {
		// Game Code - AMLQTQ (89:5a:2a:80) - Purple - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 89 5a 2a 80 07                  .......Z*..

		// Game Code - SZGEYQ (c3:41:38:80) - Purple - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 c3 41 38 80 07                  .......A8..

		// Game Code - SIXLXQ (45:9a:17:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 45 9a 17 80 07                  ......E....

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

		System.out.println("Game Code (Integer Form): " + gameCode);

		// Game Code - NPGWQQ (cd:98:00:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 cd 98 00 80 07                  ...........
		// S->C - 0000   01 00 02 0d 00 07 cd 98 00 80 f5 e9 1e 00 f5 e9   ................
		// S->C - 0010   1e 00 00 06 00 0a cd 98 00 80 01 00               ............

		// Game Code - TVJUXQ (0c:0e:1b:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 0c 0e 1b 80 07                  ...........
		// S->C - 0000   01 00 02 0d 00 07 0c 0e 1b 80 94 04 02 00 94 04   ................
		// S->C - 0010   02 00 00 06 00 0a 0c 0e 1b 80 01 00               ............

		byte unknown = 0x00;

		byte[] header = new byte[] {0x01, 0x00, 0x02, 0x0d, 0x00, 0x07};
		byte[] headerWithGameCode = PacketHelper.getCombinedReply(header, gameCodeBytes);

		byte[] messagePartOne = new byte[] {unknown, unknown, unknown, 0x00, unknown, unknown, unknown, 0x00, 0x00, 0x06, 0x00, 0x0a};
		byte[] messagePartTwo = new byte[] {0x01, 0x00};

		byte[] messagePartThree = PacketHelper.getCombinedReply(gameCodeBytes, messagePartTwo);
		byte[] message = PacketHelper.getCombinedReply(messagePartOne, messagePartThree);

		// This is enough to get to the lobby
		return PacketHelper.getCombinedReply(headerWithGameCode, message);
	}

	public static byte[] get(DatagramPacket packet) {
		// Next Steps

		return new byte[0];
	}
}
