package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.RPC;
import me.alexisevelyn.crewmate.handlers.gamepacket.Lobby;
import me.alexisevelyn.crewmate.handlers.gamepacket.StartGame;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.ChatPacket;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.CosmeticPacket;

import java.net.DatagramPacket;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GameDataPacket {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62
	// Reliable Packet Format - https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets

	// Reliable Packet Format - RP NO NO PL PL PT
	// RP = Reliable Packet Identifier (0x01)
	// NO = Nonce
	// PL = Packet Length (Starts After PT)
	// PT = Packet Type (What We Check In This File)

	public static byte[] parseGameData(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// TODO: Create a Game Data Enum and Put 0x02 As The RPC Game Data Type
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
				LogHelper.printPacketBytes(15, rpcBytes);

				return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}

			switch (type) {
				case SEND_CHAT:
					return ChatPacket.handleChat(packet, server);
				case SET_COLOR:
				case SET_HAT:
				case SET_SKIN:
				case SET_PET:
					return CosmeticPacket.handleCosmetics(packet, server); // 16 Bytes Total
				case SYNC_SETTINGS: // Double Check
					return StartGame.getLobbyGameSettings(packet); // At Least 173 Bytes?
				default:
					return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("rpc_packet_unknown_type"));
			}
		}

		return new byte[0];
	}
}
