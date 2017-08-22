package de.norbert.spiess.spring.boot.eventbus.event;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import org.springframework.integration.annotation.ServiceActivator;


/**
 * Taken and adapted from https://github.com/noorulhaq/spring.integration.eventbus
 */
@Target(METHOD)
@Retention(RUNTIME)
@ServiceActivator
public @interface SubscribeToEvent {

	String eventBus() default "eventBus";

	String topic() default "defaultChannel";
	
}
