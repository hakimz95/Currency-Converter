package com.fdmgroup.CurrencyConverter.CustomException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InsufficientFundsException extends Exception {

	private static final Logger logger = LogManager.getLogger(InsufficientFundsException.class);

	public InsufficientFundsException(String message) {
		super(message);
		logError(message);
	}

	private void logError(String message) {
		logger.warn("InsufficientFundsException: {}", message);
	}
}
