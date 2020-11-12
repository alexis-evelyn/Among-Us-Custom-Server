package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Terminal;
import me.alexisevelyn.crewmate.handlers.CommandHandler;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

public class Help implements Command {
	private final ArrayList<Class <? extends Command>> commands = new ArrayList<>();

	/**
	 * See {@link CommandHandler#handleCommand(String, Terminal)}
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
		commands.add(PackedInts.class);
		commands.add(StatisticsCommand.class);

		// TODO: Sort List and Pull From Registry
	}

	public void execute(String command, Terminal terminal) {
		String helpFormat = Main.getTranslation("help_command_format");

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
		return Main.getTranslation("help_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslation("help_command_help");
	}
}
