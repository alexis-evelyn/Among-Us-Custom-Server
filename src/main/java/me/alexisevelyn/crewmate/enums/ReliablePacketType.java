package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum ReliablePacketType {
	// https://wiki.weewoo.net/wiki/Protocol#Reliable_Packets
	PRE_HOST_SETTINGS(0x00),
	JOIN_GAME(0x01),
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

	public static ReliablePacketType getReliablePacketType(int reliablePacketTypeInteger) {
		return reliablePacketTypeSearch.get(reliablePacketTypeInteger);
	}

	public static String getReliablePacketTypeName(ReliablePacketType reliablePacketType) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (reliablePacketType) {
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
