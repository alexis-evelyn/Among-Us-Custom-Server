package me.alexisevelyn.crewmate.events.bus;

import me.alexisevelyn.crewmate.events.EventPriority;

import java.lang.reflect.Method;

public class EventContainer {

    public final Object instance;
    public final Method method;
    private final EventPriority priority;

    public EventContainer(Object instance, Method method, EventPriority priority) {
        this.instance = instance;
        this.method   = method;
        this.priority = priority;
    }

    public void invoke(Object... args) {
        try {
            this.method.invoke(instance, args);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public EventPriority getPriority() { return priority; }

}
