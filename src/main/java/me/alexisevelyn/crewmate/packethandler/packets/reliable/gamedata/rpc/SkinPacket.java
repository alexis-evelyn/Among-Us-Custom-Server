package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeSkinEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class SkinPacket {
	@NotNull
	public static byte[] handleSkins(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, int netID, @NotNull byte... payload) {
		if (payload.length < 1)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 1));

		byte skinByte = payload[0];
		Skin skin = Skin.getSkin(skinByte);

		if (skin == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("skin_unknown"));

		LogHelper.printLine(String.format(Main.getTranslationBundle().getString("skin_packet"), Skin.getSkinName(skin)));

		PlayerChangeSkinEvent event = new PlayerChangeSkinEvent(skin);
		event.call(server);

		if (!event.isCancelled()) {
			// TODO: Change skin to PlayerChangeSkinEvent#getSkin()
		} else {
			// TODO: Cancel skin changing.
		}

		return new byte[0];
	}
}
