package me.alexisevelyn.crewmate;

import org.apiguardian.api.API;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Main Class That Starts {@link Server} and {@link Terminal}
 */
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

	/**
	 * Main Method
	 *
	 * @param args runtime arguments
	 */
	@API(status = API.Status.STABLE)
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

	/**
	 * Method to call to start the server
	 *
	 * @param config Configuration for starting the server
	 */
	@API(status = API.Status.STABLE)
	public static void startServer(Config config) {
		// Read Compile Time Properties
		// TODO: Turn Into Static HashMap Object
		String versionString;
		try (InputStream compileTimePropertiesStream = Main.class.getResourceAsStream("/compileTime.properties")) {
			compileTimeProperties.load(compileTimePropertiesStream);
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

	/**
	 * Method to call to start the terminal
	 *
	 * @param config Configuration for starting the terminal
	 */
	@API(status = API.Status.STABLE)
	public static void startTerminal(Config config) {
		terminal = new Terminal();
		terminal.start();
	}

	/**
	 * Method for setting up the config
	 *
	 * @param args runtime arguments
	 * @return instance of {@link Config} class
	 */
	@API(status = API.Status.MAINTAINED)
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

	/**
	 * Get instance of the server
	 *
	 * @return instance of server
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static Server getServer() {
		return server;
	}

	/**
	 * Get instance of the terminal
	 *
	 * @return instance of terminal
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static Terminal getTerminal() {
		return terminal;
	}

	/**
	 * Get instance of the main class
	 *
	 * @return instance of the main class
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static Thread getMain() {
		return main;
	}

	/**
	 * Get instance of the translation bundle
	 *
	 * @return instance of the translation bundle
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static ResourceBundle getTranslationBundle() {
		return translations;
	}

	/**
	 * Get instance of the current locale
	 *
	 * @return instance of the current locale
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static Locale getCurrentLocale() {
		return currentLocale;
	}

	/**
	 * Set instance of the current locale
	 *
	 * @param locale instance of locale
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static void setCurrentLocale(Locale locale) {
		currentLocale = locale;
	}

	/**
	 * Set instance of the current translation bundle
	 *
	 * @param translationBundle instance of the translation bundle
	 */
	@Deprecated
	@API(status = API.Status.DEPRECATED)
	public static void setTranslationBundle(ResourceBundle translationBundle) {
		translations = translationBundle;
	}
}