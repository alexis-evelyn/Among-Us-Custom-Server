package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.EventCancellable;

public class PlayerChangeSkinEvent extends EventCancellable {
    private final Skin skin;

    public PlayerChangeSkinEvent(Skin skin) {
        this.skin = skin;
    }

    public Skin getSkin() {
        return skin;
    }
}
