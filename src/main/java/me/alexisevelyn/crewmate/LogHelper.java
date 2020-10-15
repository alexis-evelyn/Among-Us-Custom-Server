package me.alexisevelyn.crewmate;

// NOTE: This file is supposed to help with ensuring the terminal is always at the bottom and never left in place when the server logs information.

public class LogHelper {
	// https://unix.stackexchange.com/a/26592/181269
	private static String CLEAR_LINE_UP_TO_CURSOR = "\\033[1K";
	private static String CLEAR_WHOLE_LINE = "\\033[2K";

	public static void printLine() {
		System.out.println();
	}

	public static void printLine(Object line) {
		System.out.println(line);
	}

	public static void print(Object line) {
		System.out.print(line);
	}

	public static void printFormatted(String line, Object... args) {
		System.out.format(line, args);
	}

	public static void printLineErr(Object line) {
		System.err.println(line);
	}

	public static void printErr(Object line) {
		System.err.print(line);
	}

	public static void printPacketBytes(byte[] bytes, int length) {
		// https://stackoverflow.com/a/15215434/6828099
		if (bytes.length < length)
			return;

		printPacketBytesHorizontal(bytes, length);
	}

	private static void printPacketBytesHorizontal(byte[] bytes, int length) {
		String positionHeader = Main.getTranslationBundle().getString("positions_header");
		String bytesHeader = Main.getTranslationBundle().getString("bytes_header");

		int positionHeaderFormatSize = (positionHeader.getBytes().length + 1);
		String headerSize = "| %-" + positionHeaderFormatSize + "s";
		String columnSize = "| %-3s";

		// Print Top Header
		print("+");
		for (int i = 0; i < ((length * 5) + positionHeaderFormatSize + 1); i++) {
			print("-");
		}
		printLine("+");

		// Print Position Header
		print(String.format(headerSize, positionHeader));

		// Print Positions
		for (int i = 0; i < length; i++) {
			print(String.format(columnSize, i));
		}
		printLine("|");

		// Print Byte Header
		print(String.format(headerSize, bytesHeader));

		// Print Bytes
		for (int i = 0; i < length; i++) {
			// https://www.thetopsites.net/article/50509537.shtml
			String hexValue = String.format("%02X", (0xFF & bytes[i]));
			print(String.format(columnSize, hexValue));
		}
		printLine("|");

		// Print Bottom Footer
		print("+");
		for (int i = 0; i < ((length * 5) + positionHeaderFormatSize + 1); i++) {
			print("-");
		}
		printLine("+");
	}

	@SuppressWarnings("unused")
	private static void printPacketBytesVertical(byte[] bytes, int length) {
		String leftAlignFormat = "| %-15s | %-4s |%n";

		printFormatted("+-----------------+------+%n");
		printFormatted("| Position        | Byte |%n");
		printFormatted("+-----------------+------+%n");
		for (int i = 0; i < length; i++) {
			// https://www.thetopsites.net/article/50509537.shtml
			String hexValue = String.format("%02X", (0xFF & bytes[i]));

			printFormatted(leftAlignFormat, i, hexValue);
		}
		printFormatted("+-----------------+------+%n");
	}
}
