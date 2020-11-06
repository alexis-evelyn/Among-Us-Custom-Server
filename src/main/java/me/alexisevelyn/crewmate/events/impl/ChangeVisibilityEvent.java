package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.Server;
import me.alexisevelyn.crewmate.api.Player;
import me.alexisevelyn.crewmate.events.EventCancellable;

public class ChangeVisibilityEvent extends EventCancellable {
    private final Server server;
    private final Player player;
    private boolean visible;

    public ChangeVisibilityEvent(Server server, Player player, boolean visible) {
        this.server = server;
        this.player = player;
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public Server getServer() {
        return server;
    }

    public Player getPlayer() {
        return player;
    }
}
