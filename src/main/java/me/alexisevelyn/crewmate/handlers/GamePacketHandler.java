package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.ReliablePacketType;
import me.alexisevelyn.crewmate.handlers.gamepacket.Lobby;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;

import java.net.DatagramPacket;

public class GamePacketHandler {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62
	// Reliable Packet Format - https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets

	// Reliable Packet Format - RP NO NO PL PL PT
	// RP = Reliable Packet Identifier (0x01)
	// NO = Nonce
	// PL = Packet Length (Starts After PT)
	// PT = Packet Type (What We Check In This File)

	public static byte[] handleReliablePacket(DatagramPacket packet) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// Needs to Be At Least 6 Bytes Long To Be A Valid Reliable Packet
		if (length < 6)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("reliable_packet_invalid_size"));

		ReliablePacketType type = ReliablePacketType.getReliablePacketType(buffer[5]);

		switch (type) {
			case PRE_HOST_SETTINGS: // 0x00
				return StartGame.getNewGameSettings(packet); // 49 Bytes Total
			case JOIN_GAME: // 0x01
				return StartGame.getClientGameCode(packet); // 11 Bytes Total
			case GAME_DATA: // 0x05
				return Lobby.handleCosmetics(packet); // 16 Bytes Total
			// return StartGame.getInitialGameSettings(packet); // At Least 173 Bytes?
			case ALTER_GAME: // 0x0a
				return Lobby.handleGameVisibility(packet); // 12 Bytes Total
			case SEARCH_PUBLIC_GAME: // 0x10
				return SearchGame.handleSearchPublicGame(packet); // 50 Bytes Total
			case START_GAME: // 0x02
			case REMOVE_GAME: // 0x03
			case REMOVE_PLAYER: // 0x04
			case GAME_DATA_TO: // 0x06
			case JOINED_GAME: // 0x07
			case REDIRECT_GAME: // 0x0d
			default:
				return new byte[0];
		}
	}

	public static byte[] handleUnreliablePacket(DatagramPacket packet) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// LogHelper.printLine(Main.getTranslationBundle().getString("unreliable_packet"));
		// LogHelper.printPacketBytes(buffer, length);

		return new byte[0];
	}
}
