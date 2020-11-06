package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.GameDataType;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.InetAddress;

public class GameDataPacket {
	/**
	 *
	 * @param server
	 * @param clientAddress
	 * @param clientPort
	 * @param payload
	 * @return
	 */
	public static byte[] parseGameData(Server server, InetAddress clientAddress, int clientPort, byte... payload) {
		// 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e
		// --------------------------------------------
		// 42 45 41 54 08 00 02 04 0D 05 68 65 6C 6C 6F
		// E9 DD 12 8A 08 00 02 04 0D 05 68 65 6C 6C 6F
		// E9 DD 12 8A 07 00 02 04 0D 04 74 65 73 74
		// 46 41 53 54 05 00 02 04 0d 02 68 69
		// --------------------------------------------
		// GC GC GC GC PL PL MT NI RT CL CT CT CT CT CT
		// GC = Game Code (LE INT-32)
		// PL = Payload Length (UINT-16 LE)
		// MT = Message Type (0x02 for RPC)
		// RT = RPC Type
		// CL = Chat Length
		// CT = Chat Text (ASCII)

//		LogHelper.printLine("DEBUG: GameDataPacket");
//		LogHelper.printPacketBytes(payload);

		// Needs to be a minimum of 7 bytes to be able to parse game data type
		if (payload.length < 7)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 7));

		GameDataType gameDataType = GameDataType.getGameDataType(payload[6]);

		// Sanitization
		if (gameDataType == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("unknown_game_data_type"));

		switch (gameDataType) {
			case COMPONENT_DATA:
			case RPC:
			case UNKNOWN:
			case SPAWN:
			case DESPAWN:
			case SCENE_CHANGE:
			case READY:
			case CHANGE_SETTINGS:
			default:
				LogHelper.printLine("DEBUG: " + GameDataType.getGameDataTypeName(gameDataType));
		}

		// RPC Parsing
//		RPC type = RPC.getRPC(buffer[14]);
//
//		switch (type) {
//			case SEND_CHAT:
//				// return ChatPacket.handleChat(packet, server);
//			case SET_COLOR:
//			case SET_HAT:
//			case SET_SKIN:
//			case SET_PET:
//				// return CosmeticPacket.handleCosmetics(packet, server); // 16 Bytes Total
//			case SYNC_SETTINGS: // Double Check
//				// return StartGame.getLobbyGameSettings(packet); // At Least 173 Bytes?
//			default:
//				return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
//		}

		return new byte[0];
	}
}
