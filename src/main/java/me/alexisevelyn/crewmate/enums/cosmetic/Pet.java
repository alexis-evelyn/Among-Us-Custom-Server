package me.alexisevelyn.crewmate.enums.cosmetic;

import me.alexisevelyn.crewmate.Main;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Pet {
	// TODO: Confirm These Are The Correct Pet Bytes
	NONE(0),
	ALIEN(1),
	CREWMATE(2),
	DOG(3),
	STICKMIN(4),
	HAMSTER(5),
	ROBOT(6),
	UFO(7),
	ELLIE(8),
	SQUIG(9),
	BEDCRAB(10);

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

		if (pet == null)
			return translation.getString("unknown");

		switch (pet) {
			case NONE:
				return translation.getString("pet_none");
			case ALIEN:
				return translation.getString("pet_alien");
			case CREWMATE:
				return translation.getString("pet_crewmate");
			case DOG:
				return translation.getString("pet_dog");
			case STICKMIN:
				return translation.getString("pet_stickmin");
			case HAMSTER:
				return translation.getString("pet_hamster");
			case ROBOT:
				return translation.getString("pet_robot");
			case UFO:
				return translation.getString("pet_ufo");
			case ELLIE:
				return translation.getString("pet_ellie");
			case SQUIG:
				return translation.getString("pet_squig");
			case BEDCRAB:
				return translation.getString("pet_bedcrab");
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