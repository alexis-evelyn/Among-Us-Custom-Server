package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Hat;
import me.alexisevelyn.crewmate.events.EventCancellable;

public class PlayerChangeHatEvent extends EventCancellable {
    private Hat hat;

    public PlayerChangeHatEvent(Hat hat) {
        this.hat = hat;
    }

    public Hat getHat() {
        return hat;
    }

    public void setHat(Hat hat) {
        this.hat = hat;
    }
}
