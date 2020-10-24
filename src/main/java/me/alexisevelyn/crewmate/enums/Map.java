package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Map {
	SKELD(0),
	MIRA_HQ(1),
	POLUS(2),

	// These are deprecated, because I will use the map's proper name on map release.
	@Deprecated STICKMIN(3),
	@Deprecated MAP_FIVE(4),
	@Deprecated MAP_SIX(5),
	@Deprecated MAP_SEVEN(6),
	@Deprecated MAP_EIGHT(7);

	private final int map;

	Map(int map) {
		this.map = map;
	}

	private static final java.util.Map<Integer, Map> mapSearch = new HashMap<>();

	public int getMap() {
		return this.map;
	}

	@Nullable
	public static Map getMap(int mapInteger) {
		return mapSearch.get(mapInteger);
	}

	@NotNull
	public static String getMapName(@NotNull Map map) {
		ResourceBundle translation = Main.getTranslationBundle();

		if (map == null)
			return translation.getString("unknown");

		switch (map) {
			case SKELD:
				return translation.getString("skeld");
			case MIRA_HQ:
				return translation.getString("mira_hq");
			case POLUS:
				return translation.getString("polus");
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