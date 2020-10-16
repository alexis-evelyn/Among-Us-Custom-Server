package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

public interface Command {
	static void execute(String command, Terminal terminal) {
		// TODO: Replace With Non-Static Method
		LogHelper.printLine(Main.getTranslationBundle().getString("command_not_implemented"));
	}
}
