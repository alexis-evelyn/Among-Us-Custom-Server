package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

public class Bug implements Command {
	private static final String issuesLink = "https://github.com/alexis-evelyn/Crewmate/issues/new";

	public void execute(String command, Terminal terminal) {
		String message = String.format(Main.getTranslationBundle().getString("bug_command_issue_message"), issuesLink);

		LogHelper.printLine(message);
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("bug_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslationBundle().getString("bug_command_help");
	}
}
