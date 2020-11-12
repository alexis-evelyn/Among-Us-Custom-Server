package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.Statistics;
import org.jetbrains.annotations.NotNull;

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
	 * @param payloadBytes payload bytes
	 * @return data to send back to client
	 */
	@NotNull
	public static byte[] handleFragmentPacket(@NotNull Server server, @NotNull Statistics statistics, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payloadBytes) {
		// This will be added back once I get proper logging. That way the information would be invisible unless using debug level or something
//		LogHelper.printLine(Main.getTranslation("fragment_packet"));
//		LogHelper.printPacketBytes(payloadBytes);

		return ClosePacket.closeWithMessage(Main.getTranslation("fragment_packet_not_supported"));
	}
}