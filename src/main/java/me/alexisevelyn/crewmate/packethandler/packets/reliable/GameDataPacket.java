package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.RPC;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

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
		// GC GC GC GC PL UK MT NI RT CL CT CT CT CT CT
		// GC = Game Code (LE INT-32)
		// PL = Payload Length
		// UK = Unknown
		// MT = Message Type (0x02 for RPC)
		// RT = RPC Type
		// CL = Chat Length
		// CT = Chat Text (ASCII)

		LogHelper.printLine("DEBUG: GameDataPacket");
		LogHelper.printPacketBytes(payload);

		// TODO: Create a Game Data Enum and Put 0x02 As The RPC Game Data Type
		int length = 0;
		byte[] buffer = new byte[0];
		if (length >= 15 && buffer[12] == 0x02) {
			RPC type = RPC.getRPC(buffer[14]);

			// Sanitization Check
			if (type == null) {
				LogHelper.printLine(String.format(Main.getTranslationBundle().getString("unknown_rpc_flag"), LogHelper.convertByteToHexString(buffer[14])));
				// LogHelper.printPacketBytes(length, buffer);

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
				// LogHelper.printPacketBytes(15, rpcBytes);

				return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}

			switch (type) {
				case SEND_CHAT:
					// return ChatPacket.handleChat(packet, server);
				case SET_COLOR:
				case SET_HAT:
				case SET_SKIN:
				case SET_PET:
					// return CosmeticPacket.handleCosmetics(packet, server); // 16 Bytes Total
				case SYNC_SETTINGS: // Double Check
					// return StartGame.getLobbyGameSettings(packet); // At Least 173 Bytes?
				default:
					return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}
		}

		return new byte[0];
	}
}
