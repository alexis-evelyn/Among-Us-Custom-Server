package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.Statistics;
import me.alexisevelyn.crewmate.enums.GamePacketType;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.*;
import org.jetbrains.annotations.NotNull;

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
	 * @param payloadBytes payload bytes
	 * @return data to send back to client
	 */
	@NotNull
	@SuppressWarnings("PMD.MissingBreakInSwitch")
	public static byte[] handleAmongUsPacket(@NotNull Server server, @NotNull Statistics statistics, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payloadBytes) {
		// Needs to Be At Least 6 Bytes Long To Be A Valid Reliable Packet
		if (payloadBytes.length < 3)
			return ClosePacket.closeWithMessage(Main.getTranslation("game_packet_invalid_size"));

		short payloadLength = ByteBuffer.wrap(new byte[] {payloadBytes[0], payloadBytes[1]}).order(ByteOrder.LITTLE_ENDIAN).getShort();

		if (payloadLength > (payloadBytes.length - 3))
			return ClosePacket.closeWithMessage(Main.getTranslation("game_packet_payload_length_too_long"));

		GamePacketType type = GamePacketType.getByte(payloadBytes[2]);

		// Sanitization Check
		if (type == null)
			return ClosePacket.closeWithMessage(Main.getTranslation("game_packet_unknown_type"));

		// TODO: Add support for looping through all the payloads
		byte[] packetData = PacketHelper.extractFirstPartBytes(payloadLength, PacketHelper.extractSecondPartBytes(3, payloadBytes));

		switch (type) {
			case HOST_SETTINGS: // 0x00
				return HostSettingsPacket.getNewGameSettings(server, clientAddress, clientPort, packetData); // 49 Bytes Total
			case JOIN_LOBBY: // 0x01
				return JoinLobbyPacket.handleJoinLobby(server, clientAddress, clientPort, packetData); // 11 Bytes Total
			case GAME_DATA: // 0x05 - TODO: Movement Falls Under Here
				return GameDataPacket.parseGameData(server, clientAddress, clientPort, packetData);
			case ALTER_GAME: // 0x0a
				return AlterGamePacket.alterGame(server, clientAddress, clientPort, packetData); // 12 Bytes Total
			case SEARCH_PUBLIC_GAME: // 0x10
				return SearchGamePacket.handleSearchPublicGame(server, clientAddress, clientPort, packetData); // 50 Bytes Total
				// return new byte[0];
			case START_GAME: // 0x02
				return new byte[0];
			case REMOVE_GAME: // 0x03
				// TODO: Get code from data and remove game after verifying game host of game.
				return new byte[0];
			case REMOVE_PLAYER: // 0x04
				// TODO: Check client packet
				PlayerManager.removePlayer(clientAddress, clientPort);
			case GAME_DATA_TO: // 0x06
				return new byte[0];
			case JOINED_GAME: // 0x07
				return new byte[0];
			case REDIRECT_GAME: // 0x0d
				return new byte[0];
			default:
				return ClosePacket.closeWithMessage(Main.getTranslation("game_packet_unknown_type"));
		}
	}
}
