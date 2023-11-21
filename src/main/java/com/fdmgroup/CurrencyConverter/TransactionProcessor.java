package com.fdmgroup.CurrencyConverter;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.json.JSONArray;
import org.json.JSONObject;

import com.fdmgroup.CurrencyConverter.CustomException.ConversionRateNotFoundException;
import com.fdmgroup.CurrencyConverter.CustomException.InsufficientFundsException;
import com.fdmgroup.CurrencyConverter.CustomException.InvalidCurrencyException;
import com.fdmgroup.CurrencyConverter.CustomException.UserNotFoundException;

/**
 * Represents the TransactionProcessor class
 */
public class TransactionProcessor {
	
	private static final Logger logger = LogManager.getLogger(TransactionProcessor.class);


	private List<User> users;
	private Converter currencyConverter;

	/**
	 * Constructor for TransactionProcessor class
	 * 
	 * @param usersFilePath
	 * @param ratesFilePath
	 */
	public TransactionProcessor(String usersFilePath, String ratesFilePath) {
		this.users = new ArrayList<>();
		initializeUsers(usersFilePath);
		this.currencyConverter = new Converter(ratesFilePath);
	}

	/**
	 * Read from users.json file, parse the data Iterates through each object in the
	 * 'usersArray'. For each user object, extracts the name and wallet details from
	 * the json data After creating a new 'User' object with the name. For each
	 * currency in the user's wallet, it retrieves the balance and adds it to the
	 * 'User' object using the 'addCurrencyBalance' method. Adds 'User' object to
	 * collection named 'users'
	 * 
	 * @param usersFilePath
	 */
	private void initializeUsers(String usersFilePath) {
		try {
			String usersJson = Files.readString(Paths.get(usersFilePath));
			JSONArray usersArray = new JSONArray(usersJson);
			for (Object userObject : usersArray) {
				JSONObject userData = (JSONObject) userObject;
				String username = userData.getString("name");
				JSONObject wallet = userData.getJSONObject("wallet");

				User user = new User(username);
				for (String currency : wallet.keySet()) {
					double balance = wallet.getDouble(currency);
					user.addCurrencyBalance(currency, balance);
				}

				users.add(user);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Responsible for processing a transaction specified by a String input Splits
	 * the 'transaction' string into parts to extract name, fromCurrency, toCurrency
	 * and amount. Finds 'UserProfile' corresponding to the username Check if user
	 * exists, if yes proceed with transaction. Checks if fromCurrency and
	 * toCurrency is valid. Checks if balance is sufficient, if yes carry out the
	 * convert method from Convert class
	 * 
	 * @param transaction
	 * @throws UserNotFoundException
	 * @throws InsufficientFundsException
	 * @throws InvalidCurrencyException
	 */
	public void executeTransaction(String transaction)
			throws UserNotFoundException, InsufficientFundsException, InvalidCurrencyException {
		String[] transactionParts = transaction.split(" ");

		String username = transactionParts[0];
		String fromCurrency = transactionParts[1];
		String toCurrency = transactionParts[2];
		double amount = Double.parseDouble(transactionParts[3]);

		User user = users.stream().filter(u -> u.getName().equals(username)).findFirst().orElse(null);

		if (user == null) {
			throw new UserNotFoundException("User not found for transaction: " + transaction);
		}

		if (!user.getWallet().containsKey(fromCurrency)) {
			throw new InvalidCurrencyException(
					"Invalid 'FROM' currency (" + fromCurrency + ") for transaction: " + transaction);
		}

		if (!currencyConverter.isCurrencyValid(toCurrency)) {
			throw new InvalidCurrencyException(
					"Invalid 'TO' currency (" + toCurrency + ") for transaction: " + transaction);
		}

		double fromBalance = user.getBalance(fromCurrency);

		if (fromBalance < amount) {
			throw new InsufficientFundsException(
					"Insufficient '" + fromCurrency + "' currency for transaction: " + transaction);
		}

		try {
			double convertedAmount = currencyConverter.convert(fromCurrency, toCurrency, amount);

			double newFromCurrencyBalance = user.getBalance(fromCurrency) - amount;
			user.updateBalance(fromCurrency, newFromCurrencyBalance);
			double newToCurrencyBalance = user.getBalance(toCurrency) + convertedAmount;
			user.updateBalance(toCurrency, newToCurrencyBalance);

			try {
			    logger.info("Transaction processed successfully: {}", transaction);
			} catch (Exception e) {
			    e.printStackTrace();
			}
		} catch (ConversionRateNotFoundException e) {
			throw new InvalidCurrencyException("Conversion rate not found for transaction: " + transaction);
		}
	}

	/**
	 * Responsible for updating a JSON file containing user data
	 * ('updatedUsers.json') based on current state of the user profiles stored in
	 * the 'users' collection
	 */
	public void updateUsersFile() {
		try {
			JSONArray updatedUsersArray = new JSONArray();
			DecimalFormat decimalFormat = new DecimalFormat("#.##");

			for (User user : users) {
				JSONObject userObject = new JSONObject(new LinkedHashMap<>());
				userObject.put("name", user.getName());

				JSONObject walletObject = user.getWalletAsJSONObject();
				JSONObject formattedWallet = new JSONObject();

				for (String key : walletObject.keySet()) {
					double value = (Double) walletObject.get(key);
					formattedWallet.put(key, Double.valueOf(decimalFormat.format(value)));
				}

				userObject.put("wallet", formattedWallet);
				updatedUsersArray.put(userObject);
			}

			FileWriter fileWriter = new FileWriter("src/main/resources/updatedUsers.json");
			fileWriter.write(updatedUsersArray.toString(2));
			fileWriter.close();
			
			try {
			    logger.info("Updated 'updatedUsers.json' successfully");
			} catch (Exception e) {
			    e.printStackTrace();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
