package me.alexisevelyn.crewmate;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.net.InetAddress;

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

	public int getServerPort() {
		return this.serverPort;
	}

	public void setServerPort(int port) {
		this.serverPort = port;
	}

	@Nullable
	public InetAddress getServerAddress() {
		return this.serverAddress;
	}

	public void setServerAddress(@Nullable InetAddress serverAddress) {
		this.serverAddress = serverAddress;
	}

	@NotNull
	public File getRootDir() {
		return this.rootDir;
	}

	public void setRootDir(@NotNull File rootDir) {
		this.rootDir = rootDir;
	}

	public int getMaxPlayers() {
		return this.maxPlayers;
	}

	public void setMaxPlayers(int maxPlayers) {
		this.maxPlayers = maxPlayers;
	}

	@NotNull
	public File getPluginsDir() {
		return new File(this.rootDir, this.pluginsDir);
	}

	public void setPluginsDir(@NotNull String pluginsDir) {
		this.pluginsDir = pluginsDir;
	}

	@NotNull
	public File getModsDir() {
		return new File(this.rootDir, this.modsDir);
	}

	public void setModsDir(@NotNull String modsDir) {
		this.modsDir = modsDir;
	}
}
