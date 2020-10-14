package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

public class GameCodeHelper {
	// https://www.geeksforgeeks.org/bitwise-operators-in-java/
	// https://gist.github.com/alexis-evelyn/f541d27811b62fd987c93cf79ed049a7
	public static String parseGameCode(byte[] gameCodeBytes) throws InvalidBytesException, InvalidGameCodeException {
		if (gameCodeBytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("gamecode_null_exception"));

		if (gameCodeBytes.length != 4)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("gamecode_invalid_length_exception"));

		// https://stackoverflow.com/a/7619315
		// Don't Reverse Bytes Like `Integer.reverseBytes(gameCodeInteger);`. It's already reversed apparently.
		int gameCodeInteger = gameCodeBytes[0] << 24 | (gameCodeBytes[1] & 0xFF) << 16 | (gameCodeBytes[2] & 0xFF) << 8 | (gameCodeBytes[3] & 0xFF);

		if (gameCodeInteger == 0)
			throw new InvalidGameCodeException(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));

		// TODO: Figure Out How To Continue Reversing Bytes To Game Code

		return String.valueOf(gameCodeInteger);
	}

	public static byte[] generateGameCodeBytes(String gameCode) {
		// Game Codes Can Be 4 or 6 Capital Letters Long
		// Technically the client allows numbers in the game code, but it results in an integer 0.

		return new byte[0];
	}
}
