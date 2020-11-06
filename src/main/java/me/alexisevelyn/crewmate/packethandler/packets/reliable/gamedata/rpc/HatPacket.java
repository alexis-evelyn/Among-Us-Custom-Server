package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeHatEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class HatPacket {
	@NotNull
	public static byte[] handleHats(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, int netID, @NotNull byte... payload) {
		if (payload.length < 1)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 1));

		byte hatByte = payload[0];
		Hat hat = Hat.getHat(hatByte);

		if (hat == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("hat_unknown"));

		LogHelper.printLine(String.format(Main.getTranslationBundle().getString("hat_packet"), Hat.getHatName(hat)));

		PlayerChangeHatEvent event = new PlayerChangeHatEvent(hat);
		event.call(server);

		if (!event.isCancelled()) {
			// TODO: Change hat to PlayerChangeHatEvent#getHat()
		} else {
			// TODO: Cancel hat changing.
		}

		return new byte[0];
	}
}
