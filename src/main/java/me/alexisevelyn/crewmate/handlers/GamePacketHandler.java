package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.handlers.gamepacket.Lobby;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;

import java.net.DatagramPacket;

public class GamePacketHandler {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62

	public static byte[] handleGamePacket(DatagramPacket packet) {
		// Validate Packet Size
		// TODO: Figure Out Minimum Length
		// TODO: Figure Out Proper Way To Identify Game Types
		if (packet.getLength() == 11)
			return StartGame.getClientGameCode(packet);
		if (packet.getLength() == 12)
			return Lobby.handleSettings(packet);
		if (packet.getLength() == 49)
			return StartGame.getNewGameSettings(packet);
		if (packet.getLength() == 50)
			return SearchGame.handleSearchPublicGame(packet);
		if (packet.getLength() >= 173) // We don't grab the bytes here as the packet apparently gets cleared after being read.
			return StartGame.getInitialGameSettings(packet);

		return new byte[0];
	}
}
