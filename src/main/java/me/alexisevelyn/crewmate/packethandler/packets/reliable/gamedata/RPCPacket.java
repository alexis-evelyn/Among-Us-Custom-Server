package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.RPC;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc.*;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class RPCPacket {
	@NotNull
	public static byte[] handleRPCPacket(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payload) {
		if (payload.length < 2)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslation("invalid_number_of_bytes_minimum"), 2));

		int netID = payload[0];
		RPC type = RPC.getRPC(payload[1]);
		byte[] rpcPayload = PacketHelper.extractSecondPartBytes(2, payload);

		switch (type) {
			case SEND_CHAT:
				return ChatPacket.handleChat(server, clientAddress, clientPort, netID, rpcPayload);
			case SET_COLOR:
				return ColorPacket.handleColors(server, clientAddress, clientPort, netID, rpcPayload);
			case SET_HAT:
				return HatPacket.handleHats(server, clientAddress, clientPort, netID, rpcPayload);
			case SET_SKIN:
				return SkinPacket.handleSkins(server, clientAddress, clientPort, netID, rpcPayload);
			case SET_PET:
				return PetPacket.handlePets(server, clientAddress, clientPort, netID, rpcPayload);
			case SYNC_SETTINGS: // Double Check
				// return StartGame.getLobbyGameSettings(server, clientAddress, clientPort, netID, rpcPayload); // At Least 173 Bytes?
			default:
				// return ClosePacket.closeWithMessage(Main.getTranslation("rpc_packet_unknown_type"));
				LogHelper.printLine("DEBUG: " + RPC.getRPCName(type));
				return new byte[0];
		}
	}
}
