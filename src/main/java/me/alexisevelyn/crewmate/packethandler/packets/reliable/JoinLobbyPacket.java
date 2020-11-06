package me.alexisevelyn.crewmate.packethandler.packets.reliable;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.GamePacketType;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.MapSearch;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinLobbyEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;

public class JoinLobbyPacket {
	/**
	 * This is called no matter if the client joins from hosting a game or joining as a client.
	 *
	 * <br><br>
	 * It <em>really</em> doesn't need to be performed on hosting the game, but that's how the client works, so I'm taking advantage of it.
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param payload Data to be parsed
	 * @return data to send back to client
	 */
	@NotNull
	public static byte[] handleJoinLobby(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payload) {
		PlayerJoinLobbyEvent event = new PlayerJoinLobbyEvent(clientAddress, clientPort);
		event.call(server);

		try {
			if (!event.isCancelled()) {
				String gamecode = addClientToLobby(server, clientAddress, clientPort, payload);

				// TODO: Debug
				//LogHelper.printLine("DEBUG: Joined Lobby Bytes");
				byte[] reply = generateJoinLobbyReply(gamecode);
				//LogHelper.printPacketBytes(reply.length, reply);

				return reply;
			} else {
				return ClosePacket.closeWithMessage(event.getReason());
			}
		} catch (InvalidGameCodeException | InvalidBytesException exception) {
			// LogHelper.printLineErr(exception.getMessage());
			// exception.printStackTrace();

			return ClosePacket.closeWithMessage(Main.getTranslationBundle().getString("gamecode_invalid_code_exception"));
		}
	}

	/**
	 * Adds Client to Lobby.
	 *
	 * Not supposed to return gamecode or null. Supposed to be void.
	 *
	 * @param server Server Instance
	 * @param clientAddress Client's IP Address
	 * @param clientPort Client's Port
	 * @param payload payload bytes
	 */
	@Deprecated
	private static String addClientToLobby(@NotNull Server server, @NotNull InetAddress clientAddress, int clientPort, @NotNull byte... payload) throws InvalidGameCodeException {
		// 00 01 02 03 04
		// --------------
		// A2 26 8E 83 07
		// GC GC GC GC OM
		// GC = Game Code (LE INT-32)
		// OM = Owned Maps Bitfield (0x07 For Skeld, Mira, and Polus)

		// Validate Size of payload
		if (payload.length != 5)
			throw new InvalidBytesException(Main.getTranslationBundle().getString("join_game_invalid_size"));

		byte[] gameCodeBytes = new byte[] {payload[0], payload[1], payload[2], payload[3]};
		Map[] maps = MapSearch.getMapArray(payload[4]); // Client Owned Maps

		String gameCode = GameCodeHelper.parseGameCode(gameCodeBytes);

		// Add Player to Game
		PlayerJoinEvent event = new PlayerJoinEvent(gameCode, clientAddress, clientPort);
		event.call(server);

		// TODO: Decide what to do with this
		if (event.isCancelled())
			return null; // return ClosePacket.closeWithMessage(event.getReason());

		GameManager.getGameByCode(gameCode).addPlayer(PlayerManager.getPlayerByAddress(clientAddress, clientPort));
		return gameCode;
	}

	private static byte[] generateJoinLobbyReply(String gameCode) throws InvalidGameCodeException {
		// 00 01 02 03 04 05 06 07 08 09 0a 0b 0c 0d 0e 0f 10 11 12 13 14 15 16 17 18 19 1a 1b
		// -----------------------------------------------------------------------------------
		// 01 00 02 0d 00 07 fd 65 04 80 9f e4 04 00 9f e4 04 00 00
		// 01 00 01 10 00 07 32 3a 43 80 92 cd 04 00 86 cd 04 00 01 86 9b 13
		// -----------------------------------------------------------------------------------
		// RP NO NO PL PL RC GC GC GC GC CI CI CI CI HI HI HI HI OC P1 P1 P1 P1 P2 P2 P2 P2 UK
		// RP = Reliable Packet (1)
		// NO = Nonce (2 or 5)
		// PL = Packet Length (13 or 16)
		// RC = Reliable Packet Type (0x07 for Joined Game) - https://wiki.weewoo.net/wiki/Protocol#7_-_Joined_Game
		// GC = Game Code (0xfd 0x65 0x04 0x80 For JCBDQQ or 0x32 0x3a 0x43 0x80 for KBGSLQ)
		// CI = Client ID (UInt-32 LE - 320671 or 314770)
		// HI = Host ID (UInt-32 LE - 320671 or 314758)
		// OC = Other Clients (Count - 0 or 1)
		// P1 = Other Client ID (Packed Int - none or 314758)
		// P2 = Other Client ID (Packed Int - none)
		// UK = Unknown

		// TODO: Cannot already be used
		byte[] nonce = new byte[] {(byte) 0x02, (byte) 0x01};
		byte[] gameCodeBytes = GameCodeHelper.generateGameCodeBytes(gameCode);

		int clientIDInt = 12346;
		int hostIDInt = clientIDInt;
		byte[] clientID = PacketHelper.convertIntToLE(clientIDInt); // It's just a UInt-32 LE
		byte[] hostID = PacketHelper.convertIntToLE(hostIDInt); // It's just a UInt-32 LE

		byte[] packedClients = new byte[0]; // PacketHelper.packInteger(hostIDInt);
		byte[] clientCount = new byte[] {0x00}; // 0x01

		byte[] packetLength = PacketHelper.convertShortToLE((short) (gameCodeBytes.length + clientID.length + hostID.length + clientCount.length + packedClients.length)); // Length (Basically Where Other Clients Count Is)

		// Game Code - TVJUXQ (0c:0e:1b:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 0c 0e 1b 80 07                  ...........
		//
		// S->C - 0000   01 00 02 0d 00 07 0c 0e 1b 80 94 04 02 00 94 04   ................
		// S->C - 0010   02 00 00 06 00 0a 0c 0e 1b 80 01 00               ............

		return PacketHelper.mergeBytes(new byte[] {SendOption.RELIABLE.getByte()},
				nonce,
				packetLength, // Length (Basically Where Other Clients Count Is)
				new byte[] {(byte) GamePacketType.JOINED_GAME.getReliablePacketType()},
				gameCodeBytes, // TODO: Retrieve Game Assigned To Player From GameManager or Wherever
				clientID,
				hostID,
				clientCount,
				packedClients
		);
	}
}
