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

		// DecimalFormat - https://stackoverflow.com/a/3672738/6828099
		LogHelper.printLine(String.format("All Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getAllSentBytes(), statistics.getAllReceivedBytes(), statistics.getAllSentPackets(), statistics.getAllReceivedPackets()));
		LogHelper.printLine(String.format("Reliable Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getReliableSentBytes(), statistics.getReliableReceivedBytes(), statistics.getReliableSentPackets(), statistics.getReliableReceivedPackets()));
		LogHelper.printLine(String.format("Unreliable Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getUnreliableSentBytes(), statistics.getUnreliableReceivedBytes(), statistics.getUnreliableSentPackets(), statistics.getUnreliableReceivedPackets()));

		LogHelper.printLine(String.format("Hello Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getHelloSentBytes(), statistics.getHelloReceivedBytes(), statistics.getHelloSentPackets(), statistics.getHelloReceivedPackets()));
		LogHelper.printLine(String.format("Disconnect Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getDisconnectSentBytes(), statistics.getDisconnectReceivedBytes(), statistics.getDisconnectSentPackets(), statistics.getDisconnectReceivedPackets()));

		LogHelper.printLine(String.format("Ping Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getPingSentBytes(), statistics.getPingReceivedBytes(), statistics.getPingSentPackets(), statistics.getPingReceivedPackets()));
		LogHelper.printLine(String.format("Acknowledgment Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getAcknowledgementSentBytes(), statistics.getAcknowledgementReceivedBytes(), statistics.getAcknowledgementSentPackets(), statistics.getAcknowledgementReceivedPackets()));

		LogHelper.printLine(String.format("Fragment Sent/Received %,d/%,d bytes (%,d/%,d packets)", statistics.getFragmentedSentBytes(), statistics.getFragmentedReceivedBytes(), statistics.getFragmentedSentPackets(), statistics.getFragmentedReceivedPackets()));
	}
}
