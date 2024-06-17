package com.um.registration;

import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import com.um.handler.RegistrationHandler;

@Service("registrationFactory")
public class RegistrationFactory {
	
	private ApplicationContext applicationContext;
	
	public RegistrationFactory(ApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	public RegistrationHandler getHandler(String authenticationType) {
		return (RegistrationHandler) applicationContext.getBean(authenticationType+RegistrationHandler.BEAN_SUFFIX);
	}
	
}
