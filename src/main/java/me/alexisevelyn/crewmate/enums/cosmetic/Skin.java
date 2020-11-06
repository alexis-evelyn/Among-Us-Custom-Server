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
		ResourceBundle translation = Main.getTranslationBundle();

		switch (skin) {
			case NONE:
				return translation.getString("skin_none");
			case ASTRONAUT:
				return translation.getString("skin_astronaut");
			case CAPTAIN:
				return translation.getString("skin_captain");
			case MECHANIC:
				return translation.getString("skin_mechanic");
			case MILITARY:
				return translation.getString("skin_military");
			case POLICE:
				return translation.getString("skin_police");
			case SCIENTIST:
				return translation.getString("skin_scientist");
			case SUIT_BLACK:
				return translation.getString("skin_suit_black");
			case SUIT_WHITE:
				return translation.getString("skin_suit_white");
			case THE_WALL:
				return translation.getString("skin_the_wall");
			case HAZMAT:
				return translation.getString("skin_hazmat");
			case SECURITY:
				return translation.getString("skin_security");
			case TARMAC:
				return translation.getString("skin_tarmac");
			case MINER:
				return translation.getString("skin_miner");
			case WINTER:
				return translation.getString("skin_winter");
			case ARCHAEOLOGIST:
				return translation.getString("skin_archaeologist");
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
