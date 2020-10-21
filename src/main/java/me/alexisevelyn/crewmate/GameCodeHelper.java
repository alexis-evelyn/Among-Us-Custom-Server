package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameCodeHelper {
	// https://discord.com/channels/757425025379729459/759066383090188308/765437094582943774
	// The source above was missing the V between the C and B.
	private static final char[] gameCodeLetters = "QWXRTYLPESDFGHUJKZOCVBINMA".toCharArray();

	// https://wiki.weewoo.net/wiki/Game_Codes#Version_2
	private static final byte[] v2Map = new byte[] {0x19, 0x15, 0x13, 0x0a, 0x08, 0x0b, 0x0c, 0x0d, 0x16, 0x0f, 0x10, 0x06, 0x18, 0x17, 0x12, 0x07, 0x00, 0x03, 0x09, 0x04, 0x0e, 0x14, 0x01, 0x02, 0x05, 0x11};

	// https://www.geeksforgeeks.org/bitwise-operators-in-java/
	// https://gist.github.com/alexis-evelyn/f541d27811b62fd987c93cf79ed049a7
	// Convert Bytes to GameCode String
	public static String parseGameCode(byte[] gameCodeBytes) throws InvalidBytesException, InvalidGameCodeException {
		if (gameCodeBytes == null)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("gamecode_null_exception"));

		if (gameCodeBytes.length != 4)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("gamecode_invalid_length_exception"));

		// Save Bytes as V1 Game Code and Then Verify
		String gameCodeString = new String(gameCodeBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

		// Check if V1 Style Game Code
		if (gameCodeString.matches("[A-Z]+"))
			return gameCodeString;

		// This point onward is most likely V2 Game Code

		// https://stackoverflow.com/a/7619315
		int gameCodeInteger = ByteBuffer.wrap(gameCodeBytes).order(ByteOrder.LITTLE_ENDIAN).getInt();

		// This works because the game will null terminate an invalid gamecode
		if (gameCodeInteger == 0)
			throw new InvalidGameCodeException(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));

		try {
			return convertIntToGameCode(gameCodeInteger);
		} catch (ArrayIndexOutOfBoundsException exception) {
			throw new InvalidBytesException(Main.getTranslationBundle().getString("gamecode_invalid_bytes_exception"));
		}
	}

	// V2 - Convert Bytes to GameCode String
	// TODO: Fix This
	private static String convertIntToGameCode(int input) throws ArrayIndexOutOfBoundsException {
		int a = input & 0x3FF;
		int b = (input >> 10) & 0xFFFFF;

		// https://gist.github.com/codyphobe/1478d7b8794ab52d4ff9fc673c944058
		return Stream.of(
				gameCodeLetters[a % 26],
				gameCodeLetters[a / 26],
				gameCodeLetters[b % 26],
				gameCodeLetters[b / 26 % 26],
				gameCodeLetters[b / (26 * 26) % 26],
				gameCodeLetters[b / (26 * 26 * 26) % 26]
		).map(Object::toString).collect(Collectors.joining(""));
	}

	// Convert String to GameCode Bytes
	public static byte[] generateGameCodeBytes(String gameCode) throws InvalidGameCodeException {
		// Game Codes Can Be 4 or 6 Capital Letters Long
		// Technically the client allows numbers in the game code, but it results in an integer 0.

		// Ensure GameCode Is Valid Or Convertible To Valid
		if (!gameCode.matches("([A-Z]|[a-z])+"))
			throw new InvalidGameCodeException(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));

		String fixedCode = gameCode.toUpperCase();

		// 4 Digit Room Codes are just The ASCII Bytes
		if (fixedCode.length() == 4)
			return fixedCode.getBytes();
		else if (fixedCode.length() == 6)
			return generateGameCodeV2(fixedCode);

		throw new InvalidGameCodeException(Main.getTranslationBundle().getString("gamecode_wrong_length"));
	}

	// V2 - Convert String to GameCode Bytes
	private static byte[] generateGameCodeV2(String gameCode) throws InvalidGameCodeException {
		if (gameCode.length() < 6)
			throw new InvalidGameCodeException(Main.getTranslationBundle().getString("gamecode_wrong_length_v2"));

		int a = v2Map[gameCode.charAt(0) - 65];
		int b = v2Map[gameCode.charAt(1) - 65];
		int c = v2Map[gameCode.charAt(2) - 65];
		int d = v2Map[gameCode.charAt(3) - 65];
		int e = v2Map[gameCode.charAt(4) - 65];
		int f = v2Map[gameCode.charAt(5) - 65];

		int one = (a + 26 * b) & 0x3FF;
		int two = (c + 26 * (d + 26 * (e + 26 * f)));

		int gameCodeInt = one | ((two << 10) & 0x3FFFFC00) | 0x80000000;

		return ByteBuffer.allocate(4).order(ByteOrder.LITTLE_ENDIAN).putInt(gameCodeInt).array();
	}
}
