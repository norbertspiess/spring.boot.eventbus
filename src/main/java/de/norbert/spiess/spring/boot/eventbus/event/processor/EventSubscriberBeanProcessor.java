package de.norbert.spiess.spring.boot.eventbus.event.processor;

import static lombok.AccessLevel.NONE;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.DestructionAwareBeanPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import de.norbert.spiess.spring.boot.eventbus.event.SubscribeToEvent;
import de.norbert.spiess.spring.boot.eventbus.event.bus.EventBus;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


/**
 * Taken and adapted from https://github.com/noorulhaq/spring.integration.eventbus
 */
@Component
@Order(1000) // default order of 1000
@Slf4j
@Getter
@Setter
public class EventSubscriberBeanProcessor
		implements DestructionAwareBeanPostProcessor, ApplicationContextAware, InitializingBean {

	private ApplicationContext applicationContext;

	// Temporary subscribers placeholder between postProcessBefore and postProcessAfter method calls.
	@Getter(NONE)
	private final Map<String, Object> subscribers = new HashMap<>();

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		if (isAnnotationPresent(bean)) {
			subscribers.put(beanName, bean);
		}
		return bean;
	}

	@Override
	public Object postProcessAfterInitialization(final Object bean, final String beanName) throws BeansException {
		Object subscriber = subscribers.get(beanName);
		if (subscriber != null) {
			EventBus eventBus = getEventBus(eventBusName(subscriber));
			eventBus.subscribe(topic(subscriber), subscriber);
		}
		return bean;
	}

	@Override
	public void postProcessBeforeDestruction(Object bean, String beanName) throws BeansException {
		if (subscribers.containsKey(beanName)) {
			try {
				Object subscriber = subscribers.get(beanName);
				EventBus eventBus = getEventBus(eventBusName(subscriber));
				eventBus.unsubscribe(topic(subscriber), subscriber);
			} catch (Exception e) {
				log.error("An exception occurred while unsubscribing an event listener", e);
			} finally {
				subscribers.remove(beanName);
			}
		}
	}

	@Override
	public boolean requiresDestruction(Object bean) {
		return true;
	}

	private boolean isAnnotationPresent(Object bean) {
		return getAnnotation(bean) != null;
	}

	private SubscribeToEvent getAnnotation(Object bean) {
		return getAnnotation(bean.getClass());
	}

	private SubscribeToEvent getAnnotation(Class<?> clazz) {
		if (clazz == null || clazz == Object.class) {
			return null;
		}
		Method methods[] = clazz.getMethods();
		for (Method method : methods) {
			boolean foundIt = method.isAnnotationPresent(SubscribeToEvent.class);
			if (foundIt) {
				return method.getAnnotation(SubscribeToEvent.class);
			}
		}
		// recursive call with super class
		return getAnnotation(clazz.getSuperclass());

	}

	private String eventBusName(Object bean) {
		SubscribeToEvent annotation = getAnnotation(bean);
		if (annotation != null) {
			return annotation.eventBus();
		} else {
			throw new IllegalArgumentException("Missing annotation");
		}
	}

	private String topic(Object bean) {
		SubscribeToEvent annotation = getAnnotation(bean);
		if (annotation != null) {
			return annotation.topic();
		} else {
			throw new IllegalArgumentException("Missing annotation");
		}
	}

	protected EventBus getEventBus(String name) {
		Object bean = getApplicationContext().getBean(name);
		if (!(bean instanceof EventBus)) {
			throw new IllegalStateException("Wrong EventBus type, got: " + bean.getClass().getName());
		}
		return (EventBus) bean;
	}

	public void afterPropertiesSet() throws Exception {
		// do nothing
	}

}
