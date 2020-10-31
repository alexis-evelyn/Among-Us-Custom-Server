package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.GamePacketType;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.JoinLobbyPacket;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GamePacket {
	/**
	 * Determines type of Reliable Packet and passes it off to responsible handler
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param byteLength count of payload bytes
	 * @param payloadBytes payload bytes
	 * @return data to send back to client
	 */
	public static byte[] handleAmongUsPacket(Server server, InetAddress clientAddress, int clientPort, int byteLength, byte... payloadBytes) {
		// Needs to Be At Least 6 Bytes Long To Be A Valid Reliable Packet
		if (byteLength < 3)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("game_packet_invalid_size"));

		short payloadLength = ByteBuffer.wrap(new byte[] {payloadBytes[0], payloadBytes[1]}).order(ByteOrder.LITTLE_ENDIAN).getShort();

		if (payloadLength > (byteLength - 3))
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("game_packet_payload_length_too_long"));

		GamePacketType type = GamePacketType.getByte(payloadBytes[2]);

		// Sanitization Check
		if (type == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("game_packet_unknown_type"));

		// TODO: Add support for looping through all the payloads
		byte[] packetData = PacketHelper.extractFirstPartBytes(payloadLength, PacketHelper.extractSecondPartBytes(3, payloadBytes));

		switch (type) {
			case HOST_SETTINGS: // 0x00
				return StartGame.getNewGameSettings(server, clientAddress, clientPort, payloadLength, packetData); // 49 Bytes Total
			case JOIN_LOBBY: // 0x01
				return JoinLobbyPacket.handleJoinLobby(server, clientAddress, clientPort, packetData); // 11 Bytes Total
			case GAME_DATA: // 0x05 - TODO: Movement Falls Under Here
//				LogHelper.printPacketBytes(payloadLength, packetData);
//				return GameDataPacket.parseGameData(server, clientAddress, clientPort, payloadLength, packetData);
				// LogHelper.printPacketBytes(packetData.length, packetData);

				return new byte[0];
			case ALTER_GAME: // 0x0a
				// return Lobby.alterGame(server, clientAddress, clientPort, payloadLength, packetData); // 12 Bytes Total
				return new byte[0];
			case SEARCH_PUBLIC_GAME: // 0x10
				// return SearchGame.handleSearchPublicGame(server, clientAddress, clientPort, payloadLength, packetData); // 50 Bytes Total
				return new byte[0];
			case START_GAME: // 0x02
			case REMOVE_GAME: // 0x03
				// TODO: Get code from data and remove game after verifying game host of game.
			case REMOVE_PLAYER: // 0x04
				// TODO: Check client packet
				PlayerManager.removePlayer(clientAddress, clientPort);
			case GAME_DATA_TO: // 0x06
			case JOINED_GAME: // 0x07
			case REDIRECT_GAME: // 0x0d
			default:
				return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("game_packet_unknown_type"));
		}
	}
}
