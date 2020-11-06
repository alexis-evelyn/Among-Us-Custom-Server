package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.PlayerColor;
import me.alexisevelyn.crewmate.events.EventCancellable;
import org.jetbrains.annotations.NotNull;

public class PlayerChangeColorEvent extends EventCancellable {
    private PlayerColor color;

    public PlayerChangeColorEvent(@NotNull PlayerColor color) {
        this.color = color;
    }

    @NotNull
    public PlayerColor getColor() {
        return color;
    }

    public void setColor(@NotNull PlayerColor color) {
        this.color = color;
    }
}
