package me.alexisevelyn.crewmate;

import java.net.SocketException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main {
	// TODO: Add a region file generator - Same Format As https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

	private static Server server;
	private static Terminal terminal;

	// This will be changeable via commands later
	private static Locale currentLocale = Locale.getDefault();
	private static ResourceBundle translations = ResourceBundle.getBundle("translations/Main", currentLocale);

	public static void main(String[] args) {
		// Start Server
		startServer();

		// Start Terminal For Local Commands
		startTerminal();
	}

	public static void startServer() {
		LogHelper.printLine(getTranslationBundle().getString("server_starting"));

		try {
			server = new Server();
			server.start();
		} catch (SocketException e) {
			LogHelper.printLineErr(getTranslationBundle().getString("failed_socket_bind"));
		}
	}

	public static void startTerminal() {
		terminal = new Terminal();
		terminal.start();
	}

	public static Server getServer() {
		return server;
	}

	public static Terminal getTerminal() {
		return terminal;
	}

	public static ResourceBundle getTranslationBundle() {
		return translations;
	}

	public static Locale getCurrentLocale() {
		return currentLocale;
	}
}