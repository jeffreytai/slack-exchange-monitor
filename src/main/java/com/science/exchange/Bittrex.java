package com.science.exchange;

import java.io.*;
import java.net.*;
import java.util.*;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.science.exception.ReconnectionAttemptsExceededException;
import com.science.utils.Constants;

public class Bittrex implements Exchange {
	
	public static final String BITTREX_KEY_FILE = "bittrex-keys.txt";
	
	private static final Logger logger = LogManager.getLogger(Bittrex.class);
	
	// https://github.com/platelminto/java-bittrex
	public static final String ORDERBOOK_BUY = "buy", ORDERBOOK_SELL = "sell", ORDERBOOK_BOTH = "both";
	public static final int DEFAULT_RETRY_ATTEMPTS = 1;
	public static final int DEFAULT_RETRY_DELAY = 15;
	
	private static final Exception InvalidStringListException = new Exception("Must be in key-value pairs");
	private final String API_VERSION = "1.1", INITIAL_URL = "https://bittrex.com/api/";
	private final String PUBLIC = "public", MARKET = "market", ACCOUNT = "account";
	private final String encryptionAlgorithm = "HmacSHA512";
	private String apikey;
	private String secret;
	private final int retryAttempts;
	private int retryAttemptsLeft;
	private final int retryDelaySeconds;

	public Bittrex(String apikey, String secret, int retryAttempts, int retryDelaySeconds) {
		this.apikey = apikey;
		this.secret = secret;
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		
		retryAttemptsLeft = retryAttempts;
	}

	public Bittrex(int retryAttempts, int retryDelaySeconds) {
		setAuthKeysFromTextFile(Constants.KEY_FILE_PATH + BITTREX_KEY_FILE);
		
		this.retryAttempts = retryAttempts;
		this.retryDelaySeconds = retryDelaySeconds;
		
		retryAttemptsLeft = retryAttempts;
	}
	
	public Bittrex() {
		this(DEFAULT_RETRY_ATTEMPTS, DEFAULT_RETRY_DELAY);
	}
	
	@Override
	public String getName() {
		return "Bittrex";
	}

	@Override
	public void setAuthKeysFromTextFile(String textFile) { // Add the text file containing the key & secret in the same path as the source code
		try (Scanner scan = new Scanner(getClass().getResourceAsStream(textFile))) {
			String apikeyLine = scan.nextLine(), secretLine = scan.nextLine();

			apikey = apikeyLine.substring(apikeyLine.indexOf("\"") + 1, apikeyLine.lastIndexOf("\""));
			secret = secretLine.substring(secretLine.indexOf("\"") + 1, secretLine.lastIndexOf("\""));
		} catch (NullPointerException | IndexOutOfBoundsException e) {
			logger.error("Text file not found or corrupted - please attach key & secret in the format provided.");
		}
	}

	@Override
	public String getMarkets() { // Returns all markets with their metadata

		return getJson(API_VERSION, PUBLIC, "getmarkets");
	}

	@Override
	public String getCurrencies() { // Returns all currencies currently on Bittrex with their metadata

		return getJson(API_VERSION, PUBLIC, "getcurrencies");
	}

	@Override
	public String getTicker(String market) { // Returns current tick values for a specific market

		return getJson(API_VERSION, PUBLIC, "getticker", returnCorrectMap("market", market));
	}

	@Override
	public String getMarketSummaries() { // Returns a 24-hour summary of all markets

		return getJson(API_VERSION, PUBLIC, "getmarketsummaries");
	}

	@Override
	public String getMarketSummary(String market) { // Returns a 24-hour summary for a specific market

		return getJson(API_VERSION, PUBLIC, "getmarketsummary", returnCorrectMap("market", market));
	}

	@Override
	public String getOrderBook(String market, String type) { // Returns the orderbook for a specific market

		return getJson(API_VERSION, PUBLIC, "getorderbook", returnCorrectMap("market", market, "type", type));
	}

	@Override
	public String getMarketHistory(String market) { // Returns latest trades that occurred for a specific market

		return getJson(API_VERSION, PUBLIC, "getmarkethistory", returnCorrectMap("market", market));
	}

	@Override
	public String buyLimit(String market, String quantity, String rate) { // Places a limit buy in a specific market; returns the UUID of the order

		return getJson(API_VERSION, MARKET, "buylimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	@Override
	public String buyMarket(String market, String quantity) { // Places a market buy in a specific market; returns the UUID of the order

		return getJson(API_VERSION, MARKET, "buymarket", returnCorrectMap("market", market, "quantity", quantity));
	}

	@Override
	public String sellLimit(String market, String quantity, String rate) { // Places a limit sell in a specific market; returns the UUID of the order

		return getJson(API_VERSION, MARKET, "selllimit", returnCorrectMap("market", market, "quantity", quantity, "rate", rate));
	}

	@Override
	public String sellMarket(String market, String quantity) { // Places a market sell in a specific market; returns the UUID of the order

		return getJson(API_VERSION, MARKET, "sellmarket", returnCorrectMap("market", market, "quantity", quantity));
	}

	@Override
	public String cancelOrder(String uuid) { // Cancels a specific order based on its UUID

		return getJson(API_VERSION, MARKET, "cancel", returnCorrectMap("uuid", uuid));
	}

	@Override
	public String getOpenOrders(String market) { // Returns your currently open orders in a specific market

		String method = "getopenorders";

		if(market.equals(""))

			return getJson(API_VERSION, MARKET, method);

		return getJson(API_VERSION, MARKET, method, returnCorrectMap("market", market));
	}

	@Override
	public String getOpenOrders() { // Returns all your currently open orders

		return getOpenOrders("");
	}

	@Override
	public String getBalances() { // Returns all balances in your account

		return getJson(API_VERSION, ACCOUNT, "getbalances");
	}

	@Override
	public String getBalance(String currency) { // Returns a specific balance in your account

		return getJson(API_VERSION, ACCOUNT, "getbalance", returnCorrectMap("currency", currency));
	}

	@Override
	public String getDepositAddress(String currency) { // Returns the deposit address for a specific currency - if one is not found, it will be generated

		return getJson(API_VERSION, ACCOUNT, "getdepositaddress", returnCorrectMap("currency", currency));
	}

	@Override
	public String withdraw(String currency, String quantity, String address, String paymentId) { // Withdraw a certain amount of a specific coin to an address, and add a payment id

		String method = "withdraw";

		if(paymentId.equals(""))

			return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency, "quantity", quantity, "address", address));

		return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency, "quantity", quantity, "address", address, "paymentid", paymentId));
	}

	@Override
	public String withdraw(String currency, String quantity, String address) { // Withdraw a certain amount of a specific coin to an address

		return withdraw(currency, quantity, address, "");
	}

	@Override
	public String getOrder(String uuid) { // Returns information about a specific order (by UUID)

		return getJson(API_VERSION, ACCOUNT, "getorder", returnCorrectMap("uuid", uuid));
	}

	@Override
	public String getOrderHistory(String market) { // Returns your order history for a specific market

		String method = "getorderhistory";

		if(market.equals(""))

			return getJson(API_VERSION, ACCOUNT, method);

		return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("market", market));
	}

	@Override
	public String getOrderHistory() { // Returns all of your order history

		return getOrderHistory("");
	}

	@Override
	public String getWithdrawalHistory(String currency) { // Returns your withdrawal history for a specific currency

		String method = "getwithdrawalhistory";

		if(currency.equals(""))

			return getJson(API_VERSION, ACCOUNT, method);

		return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency));
	}

	@Override
	public String getWithdrawalHistory() { // Returns all of your withdrawal history

		return getWithdrawalHistory("");
	}

	@Override
	public String getDepositHistory(String currency) { // Returns your deposit history for a specific currency

		String method = "getdeposithistory";

		if(currency.equals(""))

			return getJson(API_VERSION, ACCOUNT, method);

		return getJson(API_VERSION, ACCOUNT, method, returnCorrectMap("currency", currency));
	}

	@Override
	public String getDepositHistory() { // Returns all of your deposit history

		return getDepositHistory("");
	}

	private HashMap<String, String> returnCorrectMap(String...parameters) { // Handles the exception of the generateHashMapFromStringList() method gracefully as to not have an excess of try-catch statements

		HashMap<String, String> map = null;

		try {

			map = generateHashMapFromStringList(parameters);

		} catch (Exception e) {

			e.printStackTrace();
		}

		return map;
	}

	private HashMap<String, String> generateHashMapFromStringList(String...strings) throws Exception { // Method to easily create a HashMap from a list of Strings

		if(strings.length % 2 != 0)

			throw InvalidStringListException;

		HashMap<String, String> map = new HashMap<>();

		for(int i = 0; i < strings.length; i += 2) // Each key will be i, with the following becoming its value

			map.put(strings[i], strings[i + 1]);

		return map;
	}

	private String getJson(String apiVersion, String type, String method) {

		return getResponseBody(generateUrl(apiVersion, type, method));
	}

	private String getJson(String apiVersion, String type, String method, HashMap<String, String> parameters) {

		return getResponseBody(generateUrl(apiVersion, type, method, parameters));
	}

	private String generateUrl(String apiVersion, String type, String method) {

		return generateUrl(apiVersion, type, method, new HashMap<String, String>());
	}

	private String generateUrl(String apiVersion, String type, String method, HashMap<String, String> parameters) {

		String url = INITIAL_URL;

		url += "v" + apiVersion + "/";
		url += type + "/";
		url += method;
		url += generateUrlParameters(parameters);

		return url;
	}

	private String generateUrlParameters(HashMap<String, String> parameters) { // Returns a String with the key-value pairs formatted for URL

		String urlAttachment = "?";

		Object[] keys = parameters.keySet().toArray();

		for(Object key : keys)

			urlAttachment += key.toString() + "=" + parameters.get(key) + "&";

		return urlAttachment;
	}

	public static List<HashMap<String, String>> getMapsFromResponse(String response) {

		final List<HashMap<String, String>> maps = new ArrayList<>();

		if(!response.contains("[")) {

			maps.add(jsonMapToHashMap(response.substring(response.lastIndexOf("\"result\":") + "\"result\":".length(), response.indexOf("}") + 1))); // Sorry.

		} else {

			final String resultArray = response.substring(response.indexOf("\"result\":") + "\"result\":".length() + 1, response.lastIndexOf("]"));

			final String[] jsonMaps = resultArray.split(",(?=\\{)");

			for(String map : jsonMaps)

				maps.add(jsonMapToHashMap(map));
		}

		return maps;
	}

	private static HashMap<String, String> jsonMapToHashMap(String jsonMap) {

		final Gson gson = new GsonBuilder().setPrettyPrinting().create();

		return gson.fromJson(jsonMap, new TypeToken<HashMap<String, String>>(){}.getType());
	}

	private String getResponseBody(final String baseUrl) {

		String result = null;
		final String url = baseUrl + "apikey=" + apikey + "&nonce=" + EncryptionUtility.generateNonce();

		try {
			
			HttpClient client = HttpClientBuilder.create().setRetryHandler(new DefaultHttpRequestRetryHandler(0, false)).build();
			
			HttpGet request = new HttpGet(url);
			request.addHeader("apisign", EncryptionUtility.calculateHash(secret, url, encryptionAlgorithm)); // Attaches signature as a header

			HttpResponse httpResponse = client.execute(request);
			BufferedReader reader = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));

			StringBuffer resultBuffer = new StringBuffer();
			String line = "";

			while ((line = reader.readLine()) != null)

				resultBuffer.append(line);

			result = resultBuffer.toString();

		} catch (UnknownHostException | SocketException e) {
			
			if(retryAttemptsLeft-- > 0) {
				
				System.err.printf("Could not connect to host - retrying in %d seconds... [%d/%d]%n", retryDelaySeconds, retryAttempts - retryAttemptsLeft, retryAttempts);
				
				try {
					
					Thread.sleep(retryDelaySeconds * 1000);
					
				} catch (InterruptedException e1) {
					
					e1.printStackTrace();
				}
				
				result = getResponseBody(baseUrl);
				
			} else {
				
				throw new ReconnectionAttemptsExceededException("Maximum amount of attempts to connect to host exceeded.");
			}
			
		} catch (IOException e) {
			
			e.printStackTrace();
			
		} finally {
			
			retryAttemptsLeft = retryAttempts;
		}

		return result;
	}
}
