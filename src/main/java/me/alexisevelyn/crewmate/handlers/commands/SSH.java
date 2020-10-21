package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Remote;
import me.alexisevelyn.crewmate.Terminal;

public class SSH implements Command {
	Remote server;

	public void execute(String command, Terminal terminal) {
		String message = Main.getTranslationBundle().getString("not_implemented");

		LogHelper.printLine(message);

		this.startSSHTest(command, terminal);
	}

	private void startSSHTest(String command, Terminal terminal) {
		server = new Remote();
		server.start();
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("ssh_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslationBundle().getString("ssh_command_help");
	}
}
