package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Skin {
	;

	private final int skin;

	Skin(int skin) {
		this.skin = skin;
	}

	private static final java.util.Map<Integer, Skin> skinSearch = new HashMap<>();

	public int getSkin() {
		return this.skin;
	}

	public static Skin getSkin(int skinInteger) {
		return skinSearch.get(skinInteger);
	}

	public static String getSkinName(Skin skin) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (skin) {
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Skin skinKey : Skin.values()) {
			skinSearch.put(skinKey.skin, skinKey);
		}
	}
}
