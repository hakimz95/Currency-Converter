package com.fdmgroup.CurrencyConverter.CustomException;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Custom exception class for when user is not found in the users.json file
 */
public class UserNotFoundException extends Exception {
	public UserNotFoundException(String message) {
		super(message);
		logError(message);
	}
	
	private void logError(String message) {
        try {
            FileHandler fileHandler = new FileHandler("error.log", true);
            Logger logger = Logger.getLogger("UserNotFoundExceptionLogger");
            logger.addHandler(fileHandler);
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            logger.warning("UserNotFoundException: " + message);
            fileHandler.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
