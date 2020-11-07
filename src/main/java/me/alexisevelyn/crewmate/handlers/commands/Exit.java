package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

public class Exit implements Command {
	public void execute(String command, Terminal terminal) {
		exit(terminal);
	}

	@Override
	public String getCommand() {
		return Main.getTranslation("exit_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslation("exit_command_help");
	}

	private static void exit(Terminal terminal) {
		LogHelper.printLine();

		// Shutdown Server
		Main.getServer().exit();

		// Shutdown Command Handler
		terminal.exit();
	}
}
