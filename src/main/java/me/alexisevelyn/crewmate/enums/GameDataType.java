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
	private static final java.util.Map<Byte, GameDataType> gameDataTypeSearch = new HashMap<>();

	GameDataType(byte gameDataType) {
		this.gameDataType = gameDataType;
	}

	public byte getByte() {
		return this.gameDataType;
	}

	@Nullable
	public static GameDataType getGameDataType(byte gameDataType) {
		return gameDataTypeSearch.get(gameDataType);
	}

	@NotNull
	public static String getGameDataTypeName(@NotNull GameDataType gameDataType) {
		switch (gameDataType) {
			case COMPONENT_DATA:
				return Main.getTranslation("component_data");
			case RPC:
				return Main.getTranslation("rpc");
			case SPAWN:
				return Main.getTranslation("spawn");
			case DESPAWN:
				return Main.getTranslation("despawn");
			case SCENE_CHANGE:
				return Main.getTranslation("scene_change");
			case READY:
				return Main.getTranslation("ready");
			case CHANGE_SETTINGS:
				return Main.getTranslation("change_settings");
			case UNKNOWN:
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (GameDataType gameDataTypeKey : GameDataType.values()) {
			gameDataTypeSearch.put(gameDataTypeKey.gameDataType, gameDataTypeKey);
		}
	}
}