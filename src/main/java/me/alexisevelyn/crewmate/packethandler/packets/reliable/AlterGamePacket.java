package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.AlterGame;
import me.alexisevelyn.crewmate.events.impl.ChangeVisibilityEvent;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.InetAddress;

public class AlterGamePacket {
	public static byte[] alterGame(Server server, InetAddress clientAddress, int clientPort, byte... payloadBytes) {
		// 00 01 02 03 04 05
		// -----------------
		// 5a 5c ff 89 01 01
		// -----------------
		// GC GC GC GC PT PD
		// GC = GameCode
		// PT = Packet Type (0x01 For Change Privacy)
		// PD = Payload Data

		// Should Change to 5?
    	if (payloadBytes.length < 6)
    		return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_invalid_size"));

        AlterGame alterGameFlag = AlterGame.getAlterGameFlag(payloadBytes[4]);

        if (alterGameFlag == null)
            return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));

        switch (alterGameFlag) {
            case CHANGE_PRIVACY:
                return handleGameVisibility(server, clientAddress, clientPort, payloadBytes[5]);
            default:
                return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));
        }
	}

	private static byte[] handleGameVisibility(Server server, InetAddress clientAddress, int clientPort, byte visibility) {
		// 0000   01 00 59 06 00 0a 3b be 25 8c 01 00               ..Y...;.%... - Private Game
		// 0000   01 00 42 06 00 0a 3b be 25 8c 01 01               ..B...;.%... - Public Game

		boolean isPublic = (visibility == 0x01);

		ChangeVisibilityEvent event = new ChangeVisibilityEvent(server, PlayerManager.getPlayerByAddress(clientAddress, clientPort), isPublic);
		event.call(server);

		if (!event.isCancelled()) {
			String visibilityString = event.isVisible() ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");
			LogHelper.printLine(visibilityString);
		}

		return new byte[0];
	}
}
