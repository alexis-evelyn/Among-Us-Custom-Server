package me.alexisevelyn.crewmate.packethandler;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.packethandler.packets.*;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
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
	public static byte[] handlePacket(DatagramPacket packet, Server server) throws InvalidGameCodeException, IOException {
		SendOption sendOption = SendOption.getSendOption(packet.getData()[0]);

		// Throw Out Any Unknown Packets
		// Sanitization Check
		if (sendOption == null)
			return new byte[0];

		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				// The first three bytes are irrelevant to the below function (hazel handshake and nonce)
				byte[] handshakeBytes = PacketHelper.extractSecondPartBytes(3, packet.getData());

				return HandshakePacket.handleHandshake(server, packet.getAddress(), packet.getPort(), (packet.getLength() - 3), handshakeBytes);
			case ACKNOWLEDGEMENT: // Acknowledgement of Received Data From Client
				if (packet.getLength() < 4)
					return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("nonce_wrong_size"));

				// Nonce Bytes
				byte[] nonce = new byte[] {packet.getData()[1], packet.getData()[2]};

				return AcknowledgementPacket.handleAcknowledgement(packet.getAddress(), packet.getPort(), server, nonce);
			case PING: // Ping
				// This is to stay the current format as we don't care about parsing ping past the nonce bytes.
				// Besides, there's three packet types that have nonce values, so it's better to keep the generic nonce parser
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				return new byte[0];
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server);

				// The first three bytes are irrelevant to the below function (hazel handshake and nonce)
				byte[] reliableBytes = PacketHelper.extractSecondPartBytes(3, packet.getData());

				return ReliablePacket.handleReliablePacket(server, packet.getAddress(), packet.getPort(), (packet.getLength() - 3), reliableBytes);
			case NONE: // Generic Unreliable Packet - Used For Movement (Unknown If Used For Anything Else)
				return UnreliablePacket.handleUnreliablePacket(packet, server);
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
				// Not Implemented Even on Hazel. No Idea What The Packet Structure Would Look Like
				return FragmentPacket.handleFragmentPacket(packet, server);
		}

		return new byte[0];
	}
}
