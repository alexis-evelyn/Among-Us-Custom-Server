package me.alexisevelyn.crewmate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.InetAddress;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Handles Configuration Options For Crewmate
 */
public class Config {
	private int serverPort = 22023;
	private InetAddress serverAddress = null; // Null Means 0.0.0.0 which means bind anywhere

	private int maxPlayers = 15000;

	// https://stackoverflow.com/a/17541055/6828099
	private final File jarLocation = new File(System.getProperty("java.class.path"));
	private File rootDir = new File(jarLocation.getParent(), "Crewmate");
	private String pluginsDir = "plugins";
	private String modsDir = "mods";

	private final ClassLoader mainClassLoader = Main.class.getClassLoader();

	private boolean useWordsList = false;

	// Words To Use (Overrides Blacklist)
	private URL[] whiteListWordsList = new URL[] {
			mainClassLoader.getResource("codes/words.txt"),
			mainClassLoader.getResource("codes/acronym.txt"),
			mainClassLoader.getResource("codes/names.txt"),
			mainClassLoader.getResource("codes/other.txt")
	};

	// Words To Not Used
	private URL[] blackListWordsList = new URL[] {
			mainClassLoader.getResource("codes/prof. anity.txt"),
			mainClassLoader.getResource("codes/ultrabanned.txt")
	};

	// Default Resource Bundle With Default Locale
	private ResourceBundle translations = ResourceBundle.getBundle("translations/Main", Locale.getDefault());

	/**
	 * Get's the desired server's port
	 *
	 * @return the desired server's port
	 */
	public int getServerPort() {
		return this.serverPort;
	}

	/**
	 * Set's the desired server's port
	 *
	 * @param port the desired server's port (0 if any)
	 */
	public void setServerPort(int port) {
		if (port < 0)
			port = 0;

		this.serverPort = port;
	}

	/**
	 * Get the desired server's bind address
	 *
	 * @return the desired server's bind address
	 */
	@Nullable
	public InetAddress getServerAddress() {
		return this.serverAddress;
	}

	/**
	 * Set the desired server's bind address
	 *
	 * @param serverAddress the desired server's bind address
	 */
	public void setServerAddress(@Nullable InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	/**
	 * Get's the desired server's root configuration folder (e.g. for hosting mods/plugins)
	 *
	 * @return the desired server's root configuration folder
	 */
	@NotNull
	public File getRootDir() {
		return this.rootDir;
	}

	/**
	 * Set's the desired server's root configuration folder (e.g. for hosting mods/plugins)
	 *
	 * @param rootDir the desired server's root configuration folder
	 */
	public void setRootDir(@NotNull File rootDir) {
		this.rootDir = rootDir;
	}

	/**
	 * Get's the maximum players allowed for this server's instance
	 *
	 * @return the maximum players allowed (0 if unlimited)
	 */
	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	/**
	 * Set's the maximum players allowed for this server's instance
	 *
	 * @param maxPlayers the maximum players allowed (0 if unlimited)
	 */
	public void setMaxPlayers(int maxPlayers) {
		if (maxPlayers < 0)
			maxPlayers = 0;

		this.maxPlayers = maxPlayers;
	}

	/**
	 * Get's the path to the plugin's directory (where the plugins are stored)
	 *
	 * @return path to plugin's directory
	 */
	@NotNull
	public File getPluginsDir() {
		return new File(this.rootDir, this.pluginsDir);
	}

	/**
	 * Set's the path to the plugin's directory (where the plugins are stored)
	 *
	 * @param pluginsDir path to plugin's directory (relative path to the root folder)
	 */
	public void setPluginsDir(@NotNull String pluginsDir) {
		this.pluginsDir = pluginsDir;
	}

	/**
	 * Get's the path to the mod's directory (where the mods are stored)
	 *
	 * @return path to mod's directory
	 */
	@NotNull
	public File getModsDir() {
		return new File(this.rootDir, this.modsDir);
	}

	/**
	 * Set's the path to the mod's directory (where the mods are stored)
	 *
	 * @param modsDir path to mod's directory (relative path to the root folder)
	 */
	public void setModsDir(@NotNull String modsDir) {
		this.modsDir = modsDir;
	}

	/**
	 * Get if using the words list or pure random code generation
	 *
	 * @return true if using the word's list, false if solely using pure random generation
	 */
	public boolean isUsingWordsList() {
		return this.useWordsList;
	}

	/**
	 * Set if using the words list or pure random code generation
	 *
	 * @param wordsList true if using the word's list, false if solely using pure random generation
	 */
	public void setUsingWordsList(boolean wordsList) {
		this.useWordsList = wordsList;
	}

	/**
	 * Get's the whitelisted word lists being used as a URL[].
	 *
	 * The whitelisted word list's list is cloned, so changes made to the list will not be reflected.
	 *
	 * This list overrides the blacklist.
	 *
	 * @return a clone of the whitelist word list's list
	 */
	@NotNull
	public URL[] getWhiteListWordsList() {
		return whiteListWordsList.clone();
	}

	/**
	 * Set's the whitelisted word lists being used as a URL[].
	 *
	 * The whitelisted word list's list is cloned, so changes made to the list will not be reflected.
	 *
	 * This list overrides the blacklist.
	 *
	 * @param whiteListWordsList a whitelist word list's list
	 */
	public void setWhiteListWordsList(@NotNull URL[] whiteListWordsList) {
		this.whiteListWordsList = whiteListWordsList;
	}

	/**
	 * Get's the blacklisted word lists being used as a URL[].
	 *
	 * The blacklisted word list's list is cloned, so changes made to the list will not be reflected.
	 *
	 * @return a clone of the blacklist word list's list
	 */
	@NotNull
	public URL[] getBlackListWordsList() {
		return blackListWordsList.clone();
	}

	/**
	 * Set's the blacklisted word lists being used as a URL[].
	 *
	 * The blacklisted word list's list is cloned, so changes made to the list will not be reflected.
	 *
	 * @param blackListWordsList a blacklist word list's list
	 */
	public void setBlackListWordsList(@NotNull URL[] blackListWordsList) {
		this.blackListWordsList = blackListWordsList;
	}

	/**
	 * Get's translation bundle for server instance
	 *
	 * @return translation bundle for server instance
	 */
	public ResourceBundle getTranslations() {
		return this.translations;
	}

	/**
	 * Set's translation bundle for server instance
	 *
	 * @param locale locale to use with translation bundle
	 */
	public void setTranslations(@NotNull Locale locale) {
		setTranslations("translations/Main", locale);
	}

	/**
	 * Set's translation bundle for server instance
	 *
	 * @param basename basename of translation file to use
	 * @param locale locale to use with translation bundle
	 */
	public void setTranslations(@NotNull String basename, @NotNull Locale locale) {
		this.translations = ResourceBundle.getBundle(basename, locale);
	}
}
