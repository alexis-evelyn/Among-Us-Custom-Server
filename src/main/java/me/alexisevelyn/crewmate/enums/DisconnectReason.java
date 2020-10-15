package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum DisconnectReason {
	// We Need An Outdated Server Reason

	NONE((byte) 0x00),
	GAME_FULL((byte) 0x01),
	GAME_STARTED((byte) 0x02),
	GAME_NOT_FOUND((byte) 0x03),
	LEGACY_CUSTOM((byte) 0x04),
	OUTDATED_CLIENT((byte) 0x05),
	BANNED_FROM_ROOM((byte) 0x06),
	KICKED_FROM_ROOM((byte) 0x07),
	CUSTOM((byte) 0x08),
	INVALID_USERNAME((byte) 0x09),
	HACKING((byte) 0x0A),
	FORCE_NO_REASON((byte) 0x10),
	BAD_CONNECTION((byte) 0x11),
	GAME_NOT_FOUND_TWO((byte) 0x12), // Wdym there's two Game not Found Bytes? https://wiki.weewoo.net/wiki/Protocol#9_-_Disconnect
	ROOM_CLOSED((byte) 0x13),
	SERVER_OVERLOADED((byte) 0x14);

	private final byte reason;

	DisconnectReason(byte reason) {
		this.reason = reason;
	}

	private static final java.util.Map<Byte, DisconnectReason> reasonSearch = new HashMap<>();

	public byte getReason() {
		return this.reason;
	}

	public static DisconnectReason getReason(byte reasonByte) {
		return reasonSearch.get(reasonByte);
	}

	public static String getReasonName(DisconnectReason map) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (map) {
			case NONE:
				return translation.getString("none");
			case GAME_FULL:
				return translation.getString("game_full");
			case GAME_STARTED:
				return translation.getString("game_started");
			case GAME_NOT_FOUND:
				return translation.getString("game_not_found");
			case LEGACY_CUSTOM:
				return translation.getString("legacy_custom");
			case OUTDATED_CLIENT:
				return translation.getString("outdated_client");
			case BANNED_FROM_ROOM:
				return translation.getString("banned_from_room");
			case KICKED_FROM_ROOM:
				return translation.getString("kicked_from_room");
			case CUSTOM:
				return translation.getString("custom");
			case INVALID_USERNAME:
				return translation.getString("invalid_username");
			case HACKING:
				return translation.getString("hacking");
			case FORCE_NO_REASON:
				return translation.getString("force_no_reason");
			case BAD_CONNECTION:
				return translation.getString("bad_connection");
			case GAME_NOT_FOUND_TWO:
				return translation.getString("game_not_found_two");
			case ROOM_CLOSED:
				return translation.getString("room_closed");
			case SERVER_OVERLOADED:
				return translation.getString("server_overloaded");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (DisconnectReason reasonKey : DisconnectReason.values()) {
			reasonSearch.put(reasonKey.reason, reasonKey);
		}
	}
}
