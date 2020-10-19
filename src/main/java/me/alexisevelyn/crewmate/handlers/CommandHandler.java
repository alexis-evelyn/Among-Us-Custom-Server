package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.commands.*;

public class CommandHandler {
	// TODO: Replace printLine with Terminal Specific (Implementation) Print Line
	// TODO: Replace Hardcode With Registry

	/**
	 * See {@link Help#Help()}
	 *
	 * @param command
	 * @param terminal
	 */
	public static void handleCommand(String command, Terminal terminal) {
		// Don't Bother With Command If Something's Amiss
		if (command == null || terminal == null)
			return;

		// Exit Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("exit_command"))) {
			new Exit().execute(command, terminal);
		}

		// Region File Generator Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("region_file_generator_command"))) {
			new RegionFileGenerator().execute(command, terminal);
		}

		// Help Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("help_command"))) {
			new Help().execute(command, terminal);
		}

		// Bug Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("bug_command"))) {
			new Bug().execute(command, terminal);
		}

		// Test Lua Command
		if (command.toLowerCase().equals(Main.getTranslationBundle().getString("test_lua_command"))) {
			new TestLua().execute(command, terminal);
		}
	}
}
