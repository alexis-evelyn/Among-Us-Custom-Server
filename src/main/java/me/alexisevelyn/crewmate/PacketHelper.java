package me.alexisevelyn.crewmate;

import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;

import java.util.ArrayList;

public class PacketHelper {
	public static byte[] getAcknowledgement(byte[] nonceBytes) {
		// Should this be an exception or close connection?
		if (nonceBytes.length != 2)
			return PacketHelper.closeWithMessage(Main.getTranslationBundle().getString("nonce_wrong_size"));

		return new byte[] {SendOption.ACKNOWLEDGEMENT.getSendOption(), nonceBytes[0], nonceBytes[1], (byte) 0xff};
	}

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

			return mergeBytes(header, message.getBytes());
		} else if (customMessage) {
			// Failure To Provide Non-Null Message For Custom Message Reason
			return new byte[]{SendOption.DISCONNECT.getSendOption(), 0x01, 0x00, 0x00, 0x00, DisconnectReason.NONE.getReason()};
		}

		// Predefined Disconnect Message
		return new byte[]{SendOption.DISCONNECT.getSendOption(), 0x01, 0x00, 0x00, 0x00, disconnectReason.getReason()};
	}

	public static byte[] mergeBytes(byte[] ...bytes) {
		// TODO: Make this a standard function for creating packets

		// Don't Bother Merging If Nothing To Merge
		if (bytes == null)
			return new byte[0];

		ArrayList<Byte> byteList = new ArrayList<>();

		for (byte[] byteArray : bytes) {
			// You'd think people would know better than to not pass in null values, but just in case, skip this byte[]
			if (byteArray == null)
				continue;

			for (byte bite : byteArray) {
				byteList.add(bite);
			}
		}

		Byte[] mergedBytes = byteList.toArray(new Byte[0]);
		byte[] mergedBytesPrimitive = new byte[mergedBytes.length];

		for (int i = 0; i < mergedBytesPrimitive.length; i++) {
			mergedBytesPrimitive[i] = mergedBytes[i]; // Apparently Unboxing Is Automatic
		}

		return mergedBytesPrimitive;
	}

	public static byte[] convertShortToLE(short value) {
		return new byte[] {(byte)(value & 0xff), (byte)((value >> 8) & 0xff)};
	}
}
