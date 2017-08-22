package de.norbert.spiess.spring.boot.eventbus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class EventbusApplication {

	public static void main(String[] args) {
		SpringApplication.run(EventbusApplication.class, args);
	}
}
