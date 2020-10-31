package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.*;
import me.alexisevelyn.crewmate.api.Game;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.GamePacketType;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.HostGameEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.apiguardian.api.API;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.URL;
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
	@API(status = API.Status.EXPERIMENTAL)
	public static byte[] getNewGameSettings(Server server, InetAddress clientAddress, int clientPort, int byteLength, byte... reliableBytes) {
		// 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a
		// --------------------------------------------------------------------------------------------------------------------------------
		// 2a 02 07 00 01 00 00 02 00 00 80 3f 00 00 80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00 00 02 01 0f 00 00 00 78 00 00 00 01 0f
		// --------------------------------------------------------------------------------------------------------------------------------
		// TODO: Add Full Decipher of Payload Bytes - https://wiki.weewoo.net/wiki/Game_Options_Data
		// PL GV MP LA LA LA LA CM UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK UK IC UK UK UK UK UK UK UK UK UK UK UK
		// PL = Payload Length As Packed Int
		// GV = Game Options Version
		// MP = Max Players
		// LA = Language
		// UK = Unknown
		// CM = Chosen Map
		// IC = Imposter Count

		// Useful For Verifying Bytes
		// LogHelper.printPacketBytes(byteLength, reliableBytes);

		// Data
		int maxPlayers = reliableBytes[2];
		byte map = reliableBytes[7];
		int imposterCount = reliableBytes[31];

		long languageInt = PacketHelper.getUnsignedIntLE(reliableBytes[3], reliableBytes[4], reliableBytes[5], reliableBytes[6]);
		Language[] languages = Language.getLanguageArray(languageInt);

		Map hostMap = Map.getMap(map);

		if (hostMap == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("unknown_map"));

		String mapName = Map.getMapName(hostMap);

		ResourceBundle translation = Main.getTranslationBundle();
		String extraData = String.format(translation.getString("max_player_logged"), maxPlayers) + "\n" +
				String.format(translation.getString("map_logged"), mapName) + "\n" +
				String.format(translation.getString("imposter_count_logged"), imposterCount) + "\n" +
				String.format(translation.getString("languages_logged"), Language.getPrintableLanguagesList(languages));

		LogHelper.printLine(extraData);

		byte[] gameCodeBytes = getCodeFromList();
		try {
			// TODO: Double Check For "InvalidBytesException: Game Code Bytes Needs To Be 4 Bytes Long!!!" on Below Line
			// TODO: Validate Languages[] is not empty
			HostGameEvent event = new HostGameEvent(GameCodeHelper.parseGameCode(gameCodeBytes), maxPlayers, imposterCount, Map.getMap(map), languages);
			event.call(server);

			byte[] newCode = GameCodeHelper.generateGameCodeBytes(event.getGameCode());

			GameManager.addGame(new Game(newCode));
			return useCustomCode(newCode);
		} catch (InvalidBytesException | InvalidGameCodeException e) {
			LogHelper.printLineErr(e.getMessage());
			e.printStackTrace();

			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));
		}
	}

	// For S->C
	@API(status = API.Status.EXPERIMENTAL)
	private static byte[] getRandomGameCode() {
		byte[] nonce = new byte[] {(byte) 0xbb, 0x01};
		byte[] gameCode = getCodeFromList(); // Game Code
		byte[] gameCodeSize = PacketHelper.convertShortToLE((short) gameCode.length);
		byte hostGame = (byte) GamePacketType.HOST_SETTINGS.getReliablePacketType();

		// The client will respond with a packet that triggers getNewGameSettings(...);
		return new byte[] {SendOption.RELIABLE.getByte(), nonce[0], nonce[1],
				gameCodeSize[0], gameCodeSize[1], hostGame,
				gameCode[0], gameCode[1], gameCode[2], gameCode[3]};
	}

	// TODO: Rewrite
	@API(status = API.Status.EXPERIMENTAL)
	private static byte[] useCustomCode(byte... code) throws InvalidBytesException {
		if (code.length != 4)
			throw new InvalidBytesException(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_exact"), 4));

		byte[] header = new byte[] {SendOption.RELIABLE.getByte(), 0x00, 0x01, 0x04, 0x00, 0x00};
		return PacketHelper.mergeBytes(header, code);
	}

	@Deprecated
	@API(status = API.Status.EXPERIMENTAL)
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
