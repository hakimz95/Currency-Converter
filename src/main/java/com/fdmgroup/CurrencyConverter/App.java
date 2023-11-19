package com.fdmgroup.CurrencyConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import com.fdmgroup.CurrencyConverter.CustomException.InsufficientFundsException;
import com.fdmgroup.CurrencyConverter.CustomException.InvalidCurrencyException;
import com.fdmgroup.CurrencyConverter.CustomException.UserNotFoundException;

/**
 * Currency Converter Application
 * 
 * @author Abdul Hakim
 * @version 1.0
 */
public class App {
	/**
	 * Runner for the Currency Converter Application
	 * 
	 * @param args
	 * @throws InvalidCurrencyException
	 * @throws InsufficientFundsException
	 * @throws UserNotFoundException
	 */
	public static void main(String[] args)
			throws UserNotFoundException, InsufficientFundsException, InvalidCurrencyException {
		TransactionProcessor processor = new TransactionProcessor("src/main/resources/users.json",
				"src/main/resources/fx_rates.json");

		try {
			String transactionsData = Files.readString(Paths.get("src/main/resources/transactions.txt"));
			String[] transactions = transactionsData.split("\n");

			for (String transaction : transactions) {
				try {
					processor.executeTransaction(transaction);
				} catch (UserNotFoundException | InsufficientFundsException | InvalidCurrencyException e) {
					e.printStackTrace();
				}
			}

			processor.updateUsersFile();

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
