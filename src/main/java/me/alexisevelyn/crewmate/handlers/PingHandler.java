package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.enums.hazel.SendOption;

import java.net.DatagramPacket;

public class PingHandler {
	public static byte[] handlePing(DatagramPacket packet) {
		// This is a hack. This will be changed to be a proper ping handler later when connection state storage is implemented.

		byte[] buffer = packet.getData();

		if (packet.getLength() == 3 && buffer[0] == SendOption.PING.getSendOption()) {
			// LogHelper.printLine(TerminalColors.ANSI_TEXT_RED + "Received Ping: " + String.format("0x%02X", buffer[1]).toLowerCase() + " " + String.format("0x%02X", buffer[2]).toLowerCase());

			// This should be sending SendOption.ACKNOWLEDGEMENT.getSendOption() and not PING. It's just setup with PING to keep the client happy.
			return new byte[] {SendOption.PING.getSendOption(), buffer[1], buffer[2]};
		} else if (packet.getLength() == 4 && buffer[0] == SendOption.ACKNOWLEDGEMENT.getSendOption()) {
			// LogHelper.printLine(TerminalColors.ANSI_TEXT_GREEN + "Received Acknowledgement For Ping: " + String.format("0x%02X", buffer[1]).toLowerCase() + " " + String.format("0x%02X", buffer[2]).toLowerCase());

			// This should not be responded to at all and should only be used for detecting if a connection is dead. The game and official server sends out 6 pings until it decides the connection is dead.
			return new byte[] {SendOption.ACKNOWLEDGEMENT.getSendOption(), buffer[1], buffer[2], (byte) 0xff};
		}

		return new byte[0]; //this.getUnderConstructionMessage("player");
	}
}
