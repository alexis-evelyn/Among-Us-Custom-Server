package me.alexisevelyn.crewmate.enums;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Pet {
	; // I Don't Have Pets To Test With (Will Lookup Enum Later)

	private final int pet;

	Pet(int pet) {
		this.pet = pet;
	}

	private static final java.util.Map<Integer, Pet> petSearch = new HashMap<>();

	public int getPet() {
		return this.pet;
	}

	public static Pet getPet(int hatInteger) {
		return petSearch.get(hatInteger);
	}

	public static String getPetName(Pet pet) {
		ResourceBundle translation = Main.getTranslationBundle();
		switch (pet) {
			default:
				return translation.getString("unknown");
		}
	}

	static {
		for (Pet petKey : Pet.values()) {
			petSearch.put(petKey.pet, petKey);
		}
	}
}