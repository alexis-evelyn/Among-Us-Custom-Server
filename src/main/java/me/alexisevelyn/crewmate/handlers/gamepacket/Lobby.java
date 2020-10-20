package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.*;
import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.impl.*;

import java.net.DatagramPacket;
import java.util.ResourceBundle;

public class Lobby {
	public static byte[] handleGameVisibility(DatagramPacket packet, Server server) {
		// 0000   01 00 59 06 00 0a 3b be 25 8c 01 00               ..Y...;.%... - Private Game
		// 0000   01 00 42 06 00 0a 3b be 25 8c 01 01               ..B...;.%... - Public Game

		if (packet.getLength() != 12)
			return new byte[0];

		byte[] buffer = packet.getData();

		String visibility = (buffer[11] == 1) ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");

		ChangeVisibilityEvent event = new ChangeVisibilityEvent(visibility.equals(Main.getTranslationBundle().getString("public_game")));
		event.call(server);
		if (!event.isCancelled()) {
			visibility = event.isVisible() ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");
		}

		// Used For Debugging
		LogHelper.printPacketBytes(buffer, packet.getLength());
		LogHelper.printLine(visibility);

		return new byte[0];
	}

	public static byte[] handleCosmetics(DatagramPacket packet, Server server) {
		if (packet.getLength() != 16)
			return new byte[0];

		int length = packet.getLength();
		byte[] buffer = packet.getData();

//		LogHelper.printLine(Main.getTranslationBundle().getString("cosmetic_packet"));
//		LogHelper.printPacketBytes(buffer, length);

		byte typeByte = buffer[14]; // Type of Cosmetic Set
		byte cosmeticByte = buffer[15]; // Cosmetic ID

		RPC rpcType = RPC.getRPC(typeByte);

		// Sanitization Check
		if (rpcType == null)
			return new byte[0];

		switch (rpcType) {
			case SET_COLOR:
				printByteMeaning("color_packet", PlayerColor.getColorName(PlayerColor.getColor(cosmeticByte)));
				PlayerChangeColorEvent event = new PlayerChangeColorEvent(PlayerColor.getColor(cosmeticByte));
				event.call(server);
				if (!event.isCancelled()) {
					// TODO: Change skin to PlayerChangeColorEvent#getColor()
				} else {
					// TODO: Cancel skin changing.
				}
				break;
			case SET_HAT:
				printByteMeaning("hat_packet", Hat.getHatName(Hat.getHat(cosmeticByte)));
				PlayerChangeHatEvent event1 = new PlayerChangeHatEvent(Hat.getHat(cosmeticByte));
				event1.call(server);
				if (!event1.isCancelled()) {
					// TODO: Change hat to PlayerChangeHatEvent#getHat()
				} else {
					// TODO: Cancel hat changing.
				}
				break;
			case SET_PET:
				printByteMeaning("pet_packet", Pet.getPetName(Pet.getPet(cosmeticByte)));
				PlayerChangePetEvent event2 = new PlayerChangePetEvent(Pet.getPet(cosmeticByte));
				event2.call(server);
				if (!event2.isCancelled()) {
					// TODO: Change pet to PlayerChangePetEvent#getPet()
				} else {
					// TODO: Cancel pet changing.
				}
				break;
			case SET_SKIN:
				printByteMeaning("skin_packet", Skin.getSkinName(Skin.getSkin(cosmeticByte)));
				PlayerChangeSkinEvent event3 = new PlayerChangeSkinEvent(Skin.getSkin(cosmeticByte));
				event3.call(server);
				if (!event3.isCancelled()) {
					// TODO: Change skin to PlayerChangeSkinEvent#getSkin()
				} else {
					// TODO: Cancel skin changing.
				}
				break;
		}

		return new byte[0];
	}

	private static void printByteMeaning(String translationKey, String byteMeaning) {
		ResourceBundle translation = Main.getTranslationBundle();

		LogHelper.printLine(String.format(translation.getString(translationKey), byteMeaning));
	}
}
