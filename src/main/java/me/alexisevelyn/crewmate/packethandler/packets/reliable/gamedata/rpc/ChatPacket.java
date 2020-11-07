package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.events.impl.PlayerChatEvent;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.nio.charset.StandardCharsets;

public class ChatPacket {
	@NotNull
	public static byte[] handleChat(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, int netID, @NotNull byte... payload) {
		if (payload.length < 1)
			return new byte[0];

		int messageLength = payload[0];

		// Make Sure Payload Length Is Big Enough
		if ((payload.length - 1) < messageLength)
			return ClosePacket.closeWithMessage(Main.getTranslation("chat_packet_invalid_size"));

		String chatMessage = new String(PacketHelper.extractFirstPartBytes(messageLength, PacketHelper.extractSecondPartBytes(1, payload)), StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

		new PlayerChatEvent(netID, chatMessage).call(server);

		LogHelper.printLine(String.format(Main.getTranslation("received_chat"), netID, chatMessage));
		return new byte[0];
	}
}
