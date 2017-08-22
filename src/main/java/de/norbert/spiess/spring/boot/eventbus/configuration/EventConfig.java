package de.norbert.spiess.spring.boot.eventbus.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.messaging.SubscribableChannel;


@Configuration
public class EventConfig {

	@Bean
	public SubscribableChannel defaultChannel() {
		return new PublishSubscribeChannel();
	}

}
