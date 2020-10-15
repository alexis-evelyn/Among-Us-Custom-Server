package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

public class PacketHelper {
	public static byte[] closeWithMessage(String message) {
		byte[] messageLengthBytes = convertShortToLE((short) message.getBytes().length);
		byte[] header = new byte[] {SendOption.DISCONNECT.getSendOption(), DisconnectReason.GAME_FULL.getReason(), messageLengthBytes[0], messageLengthBytes[1], 0x00, 0x08, (byte) message.getBytes().length};

		return getCombinedReply(header, message.getBytes());
	}

	public static byte[] getCombinedReply(byte[] header, byte[] message) {
		byte[] reply = new byte[header.length + message.length];

		// Copy Header into Reply
		System.arraycopy(header, 0, reply, 0, header.length);

		// Copy Message into Reply
		System.arraycopy(message, 0, reply, header.length, message.length);

		return reply;
	}

	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte)(value & 0xff), (byte)((value >> 8) & 0xff)};
	}
}
