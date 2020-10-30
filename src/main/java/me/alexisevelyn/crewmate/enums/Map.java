package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Map {
	SKELD((byte) 0),
	MIRA_HQ((byte) 1),
	POLUS((byte) 2),

	// These are deprecated, because I will use the map's proper name on map release.
	@Deprecated STICKMIN((byte) 3),
	@Deprecated MAP_FIVE((byte) 4),
	@Deprecated MAP_SIX((byte) 5),
	@Deprecated MAP_SEVEN((byte) 6),
	@Deprecated MAP_EIGHT((byte) 7),
	@Deprecated MAP_NINE((byte) 8);

	private final byte map;

	Map(byte map) {
		this.map = map;
	}

	private static final java.util.Map<Byte, Map> mapSearch = new HashMap<>();

	public byte getByte() {
		return this.map;
	}

	@Nullable
	public static Map getMap(byte mapByte) {
		return mapSearch.get(mapByte);
	}

	@NotNull
	public static String getMapName(@NotNull Map map) {
		ResourceBundle translation = Main.getTranslationBundle();

		switch (map) {
			case SKELD:
				return translation.getString("skeld");
			case MIRA_HQ:
				return translation.getString("mira_hq");
			case POLUS:
				return translation.getString("polus");
			case STICKMIN:
				return translation.getString("stickmin");
			case MAP_FIVE:
				return translation.getString("map_five");
			case MAP_SIX:
				return translation.getString("map_six");
			case MAP_SEVEN:
				return translation.getString("map_seven");
			case MAP_EIGHT:
				return translation.getString("map_eight");
			case MAP_NINE:
				return translation.getString("map_nine");
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Map mapKey : Map.values()) {
			mapSearch.put(mapKey.map, mapKey);
		}
	}
}