package me.alexisevelyn.crewmate.events;

import me.alexisevelyn.crewmate.Server;

public class EventCancellable extends Event {

    private boolean cancelled = false;
    private String reason = "No Reason Specified";

    public boolean isCancelled() { return cancelled; }
    public String getReason() { return reason; }

    public void setCancelled(boolean cancelled) { this.cancelled = cancelled; }
    public void setCancelled(boolean cancelled, String reason) { this.cancelled = cancelled; this.reason = reason; }

    @Override
    public boolean call(Server server) {
        super.call(server);
        return isCancelled();
    }

}
