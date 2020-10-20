package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.events.EventCancellable;

public class PlayerChangePetEvent extends EventCancellable {

    private final Pet pet;

    public PlayerChangePetEvent(Pet pet) {
        this.pet = pet;
    }

    public Pet getPet() {
        return pet;
    }

}
