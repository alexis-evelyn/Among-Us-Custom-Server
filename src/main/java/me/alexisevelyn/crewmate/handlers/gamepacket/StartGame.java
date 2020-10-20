package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.*;
import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.HostGameEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.io.*;
import java.net.DatagramPacket;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

public class StartGame {
	public static byte[] getNewGameSettings(DatagramPacket packet, Server server) {
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
		Language language = Language.getLanguage(Language.convertToInt(buffer[9], buffer[10]));

		String mapName = Map.getMapName(Map.getMap(map));
		String languageName = Language.getLanguageName(language);

		ResourceBundle translation = Main.getTranslationBundle();
		String extraData = String.format(translation.getString("max_player_logged"), maxPlayers) + "\n" +
				String.format(translation.getString("map_logged"), mapName) + "\n" +
				String.format(translation.getString("imposter_count_logged"), imposterCount) + "\n" +
				String.format(translation.getString("language_logged"), languageName);

		LogHelper.printLine(extraData);

		try {
			HostGameEvent event = new HostGameEvent(GameCodeHelper.parseGameCode(getCodeFromList()), maxPlayers, imposterCount, Map.getMap(map), language);
			event.call(server);
			byte[] newCode = GameCodeHelper.generateGameCodeBytes(event.getGameCode());
			if (newCode.length != 0) return useCustomCode(newCode);
		} catch (InvalidBytesException | InvalidGameCodeException e) {
			e.printStackTrace();
		}

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

		byte[] header = new byte[] {SendOption.RELIABLE.getSendOption(), 0x00, 0x01, 0x04, 0x00, 0x00};
		byte[] message = getCodeFromList(); // Game Code

		// The client will respond with a packet that triggers handleJoinPrivateGame(DatagramPacket);
		return PacketHelper.mergeBytes(header, message);
	}

	private static byte[] useCustomCode(byte[] code) {
		byte[] header = new byte[] {SendOption.RELIABLE.getSendOption(), 0x00, 0x01, 0x04, 0x00, 0x00};
		return PacketHelper.mergeBytes(header, code);
	}

	@Deprecated
	private static byte[] getCodeFromList() {
		// This is a temporary test function
		// https://stackoverflow.com/a/53673751/6828099

		try {
			// File To Pull Words From
			URL filePath = Main.class.getClassLoader().getResource("codes/words.txt");

			// Create Temporary File
			File tempFile = File.createTempFile("words", ".tmp");
			tempFile.deleteOnExit();

			// Copy Word List to Temporary File
			FileOutputStream out = new FileOutputStream(tempFile);
			out.write(filePath.openStream().readAllBytes());

			RandomAccessFile file = new RandomAccessFile(tempFile, "r");

			// For Random Position
			long length = file.length() - 1;
			long position = (long) (Math.random() * length);

			// Skip Ahead
			file.seek(position);

			// Skip to End of Line
			// TODO: Fix so it can grab the first word on the list and
			//  so it doesn't NPE for reading the line on the last word of the list
			file.readLine();

			// Get Word
			return GameCodeHelper.generateGameCodeBytes(file.readLine());
		} catch (IOException | NullPointerException exception) {
			exception.printStackTrace();

			return GameCodeHelper.generateGameCodeBytes("FAIL");
		}
	}

	// This gets called when the client either tries to join a game or create a game.
	// For C->S
	public static byte[] getClientGameCode(DatagramPacket packet, Server server) {
		// Game Code - AMLQTQ (89:5a:2a:80) - Purple - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 89 5a 2a 80 07                  .......Z*..

		// Game Code - SZGEYQ (c3:41:38:80) - Purple - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 c3 41 38 80 07                  .......A8..

		// Game Code - SIXLXQ (45:9a:17:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 45 9a 17 80 07                  ......E....

		if (packet.getLength() != 11)
			return new byte[0];

		// 00 03 05 00 01?
		byte[] buffer = packet.getData();
		byte[] gameCodeBytes = new byte[4];
		Map[] maps = SearchGame.parseMapsSearch(buffer[10]); // Client Owned Maps

		// Extract Game Code Bytes From Buffer
		System.arraycopy(buffer, 6, gameCodeBytes, 0, 4);

		String gameCode;
		try {
			gameCode = GameCodeHelper.parseGameCode(gameCodeBytes);

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("gamecode_string"), gameCode));
		} catch (InvalidBytesException e) {
			LogHelper.printLineErr(String.format(Main.getTranslationBundle().getString("gamecode_exception"), e.getMessage()));
			e.printStackTrace();

			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("gamecode_server_side_error_exception"));
		} catch (InvalidGameCodeException e) {
			return PacketHelper.closeWithMessage(e.getMessage());
		}

		// LogHelper.printLine("Game Code (Byte Form): " + Arrays.toString(gameCodeBytes));
		// LogHelper.printLine(String.format(Main.getTranslationBundle().getString("gamecode_integer_form_logged"), gameCode));
		// LogHelper.printLine(String.format(Main.getTranslationBundle().getString("owned_maps"), SearchGame.getPrintableMapsList(maps)));

		// Game Code - NPGWQQ (cd:98:00:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 cd 98 00 80 07                  ...........
		//
		// S->C - 0000   01 00 02 0d 00 07 cd 98 00 80 f5 e9 1e 00 f5 e9   ................
		// S->C - 0010   1e 00 00 06 00 0a cd 98 00 80 01 00               ............

		// Game Code - TVJUXQ (0c:0e:1b:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 0c 0e 1b 80 07                  ...........
		//
		// S->C - 0000   01 00 02 0d 00 07 0c 0e 1b 80 94 04 02 00 94 04   ................
		// S->C - 0010   02 00 00 06 00 0a 0c 0e 1b 80 01 00               ............

		byte unknown = 0x00;

		byte[] header = new byte[] {SendOption.RELIABLE.getSendOption(), 0x00, 0x02, 0x0d, 0x00, 0x07};

		byte[] messagePartOne = new byte[] {unknown, unknown, unknown, 0x00, unknown, unknown, unknown, 0x00, 0x00, 0x06, 0x00, 0x0a};
		byte[] messagePartTwo = new byte[] {0x01, 0x00};

		PlayerJoinEvent event = new PlayerJoinEvent(gameCode, packet.getAddress(), packet.getPort());
		event.call(server);
		if (event.isCancelled()) {
			return PacketHelper.closeConnection(event.getReason(), DisconnectReason.CUSTOM);
		}

		// This is enough to get to the lobby
		return PacketHelper.mergeBytes(header, gameCodeBytes, messagePartOne, gameCodeBytes, messagePartTwo);
	}

	public static byte[] getInitialGameSettings(DatagramPacket packet) {
		// TODO: Figure out what this packet is for!!!

		// Username Hi
		// C->S - 0000   01 00 04 ac 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 01 0f 01 01 00   .......x........
		// C->S - 0080   00 05 00 02 04 06 02 48 69 03 00 02 04 08 00 03   .......Hi.......
		// C->S - 0090   00 02 04 11 00 03 00 02 04 09 2b 03 00 02 04 0a   ..........+.....
		// C->S - 00a0   00 0e 00 02 02 1e 09 00 00 02 48 69 00 00 00 00   ..........Hi....
		// C->S - 00b0   00 00                                             ..

		// Username Alexis
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 01 0f 01 01 00   .......x........
		// C->S - 0080   00 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 00 03 00 02 04 11 00 03 00 02 04 09 2b 03   ..............+.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 00 00 00 00 00 00                     exis......

		// Username Alexis - Anonymous Votes On and Recommended Off
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 80 3f 00 00   .............?..
		// C->S - 0060   80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00   .?...?..4B......
		// C->S - 0070   00 01 01 0f 00 00 00 78 00 00 00 00 0f 01 01 01   .......x........
		// C->S - 0080   00 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 08 03 00 02 04 11 00 03 00 02 04 09 2c 03   ..............,.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 08 00 00 00 00 00                     exis......

		// Username Alexis - Everything Changed From Default
		// C->S - 0000   01 00 04 b4 00 05 00 00 00 00 0c 00 04 02 fe ff   ................
		// C->S - 0010   ff ff 0f 00 01 01 00 00 01 12 00 04 03 fe ff ff   ................
		// C->S - 0020   ff 0f 00 02 02 01 00 01 00 03 01 00 01 00 1c 00   ................
		// C->S - 0030   04 04 00 01 03 04 02 00 01 01 00 05 00 00 01 06   ................
		// C->S - 0040   0a 00 01 00 00 ff 7f ff 7f ff 7f ff 7f 31 00 02   .............1..
		// C->S - 0050   04 02 2e 04 0a 01 00 00 00 00 00 00 40 40 00 00   ............@@..
		// C->S - 0060   a0 40 00 00 a0 40 00 00 70 42 02 03 05 09 00 00   .@...@..pB......
		// C->S - 0070   00 01 02 78 00 00 00 2c 01 00 00 00 3c 00 00 01   ...x...,....<...
		// C->S - 0080   01 09 00 02 04 06 06 41 6c 65 78 69 73 03 00 02   .......Alexis...
		// C->S - 0090   04 08 08 03 00 02 04 11 00 03 00 02 04 09 2c 03   ..............,.
		// C->S - 00a0   00 02 04 0a 00 12 00 02 02 1e 0d 00 00 06 41 6c   ..............Al
		// C->S - 00b0   65 78 69 73 08 00 00 00 00 00                     exis......

		if (packet.getLength() < 4)
			return new byte[0];

		byte[] buffer = packet.getData();

		// Must Equal 01 00 03 (Join Game Via Code) or 01 00 04 (Create Game)
		if (!(buffer[0] == SendOption.RELIABLE.getSendOption() && buffer[1] == 0x00) || !(buffer[2] == 0x04 || buffer[2] == 0x03))
			return new byte[0];

		byte unknown = buffer[3]; // 180 for Alexis and 172 for Hi - +8
		byte unknownTwo = buffer[129]; // 9 for Alexis and 5 for Hi - +4

		byte nameLength = buffer[134];

		// Extract Name Bytes From Buffer
		byte[] nameBytes = new byte[nameLength];
		System.arraycopy(buffer, 135, nameBytes, 0, nameLength);

		byte unknownThree = buffer[159 + nameLength]; // Probably Associated With nameLength (Is 18 for Alexis and 14 for Hi) - +4
		byte unknownFour = buffer[164 + nameLength]; // Is 13 for Alexis and 9 for Hi - +4

		byte nameLengthTwo = buffer[167 + nameLength];
		byte[] nameBytesTwo = new byte[nameLength];
		System.arraycopy(buffer, 168 + nameLength, nameBytesTwo, 0, nameLengthTwo);

		// Temporary Logging, So No Translations
		LogHelper.printLineErr("Unknown: " + unknown);
		LogHelper.printLineErr("Unknown 2: " + unknownTwo);
		LogHelper.printLineErr("Unknown 3: " + unknownThree);
		LogHelper.printLineErr("Unknown 4: " + unknownFour);

		LogHelper.printLineErr("Name: " + new String(nameBytes, StandardCharsets.UTF_8));
		LogHelper.printLineErr("Name 2: " + new String(nameBytesTwo, StandardCharsets.UTF_8));

		return new byte[0];
	}
}
