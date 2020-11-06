package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum GameDataType {
	// https://wiki.weewoo.net/wiki/Protocol#5.2C_6_-_Game_Data_and_Game_Data_To
	COMPONENT_DATA((byte) 0x01),
	RPC((byte) 0x02),
	UNKNOWN((byte) 0x03),
	SPAWN((byte) 0x04),
	DESPAWN((byte) 0x05),
	SCENE_CHANGE((byte) 0x06),
	READY((byte) 0x07),
	CHANGE_SETTINGS((byte) 0x08);

	private final byte gameDataType;

	GameDataType(byte gameDataType) {
		this.gameDataType = gameDataType;
	}

	private static final java.util.Map<Byte, GameDataType> gameDataTypeSearch = new HashMap<>();

	public byte getByte() {
		return this.gameDataType;
	}

	@Nullable
	public static GameDataType getGameDataType(byte gameDataType) {
		return gameDataTypeSearch.get(gameDataType);
	}

	@NotNull
	public static String getGameDataTypeName(@NotNull GameDataType gameDataType) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (gameDataType) {
			case COMPONENT_DATA:
				return translation.getString("component_data");
			case RPC:
				return translation.getString("rpc");
			case SPAWN:
				return translation.getString("spawn");
			case DESPAWN:
				return translation.getString("despawn");
			case SCENE_CHANGE:
				return translation.getString("scene_change");
			case READY:
				return translation.getString("ready");
			case CHANGE_SETTINGS:
				return translation.getString("change_settings");
			case UNKNOWN:
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (GameDataType gameDataTypeKey : GameDataType.values()) {
			gameDataTypeSearch.put(gameDataTypeKey.gameDataType, gameDataTypeKey);
		}
	}
}