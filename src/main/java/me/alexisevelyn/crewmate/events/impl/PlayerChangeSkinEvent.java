package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.cosmetic.Skin;
import me.alexisevelyn.crewmate.events.EventCancellable;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeSkinEvent extends EventCancellable {
    private Skin skin;

    public PlayerChangeSkinEvent(@NotNull Skin skin) {
        this.skin = skin;
    }

    @NotNull
    public Skin getSkin() {
        return skin;
    }

    public void setSkin(@NotNull Skin skin) {
        this.skin = skin;
    }
}
