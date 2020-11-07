package me.alexisevelyn.crewmate.enums.cosmetic;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Skin {
	NONE((byte) 0),
	ASTRONAUT((byte) 1),
	CAPTAIN((byte) 2),
	MECHANIC((byte) 3),
	MILITARY((byte) 4),
	POLICE((byte) 5),
	SCIENTIST((byte) 6),
	SUIT_BLACK((byte) 7),
	SUIT_WHITE((byte) 8),
	THE_WALL((byte) 9),
	HAZMAT((byte) 10),
	SECURITY((byte) 11),
	TARMAC((byte) 12),
	MINER((byte) 13),
	WINTER((byte) 14),
	ARCHAEOLOGIST((byte) 15);

	private final byte skin;
	private static final java.util.Map<Byte, Skin> skinSearch = new HashMap<>();

	Skin(byte skin) {
		this.skin = skin;
	}

	public byte getByte() {
		return this.skin;
	}

	@Nullable
	public static Skin getSkin(byte skinByte) {
		return skinSearch.get(skinByte);
	}

	@NotNull
	public static String getSkinName(@NotNull Skin skin) {
		switch (skin) {
			case NONE:
				return Main.getTranslation("skin_none");
			case ASTRONAUT:
				return Main.getTranslation("skin_astronaut");
			case CAPTAIN:
				return Main.getTranslation("skin_captain");
			case MECHANIC:
				return Main.getTranslation("skin_mechanic");
			case MILITARY:
				return Main.getTranslation("skin_military");
			case POLICE:
				return Main.getTranslation("skin_police");
			case SCIENTIST:
				return Main.getTranslation("skin_scientist");
			case SUIT_BLACK:
				return Main.getTranslation("skin_suit_black");
			case SUIT_WHITE:
				return Main.getTranslation("skin_suit_white");
			case THE_WALL:
				return Main.getTranslation("skin_the_wall");
			case HAZMAT:
				return Main.getTranslation("skin_hazmat");
			case SECURITY:
				return Main.getTranslation("skin_security");
			case TARMAC:
				return Main.getTranslation("skin_tarmac");
			case MINER:
				return Main.getTranslation("skin_miner");
			case WINTER:
				return Main.getTranslation("skin_winter");
			case ARCHAEOLOGIST:
				return Main.getTranslation("skin_archaeologist");
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (Skin skinKey : Skin.values()) {
			skinSearch.put(skinKey.skin, skinKey);
		}
	}
}
