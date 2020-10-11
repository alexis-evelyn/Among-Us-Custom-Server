package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.Language;
import me.alexisevelyn.crewmate.handlers.GamePacketHandler;

import java.net.DatagramPacket;

public class CreateGame {
	public static byte[] handleCreateGame(DatagramPacket packet) {
		// 0000   01 00 02 2b 00 00 2a 02 0a 00 01 00 00 00 00 00   ...+..*.........
		// 0010   80 3f 00 00 40 3f 00 00 80 3f 00 00 f0 41 01 01   .?..@?...?...A..
		// 0020   03 01 00 00 00 02 00 00 00 00 00 87 00 00 00 00   ................
		// 0030   0f                                                .

		// 0000   01 00 12 05 00 01 00 00 00 00 07                  ...........

		byte[] buffer = packet.getData();

		// Data
		int maxPlayers = buffer[8];
		int map = buffer[13];
		int imposterCount = buffer[37];
		int language = Language.convertToInt(buffer[9], buffer[10]);

		String mapName = GamePacketHandler.getMapName(map);
		String languageName = GamePacketHandler.getLanguageName(language);

		String extraData = "Max Players: " + maxPlayers + "\n" +
				"Map: " + mapName + "\n" +
				"Imposters: " + imposterCount + "\n" +
				"Language: " + languageName;

		return PacketHelper.closeWithMessage(extraData);
	}
}
