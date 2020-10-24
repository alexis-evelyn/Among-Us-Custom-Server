package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.commands.*;

import java.util.HashMap;

public class CommandHandler {
	// TODO: Replace printLine with Terminal Specific (Implementation) Print Line
	// TODO: Replace Hardcode With Registry

	public static HashMap<String, Command> commands = new HashMap<>();

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

		String checkCommand = command.trim().toLowerCase().split("\\s+")[0];

		// Exit Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("exit_command"))) {
			new Exit().execute(command, terminal);
		}

		// Region File Generator Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("region_file_generator_command"))) {
			new RegionFileGenerator().execute(command, terminal);
		}

		// Help Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("help_command"))) {
			new Help().execute(command, terminal);
		}

		// Bug Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("bug_command"))) {
			new Bug().execute(command, terminal);
		}

		// Lua Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("lua_command"))) {
			new Lua().execute(command, terminal);
		}

		// Game Code To Bytes Or String
		if (checkCommand.equals(Main.getTranslationBundle().getString("gamecode_command"))) {
			new GameCode().execute(command, terminal);
		}

		// SSH Control Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("ssh_command"))) {
			new SSH().execute(command, terminal);
		}

		// Plugin Info Command
		if (checkCommand.equals(Main.getTranslationBundle().getString("plugin_command"))) {
			new Plugin().execute(command, terminal);
		}

		for (String commandName : commands.keySet()) {
			if (checkCommand.equalsIgnoreCase(commandName)) {
				commands.get(commandName).execute(command, terminal);
			}
		}
	}
}
