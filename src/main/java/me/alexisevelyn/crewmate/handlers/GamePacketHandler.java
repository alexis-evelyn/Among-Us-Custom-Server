package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.RPC;
import me.alexisevelyn.crewmate.enums.ReliablePacketType;
import me.alexisevelyn.crewmate.events.impl.PlayerChatEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerPreJoinEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.gamepacket.Lobby;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.StandardCharsets;

public class GamePacketHandler {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62
	// Reliable Packet Format - https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets

	// Reliable Packet Format - RP NO NO PL PL PT
	// RP = Reliable Packet Identifier (0x01)
	// NO = Nonce
	// PL = Packet Length (Starts After PT)
	// PT = Packet Type (What We Check In This File)

	public static byte[] handleReliablePacket(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// Needs to Be At Least 6 Bytes Long To Be A Valid Reliable Packet
		if (length < 6)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("reliable_packet_invalid_size"));

		ReliablePacketType type = ReliablePacketType.getReliablePacketType(buffer[5]);

		// Sanitization Check
		if (type == null)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("reliable_packet_unknown_type"));

		try {
			switch (type) {
				case PRE_HOST_SETTINGS: // 0x00
					return StartGame.getNewGameSettings(packet, server); // 49 Bytes Total
				case JOIN_GAME: // 0x01
					return handlePreJoinGame(packet, server); // 11 Bytes Total
				case GAME_DATA: // 0x05
					return parseGameData(packet, server);
				case ALTER_GAME: // 0x0a
					return Lobby.alterGame(packet, server); // 12 Bytes Total
				case SEARCH_PUBLIC_GAME: // 0x10
					return SearchGame.handleSearchPublicGame(packet, server); // 50 Bytes Total
				case START_GAME: // 0x02
				case REMOVE_GAME: // 0x03
					// TODO: Get code from data and remove game.
				case REMOVE_PLAYER: // 0x04
					PlayerManager.removePlayer(packet.getAddress(), packet.getPort());
				case GAME_DATA_TO: // 0x06
				case JOINED_GAME: // 0x07
				case REDIRECT_GAME: // 0x0d
				default:
					return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("reliable_packet_unknown_type"));
			}
		} catch (Exception e) {
			e.printStackTrace();

			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("server_side_exception"));
		}
	}

	private static byte[] handlePreJoinGame(DatagramPacket packet, Server server) throws InvalidGameCodeException {
		PlayerPreJoinEvent event = new PlayerPreJoinEvent(packet.getAddress(), packet.getPort());
		event.call(server);
		if (!event.isCancelled()) {
			return StartGame.getClientGameCode(packet, server);
		} else {
			return PacketHelper.closeWithMessage(event.getReason());
		}
	}

	public static byte[] handleUnreliablePacket(DatagramPacket packet) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// LogHelper.printLine(Main.getTranslationBundle().getString("unreliable_packet"));
		// LogHelper.printPacketBytes(buffer, length);

		return new byte[0];
	}

	private static byte[] parseGameData(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// TODO: Create a Game Data Enum and Put 0x02 As The RPC Game Data Type
		if (length >= 15 && buffer[12] == 0x02) {
			RPC type = RPC.getRPC(buffer[14]);

			// Sanitization Check
			if (type == null) {
				LogHelper.printLine(String.format(Main.getTranslationBundle().getString("unknown_rpc_flag"), LogHelper.convertByteToHexString(buffer[14])));
				// LogHelper.printPacketBytes(buffer, length);

				// Setup For Extract RPC Header
				byte[] rpcBytes = new byte[15];
				byte[] rpcSpecifiedSizeBytes = new byte[2];

				// Extract RPC Header
				System.arraycopy(buffer, 0, rpcBytes, 0, 15);
				System.arraycopy(rpcBytes, 3, rpcSpecifiedSizeBytes, 0, 2);

				// Size Math
				int rpcSpecifiedLength = ByteBuffer.wrap(rpcSpecifiedSizeBytes).order(ByteOrder.LITTLE_ENDIAN).getShort();
				int packetLength = length - 6;

				// Print Size Math
				LogHelper.printLine(String.format(Main.getTranslationBundle().getString("rpc_length_vs_packet_length"), rpcSpecifiedLength, packetLength));

				// Print RPC Header
				LogHelper.printPacketBytes(rpcBytes, 15);

				return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}

			switch (type) {
				case SEND_CHAT:
					return handleChat(packet, server);
				case SET_COLOR:
				case SET_HAT:
				case SET_SKIN:
				case SET_PET:
					return Lobby.handleCosmetics(packet, server); // 16 Bytes Total
				case SYNC_SETTINGS: // Double Check
					return StartGame.getLobbyGameSettings(packet); // At Least 173 Bytes?
				default:
					return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}
		}

		return new byte[0];
	}

	private static byte[] handleChat(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// Game Code - ABCDEF (3b:be:25:8c)

		// Chat C->S - hello - (0f 00 = 15)
		// 0000   01 00 11 0f 00 05 3b be 25 8c 08 00 02 04 0d 05   ......;.%.......
		// 0010   68 65 6c 6c 6f                                    hello

		// Chat C->S - hi - (0c 00 = 12)
		// 0000   01 01 10 0c 00 05 3b be 25 8c 05 00 02 04 0d 02   ......;.%.......
		// 0010   68 69                                             hi

		// Chat C->S - hello - (0f 00 = 15)
		// 0000   01 00 0d 0f 00 05 41 4c 45 58 08 00 02 04 0d 05   ......ALEX......
		// 0010   68 65 6c 6c 6f                                    hello

		// Chat C->S Format
		// 0000   RP NO NO PL PL PT GC GC GC GC ML ML MF UK RF TL
		// 0010   TX TX ...
		//
		// RP = Reliable Packet (0x01)
		// NO = Nonce (Repeat To Client To Confirm Received Packet)
		// PL = Packet Length (Starts After PT)
		// PT = Packet Type (GAME_DATA - 0x05)
		// GC = Game Code (Room Code)
		// ML = Message Length (After MF)
		// MF = Message Flag (0x02?)
		// UK = Unknown (I believe is the player id)
		// RF = RPC Flag (0x0d or 13 for Send Chat - RPC.SEND_CHAT)
		// TL = Text Length (Chat Message Text Length) 0x2 for hi, 0x5 for hello
		// TX = Chat Text (Length is whatever TL is)

//		LogHelper.printLine("Length: " + length);
//		LogHelper.printLine("buffer[15]: " + buffer[15]);
//		LogHelper.printLine("Combined: " + (buffer[15] + 16));

		if (length > 15 && (buffer[15] + 16) == length) {
			byte[] chatMessageBytes = new byte[buffer[15]];
			System.arraycopy(buffer, 0x10, chatMessageBytes, 0, buffer[15]);
			String chatMessage = new String(chatMessageBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

			// Unconfirmed if Player ID
			int playerID = buffer[13];

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("received_chat"), playerID, chatMessage));

			new PlayerChatEvent(playerID, chatMessage).call(server);

			// Chat reply is literally just this packet sent back
			return buffer;
		}

		return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("chat_packet_invalid_size"));
	}
}
