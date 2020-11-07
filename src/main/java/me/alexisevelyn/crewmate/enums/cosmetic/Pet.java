package me.alexisevelyn.crewmate.enums.cosmetic;

import me.alexisevelyn.crewmate.Main;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.ResourceBundle;

public enum Pet {
	// TODO: Confirm These Are The Correct Pet Bytes
	NONE((byte) 0),
	ALIEN((byte) 1),
	CREWMATE((byte) 2),
	DOG((byte) 3),
	STICKMIN((byte) 4),
	HAMSTER((byte) 5),
	ROBOT((byte) 6),
	UFO((byte) 7),
	ELLIE((byte) 8),
	SQUIG((byte) 9),
	BEDCRAB((byte) 10);

	private final byte pet;
	private static final java.util.Map<Byte, Pet> petSearch = new HashMap<>();

	Pet(byte pet) {
		this.pet = pet;
	}

	public byte getByte() {
		return this.pet;
	}

	@Nullable
	public static Pet getPet(byte petByte) {
		return petSearch.get(petByte);
	}

	@NotNull
	public static String getPetName(@NotNull Pet pet) {
		switch (pet) {
			case NONE:
				return Main.getTranslation("pet_none");
			case ALIEN:
				return Main.getTranslation("pet_alien");
			case CREWMATE:
				return Main.getTranslation("pet_crewmate");
			case DOG:
				return Main.getTranslation("pet_dog");
			case STICKMIN:
				return Main.getTranslation("pet_stickmin");
			case HAMSTER:
				return Main.getTranslation("pet_hamster");
			case ROBOT:
				return Main.getTranslation("pet_robot");
			case UFO:
				return Main.getTranslation("pet_ufo");
			case ELLIE:
				return Main.getTranslation("pet_ellie");
			case SQUIG:
				return Main.getTranslation("pet_squig");
			case BEDCRAB:
				return Main.getTranslation("pet_bedcrab");
			default:
				return Main.getTranslation("unknown");
		}
	}

	static {
		for (Pet petKey : Pet.values()) {
			petSearch.put(petKey.pet, petKey);
		}
	}
}