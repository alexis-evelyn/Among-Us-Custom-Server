package me.alexisevelyn.crewmate.packethandler;

import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.Statistics;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.packets.*;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramPacket;

public class HazelPacket {
	/**
	 * Handles Packets Received By The Server
	 *
	 * @param packet UDP Packet
	 * @param server Instance of Server
	 * @return packet bytes to send to client or empty byte array to skip sending
	 */
	@NotNull
	public static byte[] handlePacket(@NotNull DatagramPacket packet, @NotNull Server server, @NotNull Statistics statistics) {
		SendOption sendOption = SendOption.getByte(packet.getData()[0]);

		// Throw Out Any Unknown Packets
		// Sanitization Check
		if (sendOption == null)
			return new byte[0];

		switch (sendOption) {
			case HELLO: // Initial Connection (Handshake)
				statistics.logHelloReceived(packet.getData()); // For Logging Received Hello Packets
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server, statistics);

				// The first three bytes are irrelevant to the below function (hazel handshake and nonce)
				byte[] handshakeBytes = PacketHelper.extractFirstPartBytes(packet.getLength() - 3, PacketHelper.extractSecondPartBytes(3, packet.getData()));

				return HandshakePacket.handleHandshake(server, statistics, packet.getAddress(), packet.getPort(), handshakeBytes);
			case ACKNOWLEDGEMENT: // Acknowledgement of Received Data From Client
				statistics.logAcknowledgementReceived(packet.getData()); // For Logging Received Acknowledgement Packets

				if (packet.getLength() < 4)
					return ClosePacket.closeWithMessage(Main.getTranslation("nonce_wrong_size"));

				// Nonce Bytes
				byte[] nonce = new byte[] {packet.getData()[1], packet.getData()[2]};

				return AcknowledgementPacket.handleAcknowledgement(packet.getAddress(), packet.getPort(), server, nonce);
			case PING: // Ping
				statistics.logPingReceived(packet.getData()); // For Logging Received Ping Packets
				// This is to stay the current format as we don't care about parsing ping past the nonce bytes.
				// Besides, there's three packet types that have nonce values, so it's better to keep the generic nonce parser
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server, statistics);

				return new byte[0];
			case RELIABLE: // Reliable Packet (UDP Doesn't Have Reliability Builtin Like TCP Does)
				statistics.logReliableReceived(packet.getData()); // For Logging Received Hello Packets
				AcknowledgementPacket.sendReliablePacketAcknowledgement(packet, server, statistics);

				// The first three bytes are irrelevant to the below function (hazel handshake and nonce)
				byte[] payloadBytes = PacketHelper.extractFirstPartBytes(packet.getLength() - 3, PacketHelper.extractSecondPartBytes(3, packet.getData()));
				byte[] replyBytes = GamePacket.handleAmongUsPacket(server, statistics, packet.getAddress(), packet.getPort(), payloadBytes);

				// Log Sent Reply (Could be Anything - Most Likely Reliable, Unreliable, Or Close)
				statistics.logUnknownSent(replyBytes);

				return replyBytes;
			case NONE: // Generic Unreliable Packet - Used For Movement (Unknown If Used For Anything Else)
				statistics.logUnreliableReceived(packet.getData()); // For Logging Received Hello Packets
				byte[] unPayloadBytes = PacketHelper.extractFirstPartBytes(packet.getLength() - 1, PacketHelper.extractSecondPartBytes(1, packet.getData()));
				byte[] replyBytesUnreliable = GamePacket.handleAmongUsPacket(server, statistics, packet.getAddress(), packet.getPort(), unPayloadBytes);

				// Log Sent Reply (Could be Anything - Most Likely Reliable, Unreliable, Or Close)
				statistics.logUnknownSent(replyBytesUnreliable);

				return replyBytesUnreliable;
			case FRAGMENT: // Fragmented Packet (For Data Bigger Than One Packet Can Hold) - Unknown If Used in Among Us
				statistics.logFragmentReceived(packet.getData()); // For Logging Received Fragment Packets
				// Not Implemented Even on Hazel. No Idea What The Packet Structure Would Look Like
				byte[] fragmentBytes = PacketHelper.extractFirstPartBytes(packet.getLength() - 1, PacketHelper.extractSecondPartBytes(1, packet.getData()));
				byte[] replyBytesFragmented = FragmentPacket.handleFragmentPacket(server, statistics, packet.getAddress(), packet.getPort(), fragmentBytes);

				// Log Sent Reply (Could be Anything - Most Likely Reliable, Unreliable, Or Close)
				statistics.logUnknownSent(replyBytesFragmented);

				return replyBytesFragmented;
		}

		// Log's Unknown Bytes
		statistics.logUnknownReceived(packet.getData());

		return new byte[0];
	}
}
