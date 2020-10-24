package me.alexisevelyn.crewmate.events.bus;

import me.alexisevelyn.crewmate.events.Event;
import me.alexisevelyn.crewmate.events.EventCancellable;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 *
 * @author @SLLCoding
 */
public class EventBus {
    private final HashMap<Class<? extends Event>, CopyOnWriteArrayList<EventHandlerContainer>> listeners = new HashMap<>();

    @SuppressWarnings("unchecked")
    public void register(Object clazz) {
        for (Method method : clazz.getClass().getDeclaredMethods()) {
            for (Annotation annotation : method.getAnnotationsByType(EventHandler.class)) {
                if (method.getParameterTypes().length == 1 && Event.class.isAssignableFrom(method.getParameterTypes()[0])) {
                    method.setAccessible(true);

                    Class<? extends Event> event = (Class<? extends Event>) method.getParameterTypes()[0];

                    if (!listeners.containsKey(event))
                        listeners.put(event, new CopyOnWriteArrayList<>());

                    listeners.get(event).add(new EventHandlerContainer(clazz, method, method.getAnnotation(EventHandler.class).priority(), method.getAnnotation(EventHandler.class).cancellable()));
                    listeners.get(event).sort(Comparator.comparingInt(listener -> listener.getPriority().getSlot()));
                }
            }
        }
    }

    public void unregister(Object clazz) {
        listeners.values().forEach(it -> it.removeIf(listener -> listener.instance == clazz));
    }

    public void post(Event event) {
        boolean cancellable = event instanceof EventCancellable;

        for (EventHandlerContainer container : this.listeners.getOrDefault(event.getClass(), new CopyOnWriteArrayList<>())) {
            if (cancellable && ((EventCancellable) event).isCancelled() && container.cancellable)
                return;

            container.invoke(event);
        }
    }
}
