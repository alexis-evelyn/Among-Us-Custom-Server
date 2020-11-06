package me.alexisevelyn.crewmate.events;

import me.alexisevelyn.crewmate.Server;
import org.jetbrains.annotations.NotNull;

public class Event {
    public boolean call(@NotNull Server server) {
        server.getEventBus().post(this);
        return true;
    }
}
