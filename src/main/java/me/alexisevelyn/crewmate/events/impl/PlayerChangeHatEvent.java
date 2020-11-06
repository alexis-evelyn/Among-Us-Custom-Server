package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.events.EventCancellable;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeHatEvent extends EventCancellable {
    private Hat hat;

    public PlayerChangeHatEvent(@NotNull Hat hat) {
        this.hat = hat;
    }

    @NotNull
    public Hat getHat() {
        return hat;
    }

    public void setHat(@NotNull Hat hat) {
        this.hat = hat;
    }
}
