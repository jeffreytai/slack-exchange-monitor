package com.science.exchange;

import java.util.Scanner;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.science.exception.NotImplementedException;
import com.science.utils.Constants;
import com.webcerebrium.binance.api.BinanceApi;
import com.webcerebrium.binance.api.BinanceApiException;

public class Binance implements Exchange {

	public static final String BINANCE_KEY_FILE = "binance-keys.txt";
	
	private static final Logger logger = LogManager.getLogger(Binance.class);
	
	private static BinanceApi binance;
	
	private String apikey;
	private String secret;
	
	public Binance(String apiKey, String secret, int retryAttempts, int retryDelaySeconds) throws Exception {
		try { 
			binance = new BinanceApi(apiKey, secret);
			
			this.apikey = apiKey;
			this.secret = secret;
		}
		catch (BinanceApiException e) {
			logger.error("Binance API Exception");
			throw new Exception(e.toString());
		}
		
	}
	
	public Binance() {
		try {
			setAuthKeysFromTextFile(Constants.KEY_FILE_PATH + BINANCE_KEY_FILE);
			
			binance = new BinanceApi(apikey, secret);
		} catch (BinanceApiException e) {
			logger.error("Binance API Exception");
		}
	}
	
	@Override
	public String getName() {
		return "Binance";
	}

	@Override
	public void setAuthKeysFromTextFile(String textFile) {
		try (Scanner scan = new Scanner(getClass().getResourceAsStream(textFile))){
			String apiKeyLine = scan.nextLine(), secretLine = scan.nextLine();
			
			apikey = apiKeyLine.substring(apiKeyLine.indexOf("\"") + 1, apiKeyLine.lastIndexOf("\""));
			secret = secretLine.substring(secretLine.indexOf("\"") + 1, secretLine.lastIndexOf("\""));
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			logger.error("Text file not found or corrupted - please attach key & secret in the format provided.");
		}
	}

	@Override
	public String getMarkets() {
		throw new NotImplementedException(this.getClass().getEnclosingMethod().getName() + " not implemented");
	}

	@Override
	public String getCurrencies() {
		// TODO Auto-generated method stub
		
		return null;
	}

	@Override
	public String getTicker(String market) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarketSummaries() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarketSummary(String market) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderBook(String market, String type) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getMarketHistory(String market) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyLimit(String market, String quantity, String rate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String buyMarket(String market, String quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sellLimit(String market, String quantity, String rate) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String sellMarket(String market, String quantity) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String cancelOrder(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOpenOrders(String market) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOpenOrders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBalances() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBalance(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDepositAddress(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String withdraw(String currency, String quantity, String address, String paymentId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String withdraw(String currency, String quantity, String address) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrder(String uuid) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderHistory(String market) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getOrderHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWithdrawalHistory(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWithdrawalHistory() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDepositHistory(String currency) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDepositHistory() {
		// TODO Auto-generated method stub
		return null;
	}

}
