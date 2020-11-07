package me.alexisevelyn.crewmate.packethandler.packets.unreliable;

import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import org.jetbrains.annotations.NotNull;

import java.math.BigInteger;
import java.net.DatagramPacket;
import java.net.InetSocketAddress;
import java.util.Arrays;

public class MasterListPacket {
	/**
	 * Returns a fake masters list to send to client.
	 * WARNING: This function produces a broken packet.
	 *
	 * TODO: Checkout https://amongus-debugger.vercel.app/
	 *
	 * @param packet Packet From Client
	 * @param server Instance of Server
	 * @return byte array of masters list to send back to client
	 */
	@Deprecated
	@NotNull
	public static byte[] getFakeMastersList(DatagramPacket packet, Server server) {
		// TODO: Figure out how to fix this!!!

		// https://github.com/alexis-evelyn/Among-Us-Protocol/wiki/Master-Servers-List
		// https://gist.github.com/codyphobe/cce98bfc9221a00f7d1c8fede5e87f9c

		// TODO: Check if InetSocketAddress
		InetSocketAddress queriedIP = (InetSocketAddress) packet.getSocketAddress();

		// Convert Port to Little Endian Bytes
		short port = 22023; // TODO: Figure out how to extract port number from packet
		byte[] encodedPort = PacketHelper.convertShortToLE(port);

		String fakeMasterName = "Pseudo-Master-1";
		int numberOfMasters = 1;

		// LogHelper.printLine("Queried IP: " + queriedIP.getAddress());
		// LogHelper.printLine("Queried Port: " + port);

		// LogHelper.printLine("Encoded IP: " + Arrays.toString(queriedIP.getAddress().getAddress()));
		LogHelper.printLine(String.format(Main.getTranslation("encoded_port_logged"), Arrays.toString(encodedPort)));

		// Convert Player Count to Little Endian Bytes
		short playerCount = 257;
		byte[] playerCountBytes = PacketHelper.convertShortToLE(playerCount);

		// LogHelper.printLine("Player Count: " + Arrays.toString(playerCountBytes));

		byte[] endMessage = new byte[] {encodedPort[0], encodedPort[1], playerCountBytes[0], playerCountBytes[1]}; // Another Unknown if Not Last Master in List

		byte[] message = PacketHelper.mergeBytes(fakeMasterName.getBytes(), queriedIP.getAddress().getAddress(), endMessage);

		// 00 38 00 0e 01 02 18
		// The + 4 from (message.length + 4) comes from starting at (byte) numberOfMasters
//		LogHelper.printLine("Message Length: " + Arrays.toString(BigInteger.valueOf(255 + 4).toByteArray()));
//		LogHelper.printLine("Message Length Reversed: " + Arrays.toString(BigInteger.valueOf(Integer.reverseBytes(255 + 4)).toByteArray()));

		byte[] messageLength = BigInteger.valueOf(Integer.reverseBytes(message.length + 5)).toByteArray();
		byte[] masterBytesLength = BigInteger.valueOf(Integer.reverseBytes(fakeMasterName.getBytes().length + 5)).toByteArray();
		byte[] header = new byte[] {SendOption.NONE.getByte(), messageLength[0], messageLength[1], MasterBytes.FLAG.getMasterByte(), MasterBytes.UNKNOWN.getMasterByte(), (byte) numberOfMasters, masterBytesLength[0], masterBytesLength[1], MasterBytes.UNKNOWN_FLAG_TEMP.getMasterByte(), (byte) fakeMasterName.getBytes().length};

		return PacketHelper.mergeBytes(header, message);
	}

	private enum MasterBytes {
		FLAG((byte) 0x0e),
		UNKNOWN((byte) 0x01),
		UNKNOWN_FLAG_TEMP((byte) 0x00);

		private final byte masterByte;

		MasterBytes(byte masterByte) {
			this.masterByte = masterByte;
		}

		public byte getMasterByte() {
			return this.masterByte;
		}
	}
}
