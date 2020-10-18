package me.alexisevelyn.crewmate.handlers;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;

import java.net.DatagramPacket;

public class FragmentPacketHandler {
	public static byte[] handleFragmentPacket(DatagramPacket packet) {
		int length = packet.getLength();
		byte[] buffer = packet.getData();

		LogHelper.printLine(Main.getTranslationBundle().getString("fragment_packet"));
		LogHelper.printPacketBytes(buffer, length);

		return new byte[0];
	}
}
