package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Pet;
import me.alexisevelyn.crewmate.events.EventCancellable;
import org.jetbrains.annotations.NotNull;

public class PlayerChangePetEvent extends EventCancellable {
    private Pet pet;

    public PlayerChangePetEvent(@NotNull Pet pet) {
        this.pet = pet;
    }

    @NotNull
    public Pet getPet() {
        return pet;
    }

    public void setPet(@NotNull Pet pet) {
        this.pet = pet;
    }
}
