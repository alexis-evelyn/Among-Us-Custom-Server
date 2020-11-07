package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.commands.*;

import java.util.HashMap;
import java.util.Map;

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

		String checkCommand = command.trim().split("\\s+")[0];

		// Exit Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("exit_command"))) {
			new Exit().execute(command, terminal);
		}

		// Region File Generator Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("region_file_generator_command"))) {
			new RegionFileGenerator().execute(command, terminal);
		}

		// Help Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("help_command"))) {
			new Help().execute(command, terminal);
		}

		// Bug Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("bug_command"))) {
			new Bug().execute(command, terminal);
		}

		// Lua Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("lua_command"))) {
			new Lua().execute(command, terminal);
		}

		// Game Code To Bytes Or String
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("gamecode_command"))) {
			new GameCode().execute(command, terminal);
		}

		// SSH Control Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("ssh_command"))) {
			new SSH().execute(command, terminal);
		}

		// Plugin Info Command
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("plugin_command"))) {
			new Plugin().execute(command, terminal);
		}

		// Packed Ints Tester
		if (checkCommand.equalsIgnoreCase(Main.getTranslation("packed_ints_command"))) {
			new PackedInts().execute(command, terminal);
		}

		for (Map.Entry<String, Command> commandEntry : commands.entrySet()) {
			if (checkCommand.equalsIgnoreCase(commandEntry.getKey()))
				commandEntry.getValue().execute(command, terminal);
		}
	}
}
