package me.alexisevelyn.crewmate.packethandler.packets;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.Statistics;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import org.jetbrains.annotations.NotNull;

import java.net.DatagramPacket;
import java.net.InetAddress;

public class AcknowledgementPacket {
	/**
	 * Updates the last time a client acknowledgement was seen as well as the value of the nonce.
	 *
	 * Used to help determine if the client's connection is dead.
	 *
	 * This method does not close the connection if the connection is dead. The Ping Method takes care of that (not implemented).
	 *
	 * @param nonce 2 byte array with the nonce value from the client
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param server Server Instance
	 * @return Empty Byte Array Or Close Connection Byte Array
	 */
	@NotNull
	public static byte[] handleAcknowledgement(@NotNull InetAddress clientAddress, int clientPort, @NotNull Server server, @NotNull byte... nonce) {
		if (nonce.length < 2)
			return ClosePacket.closeWithMessage(Main.getTranslation("nonce_wrong_size"));

		// TODO: Implement

		return new byte[0];
	}

	/**
	 * Generates acknowledgement packet bytes to send to client
	 *
	 * @param nonceBytes 2 byte array of the nonce data to return to client
	 * @return bytes to send to client in the form of a Hazel acknowledgment packet
	 */
	private static byte[] getAcknowledgement(byte... nonceBytes) throws InvalidBytesException {
		if (nonceBytes.length != 2)
			throw new InvalidBytesException(Main.getTranslation("nonce_wrong_size"));

		return new byte[] {SendOption.ACKNOWLEDGEMENT.getByte(), nonceBytes[0], nonceBytes[1], (byte) 0xff};
	}

	/**
	 * Sends Acknowledgement Packet In Response To Reliable Packet, Handshake, Or Ping
	 *
	 * @param packet Reliable Packet or Ping
	 * @param server Server Instance
	 */
	public static void sendReliablePacketAcknowledgement(@NotNull DatagramPacket packet, @NotNull Server server, @NotNull Statistics statistics) {
		// Received Packet Port and Address
		InetAddress address = packet.getAddress();
		int port = packet.getPort();

		// Get Packet Info
		int length = packet.getLength();
		byte[] buffer = packet.getData();

	    // Verify Packet Length
	    if (length < 3) {
		    byte[] exception = ClosePacket.closeWithMessage(Main.getTranslation("nonce_wrong_size"));

		    // Packet to Send Back to Client
		    packet = server.createSendPacket(address, port, exception.length, exception);

		    // Send Reply Back
		    server.sendPacket(packet);

		    // Return
		    return;
	    }

		// Get Nonce
		byte[] nonce = new byte[] {buffer[1], buffer[2]};

		// Nonce is Big Endian
		// LogHelper.printLine(String.format(Main.getTranslation("nonce_value"), PacketHelper.getUnsignedShortBE(nonce)));

		// Get Acknowledgement
		byte[] acknowledgement;
		try {
			acknowledgement = getAcknowledgement(nonce);
		} catch (InvalidBytesException exception) {
			LogHelper.printLineErr(exception.getMessage());

			acknowledgement = ClosePacket.closeWithMessage(Main.getTranslation("server_side_exception"));
		}

		// Packet to Send Back to Client
		packet = server.createSendPacket(address, port, acknowledgement.length, acknowledgement);

		// Log Sent Acknowledgement Packet
		statistics.logAcknowledgementSent(packet.getData());

		// Send Reply Back
		server.sendPacket(packet);
	}
}
