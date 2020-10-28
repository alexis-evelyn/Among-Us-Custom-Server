package me.alexisevelyn.crewmate.packethandler.packets.reliable.gamedata;

import me.alexisevelyn.crewmate.GameCodeHelper;
import me.alexisevelyn.crewmate.LogHelper;
import me.alexisevelyn.crewmate.Main;
import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.enums.Map;
import me.alexisevelyn.crewmate.enums.ReliablePacketType;
import me.alexisevelyn.crewmate.enums.hazel.SendOption;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinEvent;
import me.alexisevelyn.crewmate.events.impl.PlayerJoinLobbyEvent;
import me.alexisevelyn.crewmate.exceptions.InvalidBytesException;
import me.alexisevelyn.crewmate.exceptions.InvalidGameCodeException;
import me.alexisevelyn.crewmate.handlers.GameManager;
import me.alexisevelyn.crewmate.handlers.PlayerManager;
import me.alexisevelyn.crewmate.handlers.gamepacket.SearchGame;
import me.alexisevelyn.crewmate.packethandler.PacketHelper;
import me.alexisevelyn.crewmate.packethandler.packets.ClosePacket;

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
	 * @return
	 * @throws InvalidGameCodeException Invalid Game Code Bytes Provided in payload
	 */
	public static byte[] handleJoinLobby(Server server, InetAddress clientAddress, int clientPort, byte... payload) throws InvalidGameCodeException {
		PlayerJoinLobbyEvent event = new PlayerJoinLobbyEvent(clientAddress, clientPort);
		event.call(server);

		if (!event.isCancelled()) {
			String gamecode = addClientToLobby(server, clientAddress, clientPort, payload);

			// TODO: Debug
			byte[] reply = generateJoinLobbyReply(gamecode);
			LogHelper.printPacketBytes(reply.length, reply);

			// Added Game: SPOONS
			// +-------------------------------------------------------------------------------------------------------------------------------------------------------+
			// | Positions | 0  | 1  | 2  | 3  | 4  | 5  | 6  | 7  | 8  | 9  | 10 | 11 | 12 | 13 | 14 | 15 | 16 | 17 | 18 | 19 | 20 | 21 | 22 | 23 | 24 | 25 | 26 | 27 |
			// | Bytes     | 01 | 00 | 01 | 16 | 00 | 07 | BF | 28 | A2 | 8A | 94 | 04 | 02 | 00 | 94 | 04 | 02 | 00 | 00 | 06 | 00 | 0A | 0C | 0E | 1B | 80 | 01 | 00 |
			// +-------------------------------------------------------------------------------------------------------------------------------------------------------+

			return reply;
		} else {
			return ClosePacket.closeWithMessage(event.getReason());
		}
	}

	/**
	 * Adds Client to Lobby.
	 *
	 * Not supposed to return gamecode or null. Supposed to be void.
	 *
	 * @param server
	 * @param clientAddress
	 * @param clientPort
	 * @param payload
	 * @throws InvalidGameCodeException
	 */
	@Deprecated
	private static String addClientToLobby(Server server, InetAddress clientAddress, int clientPort, byte... payload) throws InvalidGameCodeException, InvalidBytesException {
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
		Map[] maps = SearchGame.parseMapsSearch(payload[4]); // Client Owned Maps

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
		// 01 00 01 16 00 07 41 4c 45 58 94 04 02 00 94 04 02 00 00 06 00 0a 0c 0e 1b 80 01 00
		// -----------------------------------------------------------------------------------
		// RP NO NO PL PL RC GC GC GC GC CI CI CI CI HI HI HI HI OC UK UK UK UK UK UK UK UK UK
		// RP = Reliable Packet
		// NO = Nonce
		// PL = Packet Length
		// RC = Reliable Packet Type (0x07 for Joined Game) - https://wiki.weewoo.net/wiki/Protocol#7_-_Joined_Game
		// GC = Game Code (0x41 0x4c 0x45 0x58 For Alex)
		// CI = Client ID
		// HI = Host ID
		// OC = Other Clients (Count)
		// UK = Unknown

		byte[] nonce = new byte[] {(byte) 0x00, (byte) 0x01};

		byte[] clientID = new byte[] {(byte) 0x94, 0x04, 0x02, 0x00}; // 50462976 - Looks to just be adding the numbers together from left to right (according to https://amongus-debugger.vercel.app/)
		byte[] hostID = clientID; // Implement
		byte[] clientCount = new byte[] {0x00};

		// ???
		byte[] wtfKnows = new byte[] {0x06, 0x00, 0x0a, 0x0c, 0x0e, 0x1b, (byte) 0x80, 0x01, 0x00};

		byte[] packetLength = PacketHelper.convertShortToLE((short) (13)); // Length (Basically Where Other Clients Count Is)

		// Game Code - TVJUXQ (0c:0e:1b:80) - Red - Goggles - Private - 1/10 Players
		// C->S - 0000   01 00 03 05 00 01 0c 0e 1b 80 07                  ...........
		//
		// S->C - 0000   01 00 02 0d 00 07 0c 0e 1b 80 94 04 02 00 94 04   ................
		// S->C - 0010   02 00 00 06 00 0a 0c 0e 1b 80 01 00               ............

		return PacketHelper.mergeBytes(new byte[] {SendOption.RELIABLE.getSendOption()},
				nonce,
				packetLength, // Length (Basically Where Other Clients Count Is)
				new byte[] {(byte) ReliablePacketType.JOINED_GAME.getReliablePacketType()},
				GameCodeHelper.generateGameCodeBytes(gameCode), // TODO: Retrieve Game Assigned To Player From GameManager or Wherever
				clientID,
				hostID,
				clientCount
		);
	}
}
