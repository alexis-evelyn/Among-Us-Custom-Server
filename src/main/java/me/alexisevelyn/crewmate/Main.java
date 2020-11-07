package me.alexisevelyn.crewmate;

import org.apiguardian.api.API;

import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.nio.file.AccessDeniedException;
import java.util.Properties;
import java.util.ResourceBundle;

/**
 * Main Class That Starts {@link Server} and {@link Terminal}
 */
public class Main {
	// Compile Time Properties
	private static final Properties compileTimeProperties = new Properties();
	private static Config config;
	private static Server server;

	/**
	 * Main Method
	 *
	 * @param args runtime arguments
	 */
	@API(status = API.Status.STABLE)
	public static void main(String... args) {
		// TODO: Add Proper Argument Parser and Quit Passing Raw Args Array
		config = setUpConfig(args);

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
			versionString = config.getTranslations().getString("unknown");
		}

		// Print Version Info
		String printableVersionString = String.format(config.getTranslations().getString("server_version"), config.getTranslations().getString("server_name"), versionString);
		LogHelper.printLine(printableVersionString);

		// Print Server Starting Message
		LogHelper.printLine(config.getTranslations().getString("server_starting"));

		try {
			// TODO: Decide If I Want To Pass Config Into Server
			server = new Server(config);
			server.start();
		} catch (SocketException e) {
			LogHelper.printLineErr(config.getTranslations().getString("failed_socket_bind"));
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
		// TODO: Decide If I Want To Pass Config Into Terminal and Server Into Terminal
		Terminal terminal = new Terminal();
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

	@Deprecated
	public static Server getServer() {
		return server;
	}

	@Deprecated
	public static ResourceBundle getTranslationBundle() {
		return config.getTranslations();
	}
}