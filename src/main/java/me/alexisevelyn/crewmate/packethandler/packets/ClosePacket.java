package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.enums.DisconnectReason;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.jetbrains.annotations.NotNull;

public class ClosePacket {
	@NotNull
	public static byte[] closeWithMessage(String message) {
		return closeConnection(message, DisconnectReason.CUSTOM);
	}

	@NotNull
	public static byte[] closeConnection(DisconnectReason disconnectReason) {
		return closeConnection(null, disconnectReason);
	}

	private static byte[] closeConnection(String message, DisconnectReason disconnectReason) {
		byte[] header;

		// Custom Disconnect Message
		boolean customMessage = disconnectReason.equals(DisconnectReason.CUSTOM) || disconnectReason.equals(DisconnectReason.LEGACY_CUSTOM);
		if (message != null && (customMessage)) {
			byte[] messageLengthBytes = PacketHelper.convertShortToLE((short) message.getBytes().length);
			header = new byte[]{SendOption.DISCONNECT.getByte(), 0x01, messageLengthBytes[0], messageLengthBytes[1], 0x00, disconnectReason.getByte(), (byte) message.getBytes().length};

			return PacketHelper.mergeBytes(header, message.getBytes());
		} else if (customMessage) {
			// Failure To Provide Non-Null Message For Custom Message Reason
			return new byte[]{SendOption.DISCONNECT.getByte(), 0x01, 0x00, 0x00, 0x00, DisconnectReason.NONE.getByte()};
		}

		// Predefined Disconnect Message
		return new byte[]{SendOption.DISCONNECT.getByte(), 0x01, 0x00, 0x00, 0x00, disconnectReason.getByte()};
	}
}
