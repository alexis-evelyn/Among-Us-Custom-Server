package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.enums.TerminalColors;

import java.net.DatagramPacket;

public class PingHandler {
	public static byte[] handlePing(DatagramPacket packet) {
		byte[] buffer = packet.getData();

		if (packet.getLength() == 3 && buffer[0] == 0x0c) {
			System.out.println(TerminalColors.ANSI_TEXT_RED + "Received 0x0c Ping: " + String.format("0x%02X", buffer[1]).toLowerCase() + " " + String.format("0x%02X", buffer[2]).toLowerCase());
			return new byte[] {0x0c, buffer[1], buffer[2]};
		} else if (packet.getLength() == 4 && buffer[0] == 0x0a) {
			System.out.println(TerminalColors.ANSI_TEXT_GREEN + "Received 0x0a Ping: " + String.format("0x%02X", buffer[1]).toLowerCase() + " " + String.format("0x%02X", buffer[2]).toLowerCase());
			return new byte[] {0x0a, buffer[1], buffer[2], (byte) 0xff};
		}

		return new byte[0]; //this.getUnderConstructionMessage("player");
	}
}
