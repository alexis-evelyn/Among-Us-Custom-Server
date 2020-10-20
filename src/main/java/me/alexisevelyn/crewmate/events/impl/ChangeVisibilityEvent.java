package me.alexisevelyn.crewmate.events.impl;

import me.alexisevelyn.crewmate.events.EventCancellable;

public class ChangeVisibilityEvent extends EventCancellable {

    private boolean visible;

    public ChangeVisibilityEvent(boolean visible) {
        this.visible = visible;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

}
