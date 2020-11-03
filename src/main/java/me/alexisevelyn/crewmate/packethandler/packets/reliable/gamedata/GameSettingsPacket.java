package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

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

public class GameSettingsPacket {
	/**
	 * Retrieves the game settings from the client on game join as host.
	 *
	 * <br><br>
	 * Refer to NOT-IMPLEMENTED for the game settings that are set in the lobby.
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param byteLength count of payload bytes
	 * @param payloadBytes payload bytes
	 * @return Joined Game Packet With List of Other Clients If Any Or Close Packet If Exception
	 */
	@API(status = API.Status.EXPERIMENTAL)
	public static byte[] getNewGameSettings(Server server, InetAddress clientAddress, int clientPort, int byteLength, byte... payloadBytes) {
    /*
	     00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b 1c 1d 1e 1f 20 21 22 23 24 25 26 27 28 29 2a 2b 2c 2d 2e
	     --------------------------------------------------------------------------------------------------------------------------------------------
	     2a 02 07 00 01 00 00 02 00 00 80 3f 00 00 80 3f 00 00 c0 3f 00 00 34 42 01 01 02 01 00 00 00 02 01 0f 00 00 00 78 00 00 00 01 0f
	     --------------------------------------------------------------------------------------------------------------------------------------------
	     PL GV MP LA LA LA LA CM PS PS PS PS CL CL CL CL IL IL IL IL KC KC KC KC CT LT ST EM EM EM EM IC KD DT DT DT DT VT VT VT VT DS EC CE VI AV TU
	     PL = Payload Length As Packed Int (Supposed to be uint-32?)
	     GV = Game Options Version
	     MP = Max Players
	     LA = Language
	     PS = Player Speed Modifier (Float 32)
	     CL = Crew Light Modifier (Float 32)
	     IL = Imposter Light Modifier (Float 32)
	     KC = Kill Cooldown (Float 32)
	     CT = Common Tasks (Count)
	     LT = Long Tasks (Count)
	     ST = Short Tasks (Count)
	     EM = Emergencies (Int-32)
	     CM = Chosen Map
	     IC = Imposter Count
	     KD = Kill Distance (Unknown Units)
	     DT = Discussion Time (Int-32)
	     VT = Voting Time (Int-32)
	     DS = Default Settings
	     EC = Emergency Cooldown (Version 2+)

	     The game doesn't send these at all when starting a lobby for hosting the game
	     CE = Confirm Ejects (Version 3+)
	     VI = Visual Tasks (Version 3+)
	     AV = Anonymous Voting (Version 4+)
	     TU = Task Bar Updates (Version 4+)
    */

    // Useful For Verifying Bytes
    // LogHelper.printPacketBytes(byteLength, payloadBytes);

    // Ensure Minimum Size
    if (payloadBytes.length < 0x29 || (byteLength != payloadBytes.length))
      return ClosePacket.closeWithMessage(
          Main.getTranslationBundle().getString("game_packet_invalid_size"));

		// Data
		int payloadLength = PacketHelper.unpackInteger(payloadBytes[0]); // Currently Always 42 (on Beta)
		int gameOptionsVersion = payloadBytes[1];
		int maxPlayers = payloadBytes[2];
		long languageInt = PacketHelper.getUnsignedIntLE(payloadBytes[3], payloadBytes[4], payloadBytes[5], payloadBytes[6]);
		byte map = payloadBytes[7];

		// Floats - TODO: Implement
		float playerSpeedModifier = PacketHelper.getUnsignedFloatLE(payloadBytes[8], payloadBytes[9], payloadBytes[10], payloadBytes[11]);
		float crewLightModifier = PacketHelper.getUnsignedFloatLE(payloadBytes[12], payloadBytes[13], payloadBytes[14], payloadBytes[15]);
		float imposterLightModifier = PacketHelper.getUnsignedFloatLE(payloadBytes[16], payloadBytes[17], payloadBytes[18], payloadBytes[19]);
		float killCooldown = PacketHelper.getUnsignedFloatLE(payloadBytes[20], payloadBytes[21], payloadBytes[22], payloadBytes[23]);

		// Counts
		int commonTaskCount = payloadBytes[24];
		int longTaskCount = payloadBytes[25];
		int shortTaskCount = payloadBytes[26];
		int emergencyMeetingCount = (int) PacketHelper.getUnsignedIntLE(payloadBytes[27], payloadBytes[28], payloadBytes[29], payloadBytes[30]);
		int imposterCount = payloadBytes[31];

		// Distance
		int killDistance = payloadBytes[32];

		// Time
		int discussionTime = (int) PacketHelper.getUnsignedIntLE(payloadBytes[33], payloadBytes[34], payloadBytes[35], payloadBytes[36]);
		int voteTime = (int) PacketHelper.getUnsignedIntLE(payloadBytes[37], payloadBytes[38], payloadBytes[39], payloadBytes[40]);

		// Default Settings
		byte defaultSettings = payloadBytes[41]; // TODO: Convert To Boolean

		byte emergencyCooldown = payloadBytes[42];
//		byte confirmEjects = payloadBytes[43]; // TODO: Convert To Boolean
//		byte visualTasks = payloadBytes[44]; // TODO: Convert To Boolean
//		byte anonymousVoting = payloadBytes[45]; // TODO: Convert To Boolean
//		byte taskBarUpdates = payloadBytes[46];

		// TODO: Make this changeable without recompile
		if (maxPlayers < 0 || maxPlayers > 15)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_max_players"), 0, 15, maxPlayers));

		// TODO: Make this changeable without recompile
		if (imposterCount < 0 || imposterCount > 3)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_imposter_count"), 0, 15, imposterCount));

		Language[] languages = Language.getLanguageArray(languageInt);

		if (languages.length <= 0 || languages[0].equals(Language.UNSPECIFIED))
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("unspecified_language"));

		Map hostMap = Map.getMap(map);

		if (hostMap == null || hostMap.equals(Map.UNSPECIFIED))
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("unknown_map"));

		String mapName = Map.getMapName(hostMap);

		ResourceBundle translation = Main.getTranslationBundle();

		// Debug Info About Game Settings
		// Non-User Info
		LogHelper.printLine(String.format(translation.getString("payload_length_logged"), payloadLength));
		LogHelper.printLine(String.format(translation.getString("game_options_version_logged"), gameOptionsVersion));

		// Host Settings
		LogHelper.printLine(String.format(translation.getString("max_player_logged"), maxPlayers));
		LogHelper.printLine(String.format(translation.getString("map_logged"), mapName));
		LogHelper.printLine(String.format(translation.getString("imposter_count_logged"), imposterCount));
		LogHelper.printLine(String.format(translation.getString("languages_logged"), Language.getPrintableLanguagesList(languages)));

		// In Lobby Settings
		LogHelper.printLine(String.format(translation.getString("common_task_count_logged"), commonTaskCount));
		LogHelper.printLine(String.format(translation.getString("long_task_count_logged"), longTaskCount));
		LogHelper.printLine(String.format(translation.getString("short_task_count_logged"), shortTaskCount));
		LogHelper.printLine(String.format(translation.getString("emergency_meeting_count_logged"), emergencyMeetingCount));
		LogHelper.printLine(String.format(translation.getString("kill_distance_logged"), killDistance));
		LogHelper.printLine(String.format(translation.getString("discussion_time_logged"), discussionTime));
		LogHelper.printLine(String.format(translation.getString("vote_time_logged"), voteTime));
		LogHelper.printLine(String.format(translation.getString("emergency_cooldown_logged"), emergencyCooldown));
		LogHelper.printLine(String.format(translation.getString("default_settings_logged"), defaultSettings));

		// Floats
		LogHelper.printLine(String.format(translation.getString("player_speed_modifier_logged"), playerSpeedModifier));
		LogHelper.printLine(String.format(translation.getString("crewmate_light_modifier_logged"), crewLightModifier));
		LogHelper.printLine(String.format(translation.getString("imposter_light_modifier_logged"), imposterLightModifier));
		LogHelper.printLine(String.format(translation.getString("kill_cooldown_logged"), killCooldown));


		byte[] gameCodeBytes = getCodeFromList();
		try {
			// TODO: Double Check For "InvalidBytesException: Game Code Bytes Needs To Be 4 Bytes Long!!!" on Below Line
			HostGameEvent event = new HostGameEvent(GameCodeHelper.parseGameCode(gameCodeBytes), maxPlayers, imposterCount, Map.getMap(map), languages);
			event.call(server);

			byte[] newCode = GameCodeHelper.generateGameCodeBytes(event.getGameCode());

			GameManager.addGame(new Game(newCode));
			return useCustomCode(newCode);
		} catch (InvalidBytesException | InvalidGameCodeException e) {
			// LogHelper.printLineErr(e.getMessage());
			// e.printStackTrace();

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
