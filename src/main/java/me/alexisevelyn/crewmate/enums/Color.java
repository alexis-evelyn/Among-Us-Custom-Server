package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Color {
	;

	private final int color;

	Color(int color) {
		this.color = color;
	}

	private static final java.util.Map<Integer, Color> colorSearch = new HashMap<>();

	public int getColor() {
		return this.color;
	}

	public static Color getColor(int colorInteger) {
		return colorSearch.get(colorInteger);
	}

	public static String getColorName(Color color) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (color) {
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Color colorKey : Color.values()) {
			colorSearch.put(colorKey.color, colorKey);
		}
	}
}
