package me.alexisevelyn.crewmate.packethandler;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.packets.*;
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

		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				// The first three bytes are irrelevant to the below function (hazel handshake and nonce)
				byte[] handshakeBytes = PacketHelper.extractBytes(packet.getData(), 3);

				return HandshakePacket.handleHandshake(handshakeBytes, (packet.getLength() - 3), server, packet.getAddress(), packet.getPort());
			case ACKNOWLEDGEMENT: // Acknowledgement of Received Data From Client
				if (packet.getLength() < 4)
					return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("nonce_wrong_size"));

				// Nonce Bytes
				byte[] nonce = new byte[]{packet.getData()[1], packet.getData()[2]};

				return AcknowledgementPacket.handleAcknowledgement(nonce, packet.getAddress(), packet.getPort(), server);
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
