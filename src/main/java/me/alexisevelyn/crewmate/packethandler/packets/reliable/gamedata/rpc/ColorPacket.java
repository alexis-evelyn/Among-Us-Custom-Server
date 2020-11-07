package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.PlayerColor;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeColorEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class ColorPacket {
	@NotNull
	public static byte[] handleColors(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, int netID, @NotNull byte... payload) {
		if (payload.length < 1)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslation("invalid_number_of_bytes_minimum"), 1));

		byte colorByte = payload[0];
		PlayerColor color = PlayerColor.getColor(colorByte);

		if (color == null)
			return ClosePacket.closeWithMessage(Main.getTranslation("color_unknown"));

        LogHelper.printLine(String.format(Main.getTranslation("color_packet"), PlayerColor.getColorName(color)));

		PlayerChangeColorEvent event = new PlayerChangeColorEvent(color);
		event.call(server);

		if (!event.isCancelled()) {
			// TODO: Change skin to PlayerChangeColorEvent#getColor()
		} else {
			// TODO: Cancel skin changing.
		}

		return new byte[0];
	}
}
