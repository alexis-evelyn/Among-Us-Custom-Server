package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;

import java.net.DatagramPacket;

public class FragmentPacket {
	/**
	 * Handle Fragment Packets
	 * <br><br>
	 * WARNING: Not implemented even in Hazel.
	 * <br><br>
	 *
	 * See: <a href="https://github.com/willardf/Hazel-Networking/blob/9dc6afef033a36a27e2d3d35f18c3b0bb8bfac87/Hazel/Udp/UdpConnection.cs#L86">Official Hazel Source</a>
	 *
	 * @param packet Fragment Packet
	 * @param server Server Instance
	 * @return Absolutely No Idea
	 */
	public static byte[] handleFragmentPacket(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// This will be added back once I get proper logging. That way the information would be invisible unless using debug level or something
//		LogHelper.printLine(Main.getTranslationBundle().getString("fragment_packet"));
//		LogHelper.printPacketBytes(length, buffer);

		return new byte[0];
	}
}