package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.api.Game;
import me.alexisevelyn.crewmate.api.GameSettings;
import me.alexisevelyn.crewmate.enums.GamePacketType;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.HostGameEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.apiguardian.api.API;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.InetAddress;
import java.net.URL;
import java.security.SecureRandom;

public class HostSettingsPacket {
	/**
	 * Retrieves the game settings from the client on game join as host.
	 *
	 * <br><br>
	 * Refer to NOT-IMPLEMENTED for the game settings that are set in the lobby.
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param payloadBytes payload bytes
	 * @return Joined Game Packet With List of Other Clients If Any Or Close Packet If Exception
	 */
	@API(status = API.Status.EXPERIMENTAL)
	@NotNull
	public static byte[] getNewGameSettings(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payloadBytes) {
		// 01 00 02 2b 00 00 2a 02 09 02 00 00 00 01 00 00 c0 3f 00 00 00 3f 00 00 80 3f 00 00 f0 41 02 02 03 01 00 00 00 03 00 0f 00 00 00 78 00 00 00 00 0f
		// RP NO NO PL PL RC
		// RP = Reliable Packet (1)
		// NO = Nonce (2)
		// PL = Packet Length (43)
		// RC = Reliable Packet Type (0x00 for Host Game)

		byte[] gameCodeBytes = getCodeFromList();
		try {
			GameSettings gameSettings = new GameSettings(payloadBytes);

			HostGameEvent event = new HostGameEvent(GameCodeHelper.parseGameCode(gameCodeBytes), gameSettings.getMaxPlayers(), gameSettings.getImposterCount(), gameSettings.getMaps()[0], gameSettings.getLanguages());
			event.call(server);

			byte[] newCode = GameCodeHelper.generateGameCodeBytes(event.getGameCode());

			GameManager.addGame(new Game(newCode));
			return useCustomCode(newCode);
		} catch (InvalidGameCodeException e) {
			// LogHelper.printLineErr(e.getMessage());
			// e.printStackTrace();

			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));
		} catch(InvalidBytesException exception) {
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("game_packet_invalid_size"));
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

			assert filePath != null : "Words List is Missing - Translate Me If Method Kept";
			out.write(filePath.openStream().readAllBytes());

			RandomAccessFile file = new RandomAccessFile(tempFile, "r");

			// For Random Position
			long length = file.length() - 1;
			SecureRandom secureRandom = new SecureRandom();
			long position = secureRandom.nextLong() * length;

			// Skip Ahead
			file.seek(position);

			// Skip to End of Line
			// TODO: Fix so it can grab the first word on the list and
			//  so it doesn't NPE for reading the line on the last word of the list
			file.readLine();
			byte[] gameCodeBytes = GameCodeHelper.generateGameCodeBytes(file.readLine());

			// Close File Reference
			file.close();

			// Get Word
			return gameCodeBytes;
		} catch (IOException | NullPointerException | AssertionError exception) {
			exception.printStackTrace();

			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("server_side_exception"));
		} catch (InvalidGameCodeException exception) {
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));
		}
	}
}
