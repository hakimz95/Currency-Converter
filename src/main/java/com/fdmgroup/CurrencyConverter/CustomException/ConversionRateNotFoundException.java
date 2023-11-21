package com.fdmgroup.CurrencyConverter.CustomException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Custom exception class for when conversion rate cannot be found
 */
public class ConversionRateNotFoundException extends Exception {

	private static final Logger logger = LogManager.getLogger(ConversionRateNotFoundException.class);

	public ConversionRateNotFoundException(String message) {
		super(message);
		logError(message);
	}

	private void logError(String message) {
		logger.warn("ConversionRateNotFoundException: {}", message);
	}
}
