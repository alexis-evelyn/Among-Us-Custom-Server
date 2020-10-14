package me.alexisevelyn.crewmate;

// NOTE: This file is supposed to help with ensuring the terminal is always at the bottom and never left in place when the server logs information.

public class LogHelper {
	// https://unix.stackexchange.com/a/26592/181269
	private static String CLEAR_LINE_UP_TO_CURSOR = "\\033[1K";
	private static String CLEAR_WHOLE_LINE = "\\033[2K";

	public static void printLine(Object line) {
		System.out.println(line);
	}

	public static void print(Object line) {
		System.out.print(line);
	}
}
