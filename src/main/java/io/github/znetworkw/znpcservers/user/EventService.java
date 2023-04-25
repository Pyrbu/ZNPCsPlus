package io.github.znetworkw.znpcservers.user;

import lol.pyr.znpcsplus.ZNPCsPlus;
import org.bukkit.event.Event;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class EventService<T extends Event> {
    private final Class<T> eventClass;
    private final List<Consumer<T>> eventConsumers;

    protected EventService(Class<T> eventClass, List<Consumer<T>> eventConsumers) {
        this.eventClass = eventClass;
        this.eventConsumers = eventConsumers;
    }

    public Class<T> getEventClass() {
        return this.eventClass;
    }

    public List<Consumer<T>> getEventConsumers() {
        return this.eventConsumers;
    }

    public EventService<T> addConsumer(Consumer<T> consumer) {
        this.getEventConsumers().add(consumer);
        return this;
    }

    public void runAll(T event) {
        ZNPCsPlus.SCHEDULER.runNextTick(() -> this.eventConsumers.forEach(consumer -> consumer.accept(event)));
    }

    public static <T extends Event> EventService<T> addService(ZUser user, Class<T> eventClass) {
        if (EventService.hasService(user, eventClass)) throw new IllegalStateException(eventClass.getSimpleName() + " is already register for " + user.getUUID().toString());
        EventService<T> service = new EventService<>(eventClass, new ArrayList<>());
        user.getEventServices().add(service);
        user.toPlayer().closeInventory();
        return service;
    }

    @SuppressWarnings("unchecked")
    public static <T extends Event> EventService<T> findService(ZUser user, Class<T> eventClass) {
        Objects.requireNonNull(EventService.class);
        return user.getEventServices().stream().filter(eventService -> eventService.getEventClass().isAssignableFrom(eventClass)).map(EventService.class::cast).findFirst().orElse(null);
    }

    public static boolean hasService(ZUser user, Class<? extends Event> eventClass) {
        return user.getEventServices().stream().anyMatch(eventService -> eventService.getEventClass() == eventClass);
    }
}
