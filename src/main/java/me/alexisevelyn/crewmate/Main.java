package me.alexisevelyn.crewmate;

import java.net.SocketException;

public class Main {
	// TODO: Add a region file generator - Same Format As https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

	public static Server server;
	public static Terminal terminal;

	public static void main(String[] args) {
		// Start Server
		startServer();

		// Start Terminal For Local Commands
		startTerminal();
	}

	public static void startServer() {
		LogHelper.printLine("Starting Server!!!");

		try {
			server = new Server();
			server.start();
		} catch (SocketException e) {
			System.err.println("Failed To Bind To Socket!!!");
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
}