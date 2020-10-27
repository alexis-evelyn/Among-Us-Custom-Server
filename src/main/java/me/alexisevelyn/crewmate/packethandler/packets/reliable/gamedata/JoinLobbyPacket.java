package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinLobbyEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.DatagramPacket;

public class JoinLobbyPacket {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62
	// Reliable Packet Format - https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets

	// Reliable Packet Format - RP NO NO PL PL PT
	// RP = Reliable Packet Identifier (0x01)
	// NO = Nonce
	// PL = Packet Length (Starts After PT)
	// PT = Packet Type (What We Check In This File)

	public static byte[] handleJoinLobby(DatagramPacket packet, Server server) throws InvalidGameCodeException {
		PlayerJoinLobbyEvent event = new PlayerJoinLobbyEvent(packet.getAddress(), packet.getPort());
		event.call(server);

		if (!event.isCancelled())
			return StartGame.getClientGameCode(packet, server);
		else
			return ClosePacket.closeWithMessage(event.getReason());
	}
}
