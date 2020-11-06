package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.util.ResourceBundle;

public class GameCode implements Command {
	public void execute(String command, Terminal terminal) {
		ResourceBundle translation = Main.getTranslationBundle();
		String[] arguments = command.trim().split("\\s+");

		// Needs Argument
		if (arguments.length != 3) {
			String wrongArgumentLengthMessage = String.format(translation.getString("gamecode_invalid_argument_length"), translation.getString("gamecode_bytes_argument"), translation.getString("gamecode_string_argument"));
			LogHelper.printLineErr(wrongArgumentLengthMessage);

			return;
		}

		// Second Argument - Either bytes or string (whatever the translation has set)
		String convertType = arguments[1].trim().toLowerCase();

		// Convert To Bytes
		if (convertType.equals(translation.getString("gamecode_bytes_argument"))) {
			try {
				this.convertStringToBytes(terminal, arguments);
			} catch (InvalidGameCodeException e) {
				e.printStackTrace();
			}
		}

		// Convert To String
		if (convertType.equals(translation.getString("gamecode_string_argument")))
			this.convertBytesToString(terminal, arguments);
	}

	// Convert Game Code String to Bytes
	private void convertStringToBytes(Terminal terminal, String... arguments) throws InvalidGameCodeException {
		byte[] gameCodeBytes = GameCodeHelper.generateGameCodeBytes(arguments[2]);

		// Game Code is not valid if true
		if (gameCodeBytes.length == 0) {
			String invalidGameCodeMessage = Main.getTranslationBundle().getString("gamecode_not_valid");

			LogHelper.printLineErr(invalidGameCodeMessage);
			return;
		}

		LogHelper.printPacketBytes(gameCodeBytes.length, gameCodeBytes);
	}

	// Convert Game Code Bytes to String
	private void convertBytesToString(Terminal terminal, String... arguments) {
		// b5:f0:90:8a
		// 01:34:67:9-
		String argument = arguments[2].trim();

		// Needs to be Valid String Length
		if (argument.length() != 11) {
			LogHelper.printLineErr(Main.getTranslationBundle().getString("gamecode_string_invalid_length"));

			return;
		}

		// Needs to be Valid String Format
		if (argument.charAt(2) != ':' || argument.charAt(5) != ':' || argument.charAt(8) != ':') {
			LogHelper.printLineErr(Main.getTranslationBundle().getString("gamecode_string_invalid_format"));

			return;
		}

		// Split Byte String to Separate Array Sections
		String[] preParsedBytes = argument.split(":");

		// Convert byte list to bytes
		byte first, second, third, fourth;
		try {
			first = (byte) Integer.parseInt(preParsedBytes[0], 16);
			second = (byte) Integer.parseInt(preParsedBytes[1], 16);
			third = (byte) Integer.parseInt(preParsedBytes[2], 16);
			fourth = (byte) Integer.parseInt(preParsedBytes[3], 16);
		} catch (NumberFormatException exception) {
			LogHelper.printLineErr(Main.getTranslationBundle().getString("gamecode_invalid_hex"));

			return;
		}

		// Store Converted Bytes To byte[]
		byte[] gameCodeBytes = new byte[] {first, second, third, fourth};

		try {
			String gameCodeString = GameCodeHelper.parseGameCode(gameCodeBytes);

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("gamecode_string"), gameCodeString));
		} catch (InvalidGameCodeException | InvalidBytesException exception) {
			LogHelper.printLineErr(exception.getMessage());
		}
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("gamecode_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslationBundle().getString("gamecode_command_help");
	}
}
