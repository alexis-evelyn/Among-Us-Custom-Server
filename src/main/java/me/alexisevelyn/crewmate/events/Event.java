package me.alexisevelyn.crewmate.events;

import me.alexisevelyn.crewmate.Server;

public class Event {

    public boolean call(Server server) {
        server.getEventBus().post(this);
        return true;
    }

}
