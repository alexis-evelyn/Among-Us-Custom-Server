package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;

import java.net.InetAddress;

public class FragmentPacket {
	/**
	 * Handle Fragment Packets
	 * <br><br>
	 * WARNING: Not implemented even in Hazel.
	 * <br><br>
	 *
	 * See: <a href="https://github.com/willardf/Hazel-Networking/blob/9dc6afef033a36a27e2d3d35f18c3b0bb8bfac87/Hazel/Udp/UdpConnection.cs#L86">Official Hazel Source</a>
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param byteLength count of payload bytes
	 * @param payloadBytes payload bytes
	 * @return data to send back to client
	 */
	public static byte[] handleFragmentPacket(Server server, InetAddress clientAddress, int clientPort, int byteLength, byte... payloadBytes) {
		// This will be added back once I get proper logging. That way the information would be invisible unless using debug level or something
//		LogHelper.printLine(Main.getTranslationBundle().getString("fragment_packet"));
//		LogHelper.printPacketBytes(byteLength, payloadBytes);

		return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("fragment_packet_not_supported"));
	}
}