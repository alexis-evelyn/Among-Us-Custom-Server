package me.alexisevelyn.crewmate;

// NOTE: This file is supposed to help with ensuring the terminal is always at the bottom and never left in place when the server logs information.

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.ZonedDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LogHelper {
	// Terminal Logger
	private static final Logger logger = LoggerFactory.getLogger(Server.class);

	// https://unix.stackexchange.com/a/26592/181269
	private static String CLEAR_LINE_UP_TO_CURSOR = "\\033[1K";
	private static String CLEAR_WHOLE_LINE = "\\033[2K";

	private static boolean isTerminalThread = false;
	private static boolean showTimestamp = false;
	private static boolean twentyFourHour = true;
	private static boolean simpleDateTime = true;

	public static void printLine() {
		System.out.println();
	}

	public static void printLine(Object line) {
		setCurrentThread();

		if (showTimestamp && !isTerminalThread)
			System.out.println(createLogTimestamp(ZonedDateTime.now(), twentyFourHour, simpleDateTime) + line);
		else {
			// System.out.println(line);
			// https://www.baeldung.com/logback
			// logger.debug("{}", line);
		}
	}

	public static void print(Object line) {
		setCurrentThread();

		System.out.print(line);
	}

	public static void printFormatted(String line, Object... args) {
		setCurrentThread();

		System.out.format(line, args);
	}

	public static void printLineErr(Object line) {
		setCurrentThread();

		System.err.println(line);
	}

	public static void printErr(Object line) {
		setCurrentThread();

		System.err.print(line);
	}

	public static void printPacketBytes(byte[] bytes, int length) {
		// https://stackoverflow.com/a/15215434/6828099
		if (bytes.length < length)
			return;

		setCurrentThread();
		printPacketBytesHorizontal(bytes, length);
	}

	public static void printPacketBytesHorizontal(byte[] bytes, int length) {
		// https://stackoverflow.com/a/15215434/6828099
		if (bytes.length < length)
			return;

		setCurrentThread();

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
	public static void printPacketBytesVertical(byte[] bytes, int length) {
		// https://stackoverflow.com/a/15215434/6828099
		if (bytes.length < length)
			return;

		setCurrentThread();

		String leftAlignFormat = "| %-15s | %-5s |%n";

		printFormatted("+-----------------+-------+%n");
		printFormatted(leftAlignFormat, Main.getTranslationBundle().getString("positions_header"), Main.getTranslationBundle().getString("bytes_header"));
		printFormatted("+-----------------+-------+%n");
		for (int i = 0; i < length; i++) {
			// https://www.thetopsites.net/article/50509537.shtml
			String hexValue = String.format("%02X", (0xFF & bytes[i]));

			printFormatted(leftAlignFormat, i, hexValue);
		}
		printFormatted("+-----------------+-------+%n");
	}

	public static String convertByteToHexString(byte bite) {
		return String.format("0x%02X", (0xFF & bite));
	}

	private static boolean wasLastThreadTerminalThread() {
		return isTerminalThread;
	}

	private static void setCurrentThread() {
		// This should help with tracking lines in terminal and whether or not to overwrite them.

		isTerminalThread = Thread.currentThread().equals(Main.getTerminal());
	}

	private static void setShowTimestamp(boolean shouldShowTimestamp) {
		showTimestamp = shouldShowTimestamp;
	}

	private static boolean shouldShowTimestamp() {
		return showTimestamp;
	}

	private static String createLogTimestamp(ZonedDateTime dateTime, boolean twentyFourHour, boolean simpleDateTime) {
		ResourceBundle translation = Main.getTranslationBundle();

		// For Debugging
		// dateTime = dateTime.minusHours(3);

		// ${month}/${day}/${year} ${hour}:${minute}:${second} ${meridiem} ${timezone}
		int month = dateTime.getMonthValue();
		int day = dateTime.getDayOfMonth();
		int year = dateTime.getYear();

		int hour = dateTime.getHour();
		int minute = dateTime.getMinute();
		int second = dateTime.getSecond();
		int nanosecond = dateTime.getNano();

		String meridiem = "";
		String timezone = dateTime.getZone().toString();

		String untranslatedDateTime;
		if (twentyFourHour) {
			if (simpleDateTime)
				untranslatedDateTime = translation.getString("log_datetime_format_24_hour_simple");
			else
				untranslatedDateTime = translation.getString("log_datetime_format_24_hour");
		}
		else {
			if (simpleDateTime)
				untranslatedDateTime = translation.getString("log_datetime_format_12_hour_simple");
			else
				untranslatedDateTime = translation.getString("log_datetime_format_12_hour");

			if (hour > 12) {
				hour -= 12;
				meridiem = translation.getString("log_datetime_post_meridiem");
			} else {
				meridiem = translation.getString("log_datetime_pre_meridiem");
			}

			if (hour == 0)
				hour = 12;
		}

		Map<String, Object> parameters = new HashMap<>();
		// Date
		parameters.put("month", String.format("%02d", month));
		parameters.put("day", String.format("%02d", day));
		parameters.put("year", String.format("%02d", year));

		// Time
		parameters.put("hour", String.format("%02d", hour));
		parameters.put("minute", String.format("%02d", minute));
		parameters.put("second", String.format("%02d", second));
		parameters.put("nanosecond", nanosecond);

		// Other
		parameters.put("meridiem", meridiem);
		parameters.put("timezone", timezone);

		return formatNamed(untranslatedDateTime, parameters);
	}

	public static String formatNamed(String format, Map<String, Object> values) {
		// https://stackoverflow.com/a/27815924/6828099
		StringBuilder formatter = new StringBuilder(format);
		List<Object> valueList = new ArrayList<>();

		Matcher matcher = Pattern.compile("\\$\\{(\\w+)}").matcher(format);

		while (matcher.find()) {
			String key = matcher.group(1);

			String formatKey = String.format("${%s}", key);
			int index = formatter.indexOf(formatKey);

			if (index != -1) {
				formatter.replace(index, index + formatKey.length(), "%s");
				valueList.add(values.get(key));
			}
		}

		return String.format(formatter.toString(), valueList.toArray());
	}

	private static void printLogbackInternal() {
		// https://logback.qos.ch/manual/configuration.html
		// assume SLF4J is bound to logback in the current environment
		LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();

		// print logback's internal status
		StatusPrinter.print(lc);
	}
}
