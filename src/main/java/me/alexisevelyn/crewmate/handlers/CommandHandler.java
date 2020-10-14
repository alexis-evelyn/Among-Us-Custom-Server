package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

public class CommandHandler {
	public static void handleCommand(String command, Terminal terminal) {
		// Don't Bother With Command If Something's Amiss
		if (command == null || terminal == null)
			return;

		// Exit Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("exit_command"))) {
			exit(terminal);
		}
	}

	private static void exit(Terminal terminal) {
		LogHelper.printLine("");

		// Shutdown Server
		Main.getServer().exit();

		// Shutdown Command Handler
		terminal.exit();
	}
}
