package me.alexisevelyn.crewmate;

public class PacketHelper {
	public static byte[] closeWithMessage(String message) {
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.getBytes().length};

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
}
