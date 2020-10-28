package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.slf4j.Logger;

import java.io.IOException;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
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

	public static void main(String... args) {
		// TODO: Add Proper Argument Parser and Quit Passing Raw Args Array
		Config config = setUpConfig(args);

		// Store Main Thread
		main = Thread.currentThread();

		// Start Server
		startServer(config);

		// Start Terminal For Local Commands
		startTerminal(config);
	}

	public static void startServer(Config config) {
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

		try {
			server = new Server(config);
			server.start();
		} catch (SocketException e) {
			LogHelper.printLineErr(getTranslationBundle().getString("failed_socket_bind"));
		} catch (AccessDeniedException e) {
			LogHelper.printLineErr(e.getMessage());
		}
	}

	public static void startTerminal(Config config) {
		terminal = new Terminal();
		terminal.start();
	}

	private static Config setUpConfig(String... args) {
		// To Be Able To Modify Config
		// TODO: Add Ability To Specify Config Location Or Default To Server Jar Directory
		Config config = new Config();

		// TODO: Create an Argument Parsing Function
		// TODO: Determine If I Want To Parse --Some-- Config Options From Arguments
		try {
			if (args.length > 0) {
				config.setServerPort(Integer.parseInt(args[0]));
			}
		} catch (NumberFormatException ignored) {
			// Do Nothing!!!
			// ignored.printStackTrace();
		}

		return config;
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