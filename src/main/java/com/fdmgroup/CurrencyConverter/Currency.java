package com.fdmgroup.CurrencyConverter;

/**
 * Represents the Currency class
 */
public class Currency {

	private String username;
	private String fromCurrency;
	private String toCurrency;
	private double amount;

	/**
	 * Constructor for Currency class
	 * 
	 * @param username
	 * @param fromCurrency
	 * @param toCurrency
	 * @param amount
	 */
	public Currency(String username, String fromCurrency, String toCurrency, double amount) {
		this.username = username;
		this.fromCurrency = fromCurrency;
		this.toCurrency = toCurrency;
		this.amount = amount;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getFromCurrency() {
		return fromCurrency;
	}

	public void setFromCurrency(String fromCurrency) {
		this.fromCurrency = fromCurrency;
	}

	public String getToCurrency() {
		return toCurrency;
	}

	public void setToCurrency(String toCurrency) {
		this.toCurrency = toCurrency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

}
