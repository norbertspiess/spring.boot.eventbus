package de.norbert.spiess.spring.boot.eventbus.job;

import de.norbert.spiess.spring.boot.eventbus.event.Event;
import lombok.ToString;


@ToString
public class Event2 extends Event {

	private final int data;

	public Event2() {
		data = 10;
	}

}
