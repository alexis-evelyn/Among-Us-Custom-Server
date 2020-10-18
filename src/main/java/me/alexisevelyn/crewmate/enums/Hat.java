package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Hat {
	;

	private final int hat;

	Hat(int hat) {
		this.hat = hat;
	}

	private static final java.util.Map<Integer, Hat> hatSearch = new HashMap<>();

	public int getHat() {
		return this.hat;
	}

	public static Hat getHat(int hatInteger) {
		return hatSearch.get(hatInteger);
	}

	public static String getHatName(Hat hat) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (hat) {
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Hat hatKey : Hat.values()) {
			hatSearch.put(hatKey.hat, hatKey);
		}
	}
}
