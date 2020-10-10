package me.alexisevelyn.crewmate;

public class PacketHelper {
	public static byte[] closeWithMessage(String message) {
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.getBytes().length};

		return getCombinedReply(header, message.getBytes());
	}

	public static byte[] getUnderConstructionMessage(String extra) {
		// 0000   09 01 2e 00 00 08 2c 54 68 65 20 73 65 72 76 65   ......,The serve
		// 0010   72 20 63 6c 6f 73 65 64 20 74 68 65 20 72 6f 6f   r closed the roo
		// 0020   6d 20 64 75 65 20 74 6f 20 69 6e 61 63 74 69 76   m due to inactiv
		// 0030   69 74 79                                          ity

		StringBuilder messageBuilder = new StringBuilder("This Server Is Under Construction. :P");

		if (extra.length() > 0)
			messageBuilder.append("\n\n").append("Extra Info: ").append("\n").append(extra);

		byte[] message = messageBuilder.toString().getBytes();
		byte[] header = new byte[] {0x09, 0x01, 0x2e, 0x00, 0x00, 0x08, (byte) message.length};

		return getCombinedReply(header, message);
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
