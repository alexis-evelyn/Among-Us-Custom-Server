package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum PlayerColor {
	RED((byte) 0),
	BLUE((byte) 1),
	DARK_GREEN((byte) 2),
	PINK((byte) 3),
	ORANGE((byte) 4),
	YELLOW((byte) 5),
	DARK_GRAY((byte) 6),
	WHITE((byte) 7),
	PURPLE((byte) 8),
	BROWN((byte) 9),
	CYAN((byte) 10),
	LIGHT_GREEN((byte) 11);

	private final byte playerColor;
	private static final java.util.Map<Byte, PlayerColor> playerColorSearch = new HashMap<>();

	PlayerColor(byte playerColor) {
		this.playerColor = playerColor;
	}

	@SuppressWarnings("unused")
	public byte getByte() {
		return this.playerColor;
	}

	@Nullable
	public static PlayerColor getColor(byte playerColorInteger) {
		return playerColorSearch.get(playerColorInteger);
	}

	@NotNull
	public static String getColorName(@NotNull PlayerColor playerColor) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (playerColor) {
			case RED:
				return translation.getString("color_red");
			case BLUE:
				return translation.getString("color_blue");
			case DARK_GREEN:
				return translation.getString("color_dark_green");
			case PINK:
				return translation.getString("color_pink");
			case ORANGE:
				return translation.getString("color_orange");
			case YELLOW:
				return translation.getString("color_yellow");
			case DARK_GRAY:
				return translation.getString("color_dark_gray");
			case WHITE:
				return translation.getString("color_white");
			case PURPLE:
				return translation.getString("color_purple");
			case BROWN:
				return translation.getString("color_brown");
			case CYAN:
				return translation.getString("color_cyan");
			case LIGHT_GREEN:
				return translation.getString("color_light_green");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (PlayerColor playerColorKey : PlayerColor.values()) {
			playerColorSearch.put(playerColorKey.playerColor, playerColorKey);
		}
	}
}
