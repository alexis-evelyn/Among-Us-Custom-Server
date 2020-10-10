package me.alexisevelyn.crewmate.enums;

public enum Map {
	SKELD(0),
	MIRA_HQ(1),
	POLUS(2);

	private final int map;

	Map(int map) {
		this.map = map;
	}

	public int getMap() {
		return this.map;
	}
}