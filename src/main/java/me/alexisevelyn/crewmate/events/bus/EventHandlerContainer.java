package me.alexisevelyn.crewmate.events.bus;

import me.alexisevelyn.crewmate.events.EventPriority;

import java.lang.reflect.Method;

public class EventHandlerContainer extends EventContainer {

    public final boolean cancellable;

    public EventHandlerContainer(Object instance, Method method, EventPriority priority, boolean triggerCancelled) {
        super(instance, method, priority);
        this.cancellable = triggerCancelled;
    }

}
