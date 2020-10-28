package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.*;
import me.alexisevelyn.crewmate.api.Game;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.HostGameEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;

public class StartGame {
	/**
	 * TODO: Missing A Lot Of Information
	 *
	 * @param server
	 * @param clientAddress
	 * @param clientPort
	 * @param byteLength
	 * @param reliableBytes
	 * @return
	 * @throws InvalidGameCodeException
	 * @throws InvalidBytesException
	 */
	public static byte[] getNewGameSettings(Server server, InetAddress clientAddress, int clientPort, int byteLength, byte... reliableBytes) throws InvalidGameCodeException, InvalidBytesException {
		// 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a
		// --------------------------------------------------------------------------------------------------------------------------------
		// 2a 02 07 00 01 00 00 02 00 00 80 3f 00 00 80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00 00 02 01 0f 00 00 00 78 00 00 00 01 0f
		// --------------------------------------------------------------------------------------------------------------------------------
		// TODO: Add Full Decipher of Payload Bytes - https://wiki.weewoo.net/wiki/Game_Options_Data
		// UK UK MP UK UK UK UK CM UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK IC UK UK UK UK UK UK UK UK UK UK UK
		// UK = Unknown
		// MP = Max Players
		// CM = Chosen Map
		// IC = Imposter Count

		// Useful For Verifying Bytes
		// LogHelper.printPacketBytes(byteLength, reliableBytes);
		
		// Data
		int maxPlayers = reliableBytes[2];
		int map = reliableBytes[7];
		int imposterCount = reliableBytes[31];
		Language language = Language.getLanguage(Language.convertToInt(reliableBytes[3], reliableBytes[4]));

		String mapName = Map.getMapName(Map.getMap(map));
		String languageName = Language.getLanguageName(language);

		ResourceBundle translation = Main.getTranslationBundle();
		String extraData = String.format(translation.getString("max_player_logged"), maxPlayers) + "\n" +
				String.format(translation.getString("map_logged"), mapName) + "\n" +
				String.format(translation.getString("imposter_count_logged"), imposterCount) + "\n" +
				String.format(translation.getString("language_logged"), languageName);

		LogHelper.printLine(extraData);

		try {
			// TODO: Double Check For "InvalidBytesException: Game Code Bytes Needs To Be 4 Bytes Long!!!" on Below Line
			HostGameEvent event = new HostGameEvent(GameCodeHelper.parseGameCode(getCodeFromList()), maxPlayers, imposterCount, Map.getMap(map), language);
			event.call(server);
			byte[] newCode = GameCodeHelper.generateGameCodeBytes(event.getGameCode());
			if (newCode.length != 0) {
				GameManager.addGame(new Game(newCode));
				return useCustomCode(newCode);
			}
		} catch (InvalidBytesException | InvalidGameCodeException e) {
			e.printStackTrace();
		}

		byte[] randomCode = getCodeFromList();
		GameManager.addGame(new Game(randomCode));
		return useCustomCode(randomCode);
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

	// TODO: Rewrite
	private static byte[] useCustomCode(byte... code) throws InvalidBytesException {
		if (code.length != 4)
			throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_exact"), 4));

		byte[] header = new byte[] {SendOption.RELIABLE.getSendOption(), 0x00, 0x01, 0x04, 0x00, 0x00};
		return PacketHelper.mergeBytes(header, code);
	}

	@Deprecated
	private static byte[] getCodeFromList() {
		try {

			// This is a temporary test function
			// https://stackoverflow.com/a/53673751/6828099
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

			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("server_side_exception"));
		} catch (InvalidGameCodeException exception) {
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));
		}
	}
}
