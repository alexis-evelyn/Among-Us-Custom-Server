package me.alexisevelyn.crewmate.packethandler;

import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.packets.AcknowledgementPacket;
import me.alexisevelyn.crewmate.packethandler.packets.FragmentPacket;
import me.alexisevelyn.crewmate.packethandler.packets.GamePacket;
import me.alexisevelyn.crewmate.packethandler.packets.HandshakePacket;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramPacket;

public class HazelParser {
	/**
	 * Handles Packets Received By The Server
	 *
	 * @param packet UDP Packet
	 * @param server Instance of Server
	 * @return packet bytes to send to client or empty byte array to skip sending
	 */
	@NotNull
	public static byte[] handlePacket(DatagramPacket packet, Server server) {
		SendOption sendOption = SendOption.getSendOption(packet.getData()[0]);

		// Throw Out Any Unknown Packets
		// Sanitization Check
		if (sendOption == null)
			return new byte[0];

		byte[] packetBytes = PacketHelper.extractBytes(packet.getData(), 0);
		int packetLength = packet.getLength();

		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				return HandshakePacket.handleHandshake(packet, server);
			case ACKNOWLEDGEMENT: // Unhandled
				return AcknowledgementPacket.handleAcknowledgement(packet, server);
			case PING: // Ping
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				return new byte[0];
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				return GamePacket.handleReliablePacket(packet, server);
			case NONE: // Generic Unreliable Packet - Used For Movement (Unknown If Used For Anything Else)
				return GamePacket.handleUnreliablePacket(packet, server);
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
				return FragmentPacket.handleFragmentPacket(packet, server);
		}

		return new byte[0];
	}
}
