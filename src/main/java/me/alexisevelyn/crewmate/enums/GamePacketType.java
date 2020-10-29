package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum GamePacketType {
	// https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets
	HOST_SETTINGS((byte) 0x00),
	JOIN_LOBBY((byte) 0x01),
	START_GAME((byte) 0x02),
	REMOVE_GAME((byte) 0x03),
	REMOVE_PLAYER((byte) 0x04),

	GAME_DATA((byte) 0x05),
	GAME_DATA_TO((byte) 0x06),

	JOINED_GAME((byte) 0x07),
	ALTER_GAME((byte) 0x0a),
	REDIRECT_GAME((byte) 0x0d),

	SEARCH_PUBLIC_GAME((byte) 0x10);

	private final byte gamePacketType;

	GamePacketType(byte gamePacketType) {
		this.gamePacketType = gamePacketType;
	}

	private static final java.util.Map<Byte, GamePacketType> reliablePacketTypeSearch = new HashMap<>();

	public int getReliablePacketType() {
		return this.gamePacketType;
	}

	@Nullable
	public static GamePacketType getByte(byte gamePacketType) {
		return reliablePacketTypeSearch.get(gamePacketType);
	}

	@NotNull
	public static String getReliablePacketTypeName(@NotNull GamePacketType gamePacketType) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (gamePacketType) {
			case HOST_SETTINGS:
				return translation.getString("reliable_packet_host_settings");
			case JOIN_LOBBY:
				return translation.getString("reliable_packet_join_lobby");
			case START_GAME:
				return translation.getString("reliable_packet_start_game");
			case REMOVE_GAME:
				return translation.getString("reliable_packet_remove_game");
			case REMOVE_PLAYER:
				return translation.getString("reliable_packet_remove_player");
			case GAME_DATA:
				return translation.getString("reliable_packet_game_data");
			case GAME_DATA_TO:
				return translation.getString("reliable_packet_game_data_to");
			case JOINED_GAME:
				return translation.getString("reliable_packet_joined_game");
			case ALTER_GAME:
				return translation.getString("reliable_packet_alter_game");
			case REDIRECT_GAME:
				return translation.getString("reliable_packet_redirect_game");
			case SEARCH_PUBLIC_GAME:
				return translation.getString("reliable_packet_search_public_game");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (GamePacketType gamePacketTypeKey : GamePacketType.values()) {
			reliablePacketTypeSearch.put((byte) gamePacketTypeKey.gamePacketType, gamePacketTypeKey);
		}
	}
}
