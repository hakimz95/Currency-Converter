package com.fdmgroup.CurrencyConverter.CustomException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class UserNotFoundException extends Exception {

	private static final Logger logger = LogManager.getLogger(UserNotFoundException.class);

	public UserNotFoundException(String message) {
		super(message);
		logError(message);
	}

	private void logError(String message) {
		logger.warn("UserNotFoundException: {}", message);
	}
}
