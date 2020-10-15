package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;

import java.net.DatagramPacket;

public class Lobby {
	public static byte[] handleSettings(DatagramPacket packet) {
		// 0000   01 00 59 06 00 0a 3b be 25 8c 01 00               ..Y...;.%... - Private Game
		// 0000   01 00 42 06 00 0a 3b be 25 8c 01 01               ..B...;.%... - Public Game

		if (packet.getLength() != 12)
			return new byte[0];

		byte[] buffer = packet.getData();

		String visibility = (buffer[11] == 1) ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");

		// Used For Debugging
		LogHelper.printPacketBytes(buffer, packet.getLength());
		LogHelper.printLine(visibility);

		return new byte[0];
	}
}
