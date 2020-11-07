package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

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
		switch (playerColor) {
			case RED:
				return Main.getTranslation("color_red");
			case BLUE:
				return Main.getTranslation("color_blue");
			case DARK_GREEN:
				return Main.getTranslation("color_dark_green");
			case PINK:
				return Main.getTranslation("color_pink");
			case ORANGE:
				return Main.getTranslation("color_orange");
			case YELLOW:
				return Main.getTranslation("color_yellow");
			case DARK_GRAY:
				return Main.getTranslation("color_dark_gray");
			case WHITE:
				return Main.getTranslation("color_white");
			case PURPLE:
				return Main.getTranslation("color_purple");
			case BROWN:
				return Main.getTranslation("color_brown");
			case CYAN:
				return Main.getTranslation("color_cyan");
			case LIGHT_GREEN:
				return Main.getTranslation("color_light_green");
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (PlayerColor playerColorKey : PlayerColor.values()) {
			playerColorSearch.put(playerColorKey.playerColor, playerColorKey);
		}
	}
}
