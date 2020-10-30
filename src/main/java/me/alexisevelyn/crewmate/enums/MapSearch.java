package me.alexisevelyn.crewmate.enums;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;

public enum MapSearch {
	SKELD((byte)   0b0), // 0
	MIRA_HQ((byte) 0b1), // 1
	POLUS((byte)   0b10), // 4

	// These are deprecated, because I will use the map's proper name on map release.
	@Deprecated STICKMIN((byte)  0b100), // 8
	@Deprecated MAP_FIVE((byte)  0b1000), // 16
	@Deprecated MAP_SIX((byte)   0b10000), // 32
	@Deprecated MAP_SEVEN((byte) 0b100000), // 64
	@Deprecated MAP_EIGHT((byte) 0b1000000), // 128
	@Deprecated MAP_NINE((byte)  0b10000000); // 256

	private final byte map;

	MapSearch(byte map) {
		this.map = map;
	}

	private static final java.util.Map<Byte, MapSearch> mapSearch = new HashMap<>();

	public byte getByte() {
		return this.map;
	}

	@Nullable
	public static MapSearch getMap(byte mapByte) {
		return mapSearch.get(mapByte);
	}

	static {
		for (MapSearch mapKey : MapSearch.values()) {
			mapSearch.put(mapKey.map, mapKey);
		}
	}
}