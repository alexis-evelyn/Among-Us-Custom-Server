package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;

import java.net.DatagramPacket;

public class FragmentPacket {
	public static byte[] handleFragmentPacket(DatagramPacket packet, Server server) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		LogHelper.printLine(Main.getTranslationBundle().getString("fragment_packet"));
		LogHelper.printPacketBytes(buffer, length);

		return new byte[0];
	}
}
