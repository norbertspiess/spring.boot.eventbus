package de.norbert.spiess.spring.boot.eventbus.event;

/**
 * Taken and adapted from https://github.com/noorulhaq/spring.integration.eventbus
 */
public abstract class Event {

	public String forTopic(){
		return "defaultChannel";
	}

}
