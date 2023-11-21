package com.fdmgroup.CurrencyConverter.CustomException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvalidCurrencyException extends Exception {

	private static final Logger logger = LogManager.getLogger(InvalidCurrencyException.class);

	public InvalidCurrencyException(String message) {
		super(message);
		logError(message);
	}

	private void logError(String message) {
		logger.warn("InvalidCurrencyException: {}", message);
	}
}
