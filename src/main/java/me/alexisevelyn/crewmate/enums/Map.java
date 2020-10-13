package me.alexisevelyn.crewmate.enums;

import java.util.HashMap;

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
		switch (map) {
			case SKELD:
				return "The Skeld";
			case MIRA_HQ:
				return "Mira HQ";
			case POLUS:
				return "Polus";
			default:
				return "Unknown";
		}
	}

	static {
		for (Map mapKey : Map.values()) {
			mapSearch.put(mapKey.map, mapKey);
		}
	}
}