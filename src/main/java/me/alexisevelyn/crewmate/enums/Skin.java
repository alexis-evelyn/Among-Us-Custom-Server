package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Skin {
	NONE(0),
	ASTRONAUT(1),
	CAPTAIN(2),
	MECHANIC(3),
	MILITARY(4),
	POLICE(5),
	SCIENTIST(6),
	SUIT_BLACK(7),
	SUIT_WHITE(8),
	THE_WALL(9),
	HAZMAT(10),
	SECURITY(11),
	TARMAC(12),
	MINER(13),
	WINTER(14),
	ARCHAEOLOGIST(15);

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
			case NONE:
				return Main.getTranslationBundle().getString("skin_none");
			case ASTRONAUT:
				return Main.getTranslationBundle().getString("skin_astronaut");
			case CAPTAIN:
				return Main.getTranslationBundle().getString("skin_captain");
			case MECHANIC:
				return Main.getTranslationBundle().getString("skin_mechanic");
			case MILITARY:
				return Main.getTranslationBundle().getString("skin_military");
			case POLICE:
				return Main.getTranslationBundle().getString("skin_police");
			case SCIENTIST:
				return Main.getTranslationBundle().getString("skin_scientist");
			case SUIT_BLACK:
				return Main.getTranslationBundle().getString("skin_suit_black");
			case SUIT_WHITE:
				return Main.getTranslationBundle().getString("skin_suit_white");
			case THE_WALL:
				return Main.getTranslationBundle().getString("skin_the_wall");
			case HAZMAT:
				return Main.getTranslationBundle().getString("skin_hazmat");
			case SECURITY:
				return Main.getTranslationBundle().getString("skin_security");
			case TARMAC:
				return Main.getTranslationBundle().getString("skin_tarmac");
			case MINER:
				return Main.getTranslationBundle().getString("skin_miner");
			case WINTER:
				return Main.getTranslationBundle().getString("skin_winter");
			case ARCHAEOLOGIST:
				return Main.getTranslationBundle().getString("skin_archaeologist");
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
