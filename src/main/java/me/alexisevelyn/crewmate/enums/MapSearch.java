package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;

public enum MapSearch {
	// Not A Map
	UNSPECIFIED((byte) 0b0), // 0

	// Existing Maps
	SKELD((byte)   0b1), // 1
	MIRA_HQ((byte) 0b10), // 2
	POLUS((byte)   0b100), // 4

	// These are deprecated, because I will use the map's proper name on map release.
	// Not Implemented Maps
	@Deprecated STICKMIN((byte)  0b1000), // 8
	@Deprecated MAP_FIVE((byte)  0b10000), // 16
	@Deprecated MAP_SIX((byte)   0b100000), // 32
	@Deprecated MAP_SEVEN((byte) 0b1000000), // 64
	@Deprecated MAP_EIGHT((byte) 0b10000000); // 128

	private final byte map;
	private static final java.util.Map<Byte, MapSearch> mapSearch = new HashMap<>();

	MapSearch(byte map) {
		this.map = map;
	}

	public byte getByte() {
		return this.map;
	}

	@Nullable
	public static MapSearch getMap(byte mapByte) {
		return mapSearch.get(mapByte);
	}

	/**
	 * Produce a human readable list of the map array
	 *
	 * @param maps Array of Maps
	 * @return String representation of the map array
	 */
	public static String getPrintableMapsList(@NotNull Map... maps) {
		// List of Maps Being Included In Search
		StringBuilder printableMapsList = new StringBuilder();

		// Append Delimiters (Usually Comma + Space) To List and Then Remove Last Delimiter
		String delimiter = Main.getTranslation("list_delimiter_logged");
		for (Map map : maps) {
			printableMapsList.append(Map.getMapName(map)).append(delimiter);
		}
		printableMapsList.delete(printableMapsList.length() - delimiter.length(), printableMapsList.length());

		return printableMapsList.toString();
	}

	/**
	 * Convert Map bitfield to Map[]
	 *
	 * @param mapByte byte to represent the map bitfield
	 * @return array of Maps
	 */
	@NotNull
	@SuppressWarnings({"PMD.ExcessiveMethodLength", "PMD.NPathComplexity"})
	public static Map[] getMapArray(byte mapByte) {
		// From what I'm hearing, this is a bitfield. https://discordapp.com/channels/750301084202958899/761731747762667560/765242112031064074
		// This function parses as a bitfield so if say 9 maps exist, then we don't have to have every possible map combination written in code.

		ArrayList<Map> maps = new ArrayList<>();
		// Not a Bitwise Operation
		if (UNSPECIFIED.getByte() == mapByte) {
			// Unspecified Map
			maps.add(Map.UNSPECIFIED);
		}

		if ((SKELD.getByte() & mapByte) > 0) {
			// Skeld
			maps.add(Map.SKELD);
		}

		if ((MIRA_HQ.getByte() & mapByte) > 0) {
			// Mira-HQ
			maps.add(Map.MIRA_HQ);
		}

		if ((POLUS.getByte() & mapByte) > 0) {
			// Polus
			maps.add(Map.POLUS);
		}

		// As of this writing, these maps have either not been released or created yet.
		// So, I could not tell you the official names of the maps (especially for the ones that simply don't exist).
		if ((STICKMIN.getByte() & mapByte) > 0) {
			// Stickmin Map
			maps.add(Map.STICKMIN);
		}

		if ((MAP_FIVE.getByte() & mapByte) > 0) {
			// Fifth Map
			maps.add(Map.MAP_FIVE);
		}

		if ((MAP_SIX.getByte() & mapByte) > 0) {
			// Sixth Map
			maps.add(Map.MAP_SIX);
		}

		if ((MAP_SEVEN.getByte() & mapByte) > 0) {
			// Seventh Map
			maps.add(Map.MAP_SEVEN);
		}

		if ((MAP_EIGHT.getByte() & mapByte) > 0) {
			// Eighth Map
			maps.add(Map.MAP_EIGHT);
		}

		// https://stackoverflow.com/a/5061692/6828099
		// Apparently it's supposed to be marked as empty now
		return maps.toArray(new Map[0]);
	}

	static {
		for (MapSearch mapKey : values()) {
			mapSearch.put(mapKey.map, mapKey);
		}
	}
}