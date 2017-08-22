package de.norbert.spiess.spring.boot.eventbus.event.bus;

import de.norbert.spiess.spring.boot.eventbus.event.Event;


/**
 * Taken and adapted from https://github.com/noorulhaq/spring.integration.eventbus
 */
public interface EventBus {

	boolean publish(Event event);

	boolean subscribe(String topic, Object subscriber);

	boolean unsubscribe(String topic, Object subscriber);

}
