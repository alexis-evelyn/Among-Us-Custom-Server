package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

public class Exit implements Command {
	public static void execute(String command, Terminal terminal) {
		exit(terminal);
	}

	private static void exit(Terminal terminal) {
		LogHelper.printLine("");

		// Shutdown Server
		Main.getServer().exit();

		// Shutdown Command Handler
		terminal.exit();
	}
}
