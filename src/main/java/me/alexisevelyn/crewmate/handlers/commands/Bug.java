package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;

import java.io.IOException;
import java.util.Properties;

public class Bug implements Command {
	// Compile Time Properties
	private static final Properties compileTimeProperties = new Properties();

	private final String issuesLink;

	public Bug() {
		String tempLink;

		try {
			compileTimeProperties.load(Main.class.getResourceAsStream("/compileTime.properties"));

			tempLink = compileTimeProperties.getProperty("issue_link");
		} catch (IOException exception) {
			tempLink = Main.getTranslationBundle().getString("unknown");
		}

		// Assign Issue Link Or If IOException Unknown
		issuesLink = tempLink;
	}

	public void execute(String command, Terminal terminal) {
		String message = String.format(Main.getTranslationBundle().getString("bug_command_issue_message"), issuesLink);

		Terminal.getLogger().info(message);
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
