package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;

import java.util.ArrayList;
import java.util.ResourceBundle;

public class PackedInts implements Command {
	public void execute(String command, Terminal terminal) {
		String[] arguments = command.trim().split("\\s+");

		// Needs Argument
		if (arguments.length != 3) {
			String wrongArgumentLengthMessage = String.format(Main.getTranslation("packed_ints_invalid_argument_length"), Main.getTranslation("packed_ints_bytes_argument"), Main.getTranslation("packed_ints_int_argument"));
			LogHelper.printLineErr(wrongArgumentLengthMessage);

			return;
		}

		// Second Argument - Either bytes or string (whatever the translation has set)
		String convertType = arguments[1].trim();

		// Convert To Bytes
		if (convertType.equalsIgnoreCase(Main.getTranslation("packed_ints_bytes_argument"))) {
			this.convertIntToBytes(terminal, arguments);
		}

		// Convert To String
		if (convertType.equalsIgnoreCase(Main.getTranslation("packed_ints_int_argument")))
			this.convertBytesToInt(terminal, arguments);
	}

	// Convert Game Code String to Bytes
	private void convertIntToBytes(Terminal terminal, String... arguments) {
		byte[] packedBytes;
		try {
			packedBytes = PacketHelper.packInteger(Integer.parseInt(arguments[2]));
		} catch (NumberFormatException exception) {
			LogHelper.printLineErr(Main.getTranslation("packed_ints_invalid_int"));

			return;
		}

		LogHelper.printPacketBytes(packedBytes.length, packedBytes);
	}

	// Convert Game Code Bytes to String
	private void convertBytesToInt(Terminal terminal, String... arguments) {
		// b5:f0:90:8a
		// 01:34:67:9-
		String argument = arguments[2].trim();

		// Split Byte String to Separate Array Sections
		String[] preParsedBytes = argument.split(":");

		// Convert byte list to bytes
		ArrayList<Byte> bytes = new ArrayList<>();
		try {
			for (String bite : preParsedBytes) {
				bytes.add((byte) Integer.parseInt(bite, 16));
			}
		} catch (NumberFormatException exception) {
			LogHelper.printLineErr(Main.getTranslation("packed_ints_invalid_hex"));

			return;
		}

		// Store Converted Bytes To byte[]
		Byte[] mergedBytes = bytes.toArray(new Byte[0]);
		byte[] mergedBytesPrimitive = new byte[mergedBytes.length];

		for (int i = 0; i < mergedBytesPrimitive.length; i++) {
			mergedBytesPrimitive[i] = mergedBytes[i]; // Apparently Unboxing Is Automatic
		}

		try {
			int unpackInteger = PacketHelper.unpackInteger(mergedBytesPrimitive);

			LogHelper.printLine(String.format(Main.getTranslation("packed_ints_string"), unpackInteger));
		} catch (InvalidBytesException exception) {
			LogHelper.printLineErr(exception.getMessage());
		}
	}

	@Override
	public String getCommand() {
		return Main.getTranslation("packed_ints_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslation("packed_ints_command_help");
	}
}
