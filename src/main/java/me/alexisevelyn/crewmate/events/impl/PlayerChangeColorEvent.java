package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.enums.PlayerColor;
import me.alexisevelyn.crewmate.events.EventCancellable;

public class PlayerChangeColorEvent extends EventCancellable {

    private PlayerColor color;

    public PlayerChangeColorEvent(PlayerColor color) {
        this.color = color;
    }

    public PlayerColor getColor() {
        return color;
    }

    public void setColor(PlayerColor color) {
        this.color = color;
    }

}
