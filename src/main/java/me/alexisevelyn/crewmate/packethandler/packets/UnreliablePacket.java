package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.Server;

import java.net.DatagramPacket;

public class UnreliablePacket {
	public static byte[] handleUnreliablePacket(DatagramPacket packet, Server server) {
		// https://wiki.weewoo.net/wiki/Protocol#0_-_Unreliable_Packets

		int length = packet.getLength();
		byte[] buffer = packet.getData();

		// LogHelper.printLine(Main.getTranslationBundle().getString("unreliable_packet"));
		// LogHelper.printPacketBytes(length, buffer);

		return new byte[0];
	}
}
