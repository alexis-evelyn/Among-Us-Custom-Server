package me.alexisevelyn.crewmate.handlers.commands;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Statistics;
import me.alexisevelyn.crewmate.Terminal;
import org.jetbrains.annotations.NotNull;

public class StatisticsCommand implements Command {
	public void execute(String command, Terminal terminal) {
		Statistics statistics = Main.getServer().getStatistics();

        if (statistics == null) {
        	LogHelper.printLine(Main.getTranslation("statistics_null"));
        	return;
        }

        printStatistics(statistics);
	}

	@Override
	public String getCommand() {
		return Main.getTranslation("statistics_command");
	}

	@Override
	public String getHelp() {
		return Main.getTranslation("statistics_command_help");
	}

	private static void printStatistics(@NotNull Statistics statistics) {
		LogHelper.printLine("Not Translated Yet!!!");


		LogHelper.printLine(String.format("All Sent/Received %s/%s", statistics.getAllSent(), statistics.getAllReceived()));
		LogHelper.printLine(String.format("Reliable Sent/Received %s/%s", statistics.getReliableSent(), statistics.getReliableReceived()));
		LogHelper.printLine(String.format("Unreliable Sent/Received %s/%s", statistics.getUnreliableSent(), statistics.getUnreliableReceived()));

		LogHelper.printLine(String.format("Hello Sent/Received %s/%s", statistics.getHelloSent(), statistics.getHelloReceived()));
		LogHelper.printLine(String.format("Disconnect Sent/Received %s/%s", statistics.getDisconnectSent(), statistics.getDisconnectReceived()));

		LogHelper.printLine(String.format("Ping Sent/Received %s/%s", statistics.getPingSent(), statistics.getPingReceived()));
		LogHelper.printLine(String.format("Acknowledgment Sent/Received %s/%s", statistics.getAcknowledgementSent(), statistics.getAcknowledgementReceived()));

		LogHelper.printLine(String.format("Fragment Sent/Received %s/%s", statistics.getFragmentedSent(), statistics.getFragmentedReceived()));
	}
}
