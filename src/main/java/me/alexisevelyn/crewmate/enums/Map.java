package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Map {
	SKELD(0),
	MIRA_HQ(1),
	POLUS(2);

	private final int map;

	Map(int map) {
		this.map = map;
	}

	private static final java.util.Map<Integer, Map> mapSearch = new HashMap<>();

	public int getMap() {
		return this.map;
	}

	public static Map getMap(int mapInteger) {
		return mapSearch.get(mapInteger);
	}

	public static String getMapName(Map map) {
		ResourceBundle translation = Main.getTranslationBundle();
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