package me.alexisevelyn.crewmate.handlers.gamepacket;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.PacketHelper;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;

import java.net.DatagramPacket;

public class JoinGame {
	public static byte[] handleJoinPrivateGame(DatagramPacket packet) {
		// 0000   01 00 02 05 00 01 a3 3e e4 9b 07                  .......>...
		// 0000   01 00 02 05 00 01 37 ce 6d 97 07                  ......7.m..

		if (packet.getLength() != 11)
			return new byte[0];

		byte[] buffer = packet.getData();
		byte[] gameCodeBytes = new byte[4];

		// Extract Game Code Bytes From Buffer
		System.arraycopy(buffer, 6, gameCodeBytes, 0, 4);

		String gameCode;
		try {
			gameCode = GameCodeHelper.parseGameCode(gameCodeBytes);
		} catch (InvalidBytesException e) {
			System.out.println("Game Code Exception: " + e.getMessage());
			e.printStackTrace();

			return new byte[0];
		}

		System.out.println("Game Code: " + gameCode);

		return PacketHelper.closeWithMessage("Joining Games Is Not Supported Yet!!!");
	}
}
