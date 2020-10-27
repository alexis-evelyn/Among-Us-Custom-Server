package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.events.impl.PlayerChatEvent;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

import java.net.DatagramPacket;
import java.nio.charset.StandardCharsets;

public class ChatPacket {
	// TODO: Read Game Packet Data
	// Movement Packet - https://gist.github.com/codyphobe/cc738881daf11da519ee9d4a77d24f62
	// Reliable Packet Format - https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets

	// Reliable Packet Format - RP NO NO PL PL PT
	// RP = Reliable Packet Identifier (0x01)
	// NO = Nonce
	// PL = Packet Length (Starts After PT)
	// PT = Packet Type (What We Check In This File)

	public static byte[] handleChat(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// Game Code - ABCDEF (3b:be:25:8c)

		// Chat C->S - hello - (0f 00 = 15)
		// 0000   01 00 11 0f 00 05 3b be 25 8c 08 00 02 04 0d 05   ......;.%.......
		// 0010   68 65 6c 6c 6f                                    hello

		// Chat C->S - hi - (0c 00 = 12)
		// 0000   01 01 10 0c 00 05 3b be 25 8c 05 00 02 04 0d 02   ......;.%.......
		// 0010   68 69                                             hi

		// Chat C->S - hello - (0f 00 = 15)
		// 0000   01 00 0d 0f 00 05 41 4c 45 58 08 00 02 04 0d 05   ......ALEX......
		// 0010   68 65 6c 6c 6f                                    hello

		// Chat C->S Format
		// 0000   RP NO NO PL PL PT GC GC GC GC ML ML MF UK RF TL
		// 0010   TX TX ...
		//
		// RP = Reliable Packet (0x01)
		// NO = Nonce (Repeat To Client To Confirm Received Packet)
		// PL = Packet Length (Starts After PT)
		// PT = Packet Type (GAME_DATA - 0x05)
		// GC = Game Code (Room Code)
		// ML = Message Length (After MF)
		// MF = Message Flag (0x02?)
		// UK = Unknown (I believe is the player id)
		// RF = RPC Flag (0x0d or 13 for Send Chat - RPC.SEND_CHAT)
		// TL = Text Length (Chat Message Text Length) 0x2 for hi, 0x5 for hello
		// TX = Chat Text (Length is whatever TL is)

//		LogHelper.printLine("Length: " + length);
//		LogHelper.printLine("buffer[15]: " + buffer[15]);
//		LogHelper.printLine("Combined: " + (buffer[15] + 16));

		if (length > 15 && (buffer[15] + 16) == length) {
			byte[] chatMessageBytes = new byte[buffer[15]];
			System.arraycopy(buffer, 0x10, chatMessageBytes, 0, buffer[15]);
			String chatMessage = new String(chatMessageBytes, StandardCharsets.UTF_8); // Can we assume it will always be UTF-8?

			// Unconfirmed if Player ID
			int playerID = buffer[13];

			LogHelper.printLine(String.format(Main.getTranslationBundle().getString("received_chat"), playerID, chatMessage));

			new PlayerChatEvent(playerID, chatMessage).call(server);

			// Chat reply is literally just this packet sent back
			return buffer;
		}

		return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("chat_packet_invalid_size"));
	}
}
