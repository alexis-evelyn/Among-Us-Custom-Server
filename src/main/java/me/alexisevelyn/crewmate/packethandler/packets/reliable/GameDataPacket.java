package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.GameDataType;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.RPCPacket;
import org.jetbrains.annotations.NotNull;

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
	@NotNull
	public static byte[] parseGameData(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payload) {
		// 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e
		// --------------------------------------------
		// 42 45 41 54 08 00 02 04 0D 05 68 65 6C 6C 6F
		// E9 DD 12 8A 08 00 02 04 0D 05 68 65 6C 6C 6F
		// E9 DD 12 8A 07 00 02 04 0D 04 74 65 73 74
		// 46 41 53 54 05 00 02 04 0d 02 68 69
		// 42 4F 49 4C 12 00 04 03 FE FF FF FF
		// 00 00 00 00 0C 00 04 02 FE FF FF FF
		// --------------------------------------------
		// GC GC GC GC PL PL MT NI RT CL CT CT CT CT CT
		// GC = Game Code (LE INT-32)
		// PL = Payload Length (UINT-16 LE)
		// MT = Message Type (0x02 for RPC)
		// NI = Net ID
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

		byte[] gameDataPayload = PacketHelper.extractSecondPartBytes(7, payload);

		switch (gameDataType) {
			case COMPONENT_DATA:
			case RPC:
				return RPCPacket.handleRPCPacket(server, clientAddress, clientPort, gameDataPayload);
			case UNKNOWN:
			case SPAWN:
			case DESPAWN:
			case SCENE_CHANGE:
			case READY:
			case CHANGE_SETTINGS:
			default:
				// LogHelper.printLine("DEBUG: " + GameDataType.getGameDataTypeName(gameDataType));
		}

		return new byte[0];
	}
}
