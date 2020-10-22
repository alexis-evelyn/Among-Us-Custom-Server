package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.CommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class Help implements Command {
	private final ArrayList<Class <? extends Command>> commands = new ArrayList<>();

	/**
	 * See {@link CommandHandler#handleCommand)
	 */
	public Help() {
		commands.add(Exit.class);
		commands.add(RegionFileGenerator.class);
		commands.add(Help.class);
		commands.add(Bug.class);
		commands.add(Lua.class);
		commands.add(GameCode.class);
		commands.add(SSH.class);
		commands.add(Plugin.class);

		// TODO: Sort List and Pull From Registry
	}

	public void execute(String command, Terminal terminal) {
		ResourceBundle translation = Main.getTranslationBundle();
		String helpFormat = translation.getString("help_command_format");

		try {
			for (Class<? extends Command> commandHandler : commands) {
				Command handler = commandHandler.getDeclaredConstructor().newInstance();

				LogHelper.printLine(String.format(helpFormat, handler.getCommand(), handler.getHelp()));
			}
		} catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException exception) {
			LogHelper.printLineErr(exception.getMessage());
			exception.printStackTrace(); // TODO: Replace With LogHelper StackTrace Print
		}
	}

	@Override
	public String getCommand() {
		return Main.getTranslationBundle().getString("help_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslationBundle().getString("help_command_help");
	}
}
