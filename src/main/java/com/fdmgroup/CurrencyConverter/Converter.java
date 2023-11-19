package com.fdmgroup.CurrencyConverter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import org.json.JSONObject;

import com.fdmgroup.CurrencyConverter.CustomException.ConversionRateNotFoundException;

/**
 * Represents the Converter class
 */
public class Converter {

	private JSONObject exchangeRates;

	/**
	 * Constructor for Converter class
	 * 
	 * @param ratesFilePath
	 */
	public Converter(String ratesFilePath) {
		try {
			String ratesJson = Files.readString(Paths.get(ratesFilePath));
			exchangeRates = new JSONObject(ratesJson);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Represents convert method Takes the fromCurrency value and converts it to the
	 * toCurrency value
	 * 
	 * @param fromCurrency
	 * @param toCurrency
	 * @param amount
	 * @return converted money
	 * @throws ConversionRateNotFoundException
	 */
	public double convert(String fromCurrency, String toCurrency, double amount)
			throws ConversionRateNotFoundException {
		if (fromCurrency.equals(toCurrency)) {
			return amount;
		}

		double toRate = getRate(toCurrency);

		if (fromCurrency.equalsIgnoreCase("USD")) {
			if (toCurrency.equalsIgnoreCase("USD")) {
				return amount;
			} else if (toRate != -1) {
				// Convert directly from USD to the toCurrency
				return amount * toRate;
			} else {
				throw new ConversionRateNotFoundException(
						"Conversion rate not available for " + fromCurrency + " to " + toCurrency);
			}
		} else {
			double fromRate = getRate(fromCurrency);

			if (toCurrency.equalsIgnoreCase("USD")) {
				if (fromRate != -1) {
					// Convert the fromCurrency to USD
					return amount / fromRate;
				} else {
					throw new ConversionRateNotFoundException(
							"Conversion rate not available for " + fromCurrency + " to " + toCurrency);
				}
			} else if (fromRate != -1 && toRate != -1) {
				// Convert the amount from the 'fromCurrency' to 'USD' and then to 'toCurrency'
				double inUSD = amount / fromRate;
				return inUSD * toRate;
			} else {
				throw new ConversionRateNotFoundException(
						"Conversion rate not available for " + fromCurrency + " to " + toCurrency);
			}
		}
	}

	/**
	 * Retrieves the conversion rate of a specific currency from the fx_rates.json
	 * file Assume exchangeRates is a JSONObject where currency codes are keys. Each
	 * key contains another JSONObject with a key-value pair for the rate. If the
	 * exchangeRates JSONObject contains the currency code (in lowercase) as a key,
	 * the method retrieves the nested JSONObject associated with that currency and
	 * fetches the "rate" field
	 * 
	 * @param currency
	 * @return
	 */
	private double getRate(String currency) {
		if (exchangeRates.has(currency.toLowerCase())) {
			return exchangeRates.getJSONObject(currency.toLowerCase()).getDouble("rate");
		}
		return -1;
	}

	/**
	 * Checks if a given currency is valid in reference to fx_rates.json. Also
	 * checks if currency is equivalent to USD. If either of the condition is true,
	 * it returns true
	 * 
	 * @param currency
	 * @return
	 */
	public boolean isCurrencyValid(String currency) {
		return exchangeRates.has(currency.toLowerCase()) || currency.equalsIgnoreCase("USD");
	}

}
