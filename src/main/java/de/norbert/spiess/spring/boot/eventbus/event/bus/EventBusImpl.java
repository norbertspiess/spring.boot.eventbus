package de.norbert.spiess.spring.boot.eventbus.event.bus;

import java.util.Map;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;

import com.google.common.collect.Maps;

import de.norbert.spiess.spring.boot.eventbus.event.Event;
import lombok.Setter;


/**
 * Taken and adapted from https://github.com/noorulhaq/spring.integration.eventbus
 */
@Component("eventBus")
public class EventBusImpl implements EventBus, ApplicationContextAware {

	@Setter
	private ApplicationContext applicationContext;

	private final Map<EventSubscriber, MessageHandler> subscribers = Maps.newHashMap();

	@Override
	public boolean subscribe(String topic, Object subscriber) {

		PublishSubscribeChannel intChannel = getChannel(topic);

		EventSubscriberMethodSelectorUtil.loadSubscribeMethodsIntoCache(subscriber);

		MessageHandler messageHandler = (message) -> {
			EventSubscriberMethodSelectorUtil.selectSubscriberMethodAndInvoke(message, subscriber);
		};
		EventSubscriber eventListener = new EventSubscriber(topic, subscriber);
		boolean success = intChannel.subscribe(messageHandler);
		if (success) {
			synchronized (subscribers) {
				subscribers.put(eventListener, messageHandler);
			}
		}
		return success;
	}

	@Override
	public boolean unsubscribe(String topic, Object subscriber) {
		PublishSubscribeChannel intChannel = getChannel(topic);
		EventSubscriber eventListener = new EventSubscriber(topic, subscriber);
		MessageHandler messageHandler;
		boolean success = true;
		synchronized (subscribers) {
			messageHandler = subscribers.get(eventListener);
			subscribers.remove(eventListener);
		}
		if (messageHandler != null) {
			success = intChannel.unsubscribe(messageHandler);
		}

		return success;
	}

	@Override
	public boolean publish(Event event) {
		PublishSubscribeChannel intChannel = getChannel(event.forTopic());
		GenericMessage<Event> intMessage = new GenericMessage<>(event);
		return intChannel.send(intMessage);
	}

	protected PublishSubscribeChannel getChannel(String topic) {
		return this.applicationContext.getBean(topic, PublishSubscribeChannel.class);
	}

	private class EventSubscriber {
		final String topic;

		final Object subscriber;

		EventSubscriber(String topic, Object subscriber) {
			this.topic = topic;
			this.subscriber = subscriber;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + topic.hashCode();
			result = prime * result + subscriber.hashCode();
			return result;
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj) {
				return true;
			}
			if (obj == null) {
				return false;
			}
			if (getClass() != obj.getClass()) {
				return false;
			}
			EventSubscriber other = (EventSubscriber) obj;

			return topic.equals(other.topic) && subscriber.equals(other.subscriber);
		}

	}
}
