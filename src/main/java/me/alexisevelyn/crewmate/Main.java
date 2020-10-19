package me.alexisevelyn.crewmate;

import java.io.IOException;
import java.net.SocketException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

public class Main {
	// TODO: Add a region file generator - Same Format As https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

	private static Server server;
	private static Terminal terminal;
	private static Thread main;

	// This will be changeable via commands later
	private static Locale currentLocale = Locale.getDefault();
	private static ResourceBundle translations = ResourceBundle.getBundle("translations/Main", currentLocale);

	// Compile Time Properties
	private static final Properties compileTimeProperties = new Properties();

	public static void main(String[] args) {
		// Store Main Thread
		main = Thread.currentThread();

		// Start Server
		startServer(args);

		// Start Terminal For Local Commands
		startTerminal(args);
	}

	public static void startServer(String[] args) {
		// Read Compile Time Properties
		String versionString;
		try {
			compileTimeProperties.load(Main.class.getResourceAsStream("/compileTime.properties"));
			versionString = compileTimeProperties.getProperty("version");
		} catch (IOException exception) {
			versionString = getTranslationBundle().getString("unknown");
		}

		// Print Version Info
		String printableVersionString = String.format(getTranslationBundle().getString("server_version"), getTranslationBundle().getString("server_name"), versionString);
		LogHelper.printLine(printableVersionString);

		// Print Server Starting Message
		LogHelper.printLine(getTranslationBundle().getString("server_starting"));

		// TODO: Create an Argument Parsing Function
		// TODO: Figure out why client "ignores" different port number from 22023
		int portNumber = -1;
		try {
			if (args.length > 0) {
				portNumber = Integer.parseInt(args[0]);
			}
		} catch (NumberFormatException ignored) {
			// Do Nothing!!!
			// ignored.printStackTrace();
		}

		try {
			if (portNumber != -1)
				server = new Server(portNumber, null);
			else
				server = new Server();

			server.start();
		} catch (SocketException e) {
			LogHelper.printLineErr(getTranslationBundle().getString("failed_socket_bind"));
		}
	}

	public static void startTerminal(String[] args) {
		terminal = new Terminal();
		terminal.start();
	}

	public static Server getServer() {
		return server;
	}

	public static Terminal getTerminal() {
		return terminal;
	}

	public static Thread getMain() {
		return main;
	}

	public static ResourceBundle getTranslationBundle() {
		return translations;
	}

	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	public static void setCurrentLocale(Locale locale) {
		currentLocale = locale;
	}

	public static void setTranslationBundle(ResourceBundle translationBundle) {
		translations = translationBundle;
	}
}