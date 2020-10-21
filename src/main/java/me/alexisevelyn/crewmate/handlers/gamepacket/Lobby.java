package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.enums.*;
import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;

import java.net.DatagramPacket;
import java.util.ResourceBundle;

public class Lobby {
	public static byte[] alterGame(DatagramPacket packet) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		if (length < 6)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_invalid_size"));

		AlterGame alterGameFlag = AlterGame.getAlterGameFlag(buffer[5]);

		if (alterGameFlag == null)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));

		switch (alterGameFlag) {
			case CHANGE_PRIVACY:
				return handleGameVisibility(packet);
			default:
				return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_unknown_type"));
		}
	}

	private static byte[] handleGameVisibility(DatagramPacket packet) {
		// 0000   01 00 59 06 00 0a 3b be 25 8c 01 00               ..Y...;.%... - Private Game
		// 0000   01 00 42 06 00 0a 3b be 25 8c 01 01               ..B...;.%... - Public Game

		// 0000   RP NO NO ML ML MF GC GC GC GC AG PG
		// RP = Reliable Packet (0x01)
		// NO = Nonce
		// ML = Message Length (LE UINT-16 - Starts After MF)
		// MF = Message Flag (0x0a or 10 for Alter Game)
		// GC = Game Code (LE UINT-32)
		// AG = Alter Game Flag (0x01 For Change Privacy)
		// PG = Public/Private Game Boolean

		if (packet.getLength() != 12)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("alter_game_packet_invalid_size"));

		byte[] buffer = packet.getData();

		String visibility = (buffer[11] == 1) ? Main.getTranslationBundle().getString("public_game") : Main.getTranslationBundle().getString("private_game");

		// Extract Game Code Bytes
		byte[] gameCodeBytes = new byte[4];
		System.arraycopy(buffer, 6, gameCodeBytes, 0, 4);

		String gameCode;
		try {
			gameCode = GameCodeHelper.parseGameCode(gameCodeBytes);
		} catch (InvalidBytesException | InvalidGameCodeException exception) {
			return PacketHelper.closeWithMessage(exception.getMessage());
		}

		// Used For Debugging
		String displayVisibilityString = String.format(Main.getTranslationBundle().getString("print_key_value"), gameCode, visibility);
		LogHelper.printLine(displayVisibilityString);

		return new byte[0];
	}

	public static byte[] handleCosmetics(DatagramPacket packet) {
		if (packet.getLength() != 16)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("cosmetic_packet_invalid_size"));

		int length = packet.getLength();
		byte[] buffer = packet.getData();
		byte[] nonce = new byte[] {buffer[1], buffer[2]};

//		LogHelper.printLine(Main.getTranslationBundle().getString("cosmetic_packet"));
//		LogHelper.printPacketBytes(buffer, length);

		byte typeByte = buffer[14]; // Type of Cosmetic Set
		byte cosmeticByte = buffer[15]; // Cosmetic ID

		RPC rpcType = RPC.getRPC(typeByte);

		// Sanitization Check
		if (rpcType == null)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("cosmetic_packet_unknown_type"));

		switch (rpcType) {
			case SET_COLOR:
				printByteMeaning("color_packet", PlayerColor.getColorName(PlayerColor.getColor(cosmeticByte)));
				break;
			case SET_HAT:
				printByteMeaning("hat_packet", Hat.getHatName(Hat.getHat(cosmeticByte)));
				break;
			case SET_PET:
				printByteMeaning("pet_packet", Pet.getPetName(Pet.getPet(cosmeticByte)));
				break;
			case SET_SKIN:
				printByteMeaning("skin_packet", Skin.getSkinName(Skin.getSkin(cosmeticByte)));
				break;
		}

		return new byte[0];
	}

	private static void printByteMeaning(String translationKey, String byteMeaning) {
		ResourceBundle translation = Main.getTranslationBundle();

		LogHelper.printLine(String.format(translation.getString(translationKey), byteMeaning));
	}
}
