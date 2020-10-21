package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.Terminal;

public interface Command {
	// So, commands should be split on which terminal types are allowed to get access to which commands.
	// That way ssh users can't change ssh settings and in game chat can perform server admin commands.

	void execute(String command, Terminal terminal);

	String getCommand();
	String getHelp();
}
