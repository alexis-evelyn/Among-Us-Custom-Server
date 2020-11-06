package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata.rpc;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.events.impl.PlayerChangePetEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class PetPacket {
	@NotNull
	public static byte[] handlePets(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, int netID, @NotNull byte... payload) {
		if (payload.length < 1)
			return ClosePacket.closeWithMessage(String.format(Main.getTranslationBundle().getString("invalid_number_of_bytes_minimum"), 1));

		byte petByte = payload[0];
		Pet pet = Pet.getPet(petByte);

		if (pet == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("pet_unknown"));

		LogHelper.printLine(String.format(Main.getTranslationBundle().getString("pet_packet"), Pet.getPetName(pet)));

		PlayerChangePetEvent event = new PlayerChangePetEvent(pet);
		event.call(server);

		if (!event.isCancelled()) {
			// TODO: Change pet to PlayerChangePetEvent#getPet()
		} else {
			// TODO: Cancel pet changing.
		}

		return new byte[0];
	}
}
