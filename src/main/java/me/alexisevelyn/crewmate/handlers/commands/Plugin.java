package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ResourceBundle;

public class Plugin implements Command {
	public void execute(String command, Terminal terminal) {
		ResourceBundle translation = Main.getTranslationBundle();
		String[] arguments = command.trim().split("\\s+");

		// Needs Argument
		if (arguments.length < 2) {
			String wrongArgumentLengthMessage = translation.getString("plugin_command_invalid_argument_length");
			LogHelper.printLineErr(wrongArgumentLengthMessage);

			return;
		}

		// Second Argument - The Plugin Path
		File pluginPath = new File(arguments[1].trim());

		// Check Mime Type To Plugin
		try {
			String mimeType = Files.probeContentType(pluginPath.toPath());
			if (mimeType == null || !mimeType.equals("plugin/x-crewmate")) {
				LogHelper.printLine(translation.getString("plugin_command_fail"));
				return;
			}

			LogHelper.printLine(translation.getString("plugin_command_success"));
		} catch (IOException exception) {
			LogHelper.printErr(String.format(translation.getString("plugin_command_io_exception"), exception.getMessage()));
		}
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("plugin_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslationBundle().getString("plugin_command_help");
	}
}
