package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.commands.Exit;
import me.alexisevelyn.crewmate.handlers.commands.RegionFileGenerator;

public class CommandHandler {
	// TODO: Replace printLine with Terminal Specific (Implementation) Print Line

	public static void handleCommand(String command, Terminal terminal) {
		// Don't Bother With Command If Something's Amiss
		if (command == null || terminal == null)
			return;

		// Exit Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("exit_command"))) {
			Exit.execute(command, terminal);
		}

		// Region File Generator Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("region_file_generator_command"))) {
			RegionFileGenerator.execute(command, terminal);
		}
	}
}
