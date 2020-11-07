package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

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
	private static final java.util.Map<Byte, DisconnectReason> reasonSearch = new HashMap<>();

	DisconnectReason(byte reason) {
		this.reason = reason;
	}

	public byte getByte() {
		return this.reason;
	}

	@Nullable
	public static DisconnectReason getReason(byte reasonByte) {
		return reasonSearch.get(reasonByte);
	}

	@NotNull
	public static String getReasonName(@NotNull DisconnectReason disconnectReason) {
		switch (disconnectReason) {
			case NONE:
				return Main.getTranslation("none");
			case GAME_FULL:
				return Main.getTranslation("game_full");
			case GAME_STARTED:
				return Main.getTranslation("game_started");
			case GAME_NOT_FOUND:
				return Main.getTranslation("game_not_found");
			case LEGACY_CUSTOM:
				return Main.getTranslation("legacy_custom");
			case OUTDATED_CLIENT:
				return Main.getTranslation("outdated_client");
			case BANNED_FROM_ROOM:
				return Main.getTranslation("banned_from_room");
			case KICKED_FROM_ROOM:
				return Main.getTranslation("kicked_from_room");
			case CUSTOM:
				return Main.getTranslation("custom");
			case INVALID_USERNAME:
				return Main.getTranslation("invalid_username");
			case HACKING:
				return Main.getTranslation("hacking");
			case FORCE_NO_REASON:
				return Main.getTranslation("force_no_reason");
			case BAD_CONNECTION:
				return Main.getTranslation("bad_connection");
			case GAME_NOT_FOUND_TWO:
				return Main.getTranslation("game_not_found_two");
			case ROOM_CLOSED:
				return Main.getTranslation("room_closed");
			case SERVER_OVERLOADED:
				return Main.getTranslation("server_overloaded");
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (DisconnectReason reasonKey : DisconnectReason.values()) {
			reasonSearch.put(reasonKey.reason, reasonKey);
		}
	}
}
