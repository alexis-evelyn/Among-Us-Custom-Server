package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.events.Event;

public class PlayerChatEvent extends Event {

    private final int playerId;
    private final String message;

    public PlayerChatEvent(int playerId, String message) {
        this.playerId = playerId;
        this.message = message;
    }

    public int getPlayerId() {
        return playerId;
    }

    public String getMessage() {
        return message;
    }

}
