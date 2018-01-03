package com.science.exchange;

public interface Exchange {
	
	/**
	 * Return the name of the exchange
	 * @return
	 */
	public String getName();
	
	/**
	 * Add the text file containing the key & secret in the same path as the source code
	 * @param textFile
	 */
	public void setAuthKeysFromTextFile(String textFile);
	
	/**
	 * Returns all markets with their metadata
	 * @return
	 */
	public String getMarkets();
	
	/**
	 * Returns all currencies currently on Bittrex with their metadata
	 * @return
	 */
	public String getCurrencies();
	
	/**
	 * Returns current tick values for a specific market
	 * @param market
	 * @return
	 */
	public String getTicker(String market);
	
	/**
	 * Returns a 24-hour summary of all markets
	 * @return
	 */
	public String getMarketSummaries();
	
	/**
	 * Returns a 24-hour summary for a specific market
	 * @param market
	 * @return
	 */
	public String getMarketSummary(String market);
	
	/**
	 * Returns the orderbook for a specific market
	 * @param market
	 * @param type
	 * @return
	 */
	public String getOrderBook(String market, String type);
	
	/**
	 * Returns latest trades that occurred for a specific market
	 * @param market
	 * @return
	 */
	public String getMarketHistory(String market);
	
	/**
	 * Places a limit buy in a specific market; returns the UUID of the order
	 * @param market
	 * @param quantity
	 * @param rate
	 * @return
	 */
	public String buyLimit(String market, String quantity, String rate);
	
	/**
	 * Places a market buy in a specific market; returns the UUID of the order
	 * @param market
	 * @param quantity
	 * @return
	 */
	public String buyMarket(String market, String quantity);
	
	/**
	 * Places a limit sell in a specific market; returns the UUID of the order
	 * @param market
	 * @param quantity
	 * @param rate
	 * @return
	 */
	public String sellLimit(String market, String quantity, String rate);
	
	/**
	 * Places a market sell in a specific market; returns the UUID of the order
	 * @param market
	 * @param quantity
	 * @return
	 */
	public String sellMarket(String market, String quantity);
	
	/**
	 * Cancels a specific order based on its UUID
	 * @param uuid
	 * @return
	 */
	public String cancelOrder(String uuid);
	
	/**
	 * Returns your currently open orders in a specific market
	 * @param market
	 * @return
	 */
	public String getOpenOrders(String market);
	
	/**
	 * Returns all your currently open orders
	 * @return
	 */
	public String getOpenOrders();
	
	/**
	 * Returns all balances in your account
	 * @return
	 */
	public String getBalances();
	
	/**
	 * Returns a specific balance in your account
	 * @param currency
	 * @return
	 */
	public String getBalance(String currency);
	
	/**
	 * Returns the deposit address for a specific currency - if one is not found, it will be generated
	 * @param currency
	 * @return
	 */
	public String getDepositAddress(String currency);
	
	/**
	 * Withdraw a certain amount of a specific coin to an address, and add a payment id
	 * @param currency
	 * @param quantity
	 * @param address
	 * @param paymentId
	 * @return
	 */
	public String withdraw(String currency, String quantity, String address, String paymentId);
	
	/**
	 * Withdraw a certain amount of a specific coin to an address
	 * @param currency
	 * @param quantity
	 * @param address
	 * @return
	 */
	public String withdraw(String currency, String quantity, String address);
	
	/**
	 * Returns information about a specific order (by UUID)
	 * @param uuid
	 * @return
	 */
	public String getOrder(String uuid);
	
	/**
	 * Returns your order history for a specific market
	 * @param market
	 * @return
	 */
	public String getOrderHistory(String market);
	
	/**
	 * Returns all of your order history
	 * @return
	 */
	public String getOrderHistory();
	
	/**
	 * Returns your withdrawal history for a specific currency
	 * @param currency
	 * @return
	 */
	public String getWithdrawalHistory(String currency);
	
	/**
	 * Returns all of your withdrawal history
	 * @return
	 */
	public String getWithdrawalHistory();
	
	/**
	 * Returns your deposit history for a specific currency
	 * @param currency
	 * @return
	 */
	public String getDepositHistory(String currency);
	
	/**
	 * Returns all of your deposit history
	 * @return
	 */
	public String getDepositHistory();
	
	
	
}
