package de.norbert.spiess.spring.boot.eventbus.job;

import de.norbert.spiess.spring.boot.eventbus.event.Event;
import lombok.ToString;


@ToString
public class Event1 extends Event {

	private final int data;

	public Event1() {
		data = 5;
	}

}
