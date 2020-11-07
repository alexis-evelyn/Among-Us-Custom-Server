package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum Map {
	// Not A Map (-1 == 255)
	// It is unlikely that the map count will reach 127 (much less 255) before a protocol rewrite.
	// That means we don't know if the byte is supposed to be signed or not.
	// https://wiki.weewoo.net/wiki/Enums#Map
	UNSPECIFIED((byte) -1),

	// Existing Maps
	SKELD((byte) 0),
	MIRA_HQ((byte) 1),
	POLUS((byte) 2),

	// These are deprecated, because I will use the map's proper name on map release.
	@Deprecated STICKMIN((byte) 3),
	@Deprecated MAP_FIVE((byte) 4),
	@Deprecated MAP_SIX((byte) 5),
	@Deprecated MAP_SEVEN((byte) 6),
	@Deprecated MAP_EIGHT((byte) 7);

	private final byte map;
	private static final java.util.Map<Byte, Map> mapSearch = new HashMap<>();

	Map(byte map) {
		this.map = map;
	}

	public byte getByte() {
		return this.map;
	}

	@Nullable
	public static Map getMap(byte mapByte) {
		return mapSearch.get(mapByte);
	}

	@NotNull
	public static String getMapName(@NotNull Map map) {
		switch (map) {
			case SKELD:
				return Main.getTranslation("skeld");
			case MIRA_HQ:
				return Main.getTranslation("mira_hq");
			case POLUS:
				return Main.getTranslation("polus");
			case STICKMIN:
				return Main.getTranslation("stickmin");
			case MAP_FIVE:
				return Main.getTranslation("map_five");
			case MAP_SIX:
				return Main.getTranslation("map_six");
			case MAP_SEVEN:
				return Main.getTranslation("map_seven");
			case MAP_EIGHT:
				return Main.getTranslation("map_eight");
			case UNSPECIFIED:
				return Main.getTranslation("unspecified");
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (Map mapKey : Map.values()) {
			mapSearch.put(mapKey.map, mapKey);
		}
	}
}