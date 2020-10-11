package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;

public class GameCodeHelper {
	// https://www.geeksforgeeks.org/bitwise-operators-in-java/
	public static String parseGameCode(byte[] gameCodeBytes) throws InvalidBytesException {
		if (gameCodeBytes == null)
			throw new InvalidBytesException("Check to make sure your assigned your game code variable before passing it to me!!!");

		if (gameCodeBytes.length != 4)
			throw new InvalidBytesException("Game Code Bytes Needs To Be 4 Bytes Long!!!");

		// https://stackoverflow.com/a/7619315
		// Don't Reverse Bytes Like `Integer.reverseBytes(gameCodeInteger);`. It's already reversed apparently.
		int gameCodeInteger = gameCodeBytes[0] << 24 | (gameCodeBytes[1] & 0xFF) << 16 | (gameCodeBytes[2] & 0xFF) << 8 | (gameCodeBytes[3] & 0xFF);

		// TODO: Figure Out How To Continue Reversing Bytes To Game Code

		return String.valueOf(gameCodeInteger);
	}
}
