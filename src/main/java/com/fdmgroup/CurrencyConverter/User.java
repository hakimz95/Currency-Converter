package com.fdmgroup.CurrencyConverter;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

/**
 * Represents User class
 */
public class User {

	private String name;
	private Map<String, Double> wallet;

	/**
	 * Constructor for User class
	 * 
	 * @param name
	 */
	public User(String name) {
		this.name = name;
		this.wallet = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Double> getWallet() {
		return wallet;
	}

	public void setWallet(Map<String, Double> wallet) {
		this.wallet = wallet;
	}

	public void addCurrencyBalance(String currency, double balance) {
		wallet.put(currency, balance);
	}

	public double getBalance(String currency) {
		return wallet.getOrDefault(currency, 0.0);
	}

	public void updateBalance(String currency, double newBalance) {
		wallet.put(currency, newBalance);
	}

	/**
	 * Takes a 'User' object as input and creates a formatted string representation
	 * of the user's wallet details to reduce the decimal place to 2 decimal place
	 * 
	 * @param user
	 * @return details as a String
	 */
	public String formatWalletDetails(User user) {
		StringBuilder details = new StringBuilder();
		for (Map.Entry<String, Double> entry : user.getWallet().entrySet()) {
			String currency = entry.getKey();
			double amount = entry.getValue();
			String formattedAmount = String.format("%.2f", amount); // Formats to two decimal places
			details.append(currency).append(": ").append(formattedAmount).append(", ");
		}
		return details.toString();
	}

	/**
	 * Constructs a JSONObject representation of a user's wallet
	 * 
	 * @return walletObject
	 */
	public JSONObject getWalletAsJSONObject() {
		JSONObject walletObject = new JSONObject();

		for (Map.Entry<String, Double> entry : wallet.entrySet()) {
			String currency = entry.getKey();
			double balance = entry.getValue();
			walletObject.put(currency, balance);
		}

		return walletObject;
	}

}
