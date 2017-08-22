package de.norbert.spiess.spring.boot.eventbus.job;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import de.norbert.spiess.spring.boot.eventbus.event.SubscribeToEvent;
import de.norbert.spiess.spring.boot.eventbus.event.bus.EventBus;
import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class EventExampleJob {

	private EventBus eventBus;

	@Autowired
	public EventExampleJob(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	@Scheduled(fixedRate = 5000)
	public void scheduled() {
		log.error("publishing event1");
		eventBus.publish(new Event1());
	}

	@Scheduled(initialDelay = 2500, fixedRate = 5000)
	public void scheduled2() {
		log.error("publishing event2");
		eventBus.publish(new Event2());
	}

	@SubscribeToEvent
	public void subscribe(Event1 event) {
		log.error("retrieved event1: " + event);
	}

	@SubscribeToEvent
	public void subscribe2(Event2 event) {
		log.error("retrieved event2: " + event);
	}


}