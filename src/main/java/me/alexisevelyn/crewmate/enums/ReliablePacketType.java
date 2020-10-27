package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum ReliablePacketType {
	// https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets
	HOST_SETTINGS(0x00),
	JOIN_LOBBY(0x01),
	START_GAME(0x02),
	REMOVE_GAME(0x03),
	REMOVE_PLAYER(0x04),

	GAME_DATA(0x05),
	GAME_DATA_TO(0x06),

	JOINED_GAME(0x07),
	ALTER_GAME(0x0a),
	REDIRECT_GAME(0x0d),

	SEARCH_PUBLIC_GAME(0x10);

	private final int reliablePacketType;

	ReliablePacketType(int reliablePacketType) {
		this.reliablePacketType = reliablePacketType;
	}

	private static final java.util.Map<Integer, ReliablePacketType> reliablePacketTypeSearch = new HashMap<>();

	public int getReliablePacketType() {
		return this.reliablePacketType;
	}

	@Nullable
	public static ReliablePacketType getReliablePacketType(int reliablePacketTypeInteger) {
		return reliablePacketTypeSearch.get(reliablePacketTypeInteger);
	}

	@NotNull
	public static String getReliablePacketTypeName(@NotNull ReliablePacketType reliablePacketType) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (reliablePacketType) {
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
		for (ReliablePacketType reliablePacketTypeKey : ReliablePacketType.values()) {
			reliablePacketTypeSearch.put(reliablePacketTypeKey.reliablePacketType, reliablePacketTypeKey);
		}
	}
}
