package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.PlayerColor;
import me.alexisevelyn.crewmate.enums.RPC;
import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeColorEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeHatEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerChangePetEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerChangeSkinEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.DatagramPacket;
import java.util.ResourceBundle;

public class CosmeticPacket {
	/**
	 * Will be replaced with individual cosmetic handling for cleaner code
	 *
	 * @param packet
	 * @param server
	 * @return
	 */
	@Deprecated
	public static byte[] handleCosmetics(DatagramPacket packet, Server server) {
		if (packet.getLength() != 16)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("cosmetic_packet_invalid_size"));

		int length = packet.getLength();
		byte[] buffer = packet.getData();
		byte[] nonce = new byte[] {buffer[1], buffer[2]};

//		LogHelper.printLine(Main.getTranslationBundle().getString("cosmetic_packet"));
//		LogHelper.printPacketBytes(length, buffer);

		byte typeByte = buffer[14]; // Type of Cosmetic Set
		byte cosmeticByte = buffer[15]; // Cosmetic ID

		RPC rpcType = RPC.getRPC(typeByte);

		// Sanitization Check
		if (rpcType == null)
			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("cosmetic_packet_unknown_type"));

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

	@Deprecated
	private static void printByteMeaning(String translationKey, String byteMeaning) {
		ResourceBundle translation = Main.getTranslationBundle();

		LogHelper.printLine(String.format(translation.getString(translationKey), byteMeaning));
	}
}
