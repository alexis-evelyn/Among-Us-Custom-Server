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

		byte[] replyBuffer;
		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				replyBuffer = HandshakePacket.handleHandshake(packet, server);
				break;
			case ACKNOWLEDGEMENT: // Unhandled
			case PING: // Ping
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);
				return new byte[0];
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				replyBuffer = GamePacket.handleReliablePacket(packet, server);
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);
				break;
			case NONE: // Generic Unreliable Packet - Used For Movement (Unknown If Used For Anything Else)
				replyBuffer = GamePacket.handleUnreliablePacket(packet, server);
				break;
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
				replyBuffer = FragmentPacket.handleFragmentPacket(packet, server);
				break;
			default:
				return new byte[0];
		}

		return replyBuffer;
	}
}
