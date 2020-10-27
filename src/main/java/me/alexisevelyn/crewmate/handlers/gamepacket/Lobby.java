package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.*;
import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.impl.*;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.DatagramPacket;
import java.util.ResourceBundle;

public class Lobby {
	public static byte[] alterGame(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		if (length < 6)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_invalid_size"));

		AlterGame alterGameFlag = AlterGame.getAlterGameFlag(buffer[5]);

		if (alterGameFlag == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));

		switch (alterGameFlag) {
			case CHANGE_PRIVACY:
				return handleGameVisibility(packet, server);
			default:
				return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));
		}
	}

	private static byte[] handleGameVisibility(DatagramPacket packet, Server server) {
		// 0000   01 00 59 06 00 0a 3b be 25 8c 01 00               ..Y...;.%... - Private Game
		// 0000   01 00 42 06 00 0a 3b be 25 8c 01 01               ..B...;.%... - Public Game

		if (packet.getLength() != 12)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_invalid_size"));

		byte[] buffer = packet.getData();

		String visibility = (buffer[11] == 1) ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");

		ChangeVisibilityEvent event = new ChangeVisibilityEvent(server, PlayerManager.getPlayerByAddress(packet.getAddress(), packet.getPort()), visibility.equals(Main.getTranslationBundle().getString("public_game")));
		event.call(server);

		if (!event.isCancelled())
			visibility = event.isVisible() ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");

		// Used For Debugging
		LogHelper.printPacketBytes(packet.getLength(), buffer);
		LogHelper.printLine(visibility);

		return new byte[0];
	}
}
