package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

public class PacketHelper {
	public static byte[] closeWithMessage(String message) {
		return closeConnection(message, DisconnectReason.CUSTOM);
	}

	public static byte[] closeConnection(DisconnectReason disconnectReason) {
		return closeConnection(null, disconnectReason);
	}

	public static byte[] closeConnection(String message, DisconnectReason disconnectReason) {
		byte[] header;

		// Custom Disconnect Message
		boolean customMessage = disconnectReason.equals(DisconnectReason.CUSTOM) || disconnectReason.equals(DisconnectReason.LEGACY_CUSTOM);
		if (message != null && (customMessage)) {
			byte[] messageLengthBytes = convertShortToLE((short) message.getBytes().length);
			header = new byte[]{SendOption.DISCONNECT.getSendOption(), 0x01, messageLengthBytes[0], messageLengthBytes[1], 0x00, disconnectReason.getReason(), (byte) message.getBytes().length};

			return getCombinedReply(header, message.getBytes());
		} else if (customMessage) {
			// Failure To Provide Non-Null Message For Custom Message Reason
			return new byte[]{SendOption.DISCONNECT.getSendOption(), 0x01, 0x00, 0x00, 0x00, DisconnectReason.NONE.getReason()};
		}

		// Predefined Disconnect Message
		return new byte[]{SendOption.DISCONNECT.getSendOption(), 0x01, 0x00, 0x00, 0x00, disconnectReason.getReason()};
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
