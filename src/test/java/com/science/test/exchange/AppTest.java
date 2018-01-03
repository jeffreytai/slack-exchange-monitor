package com.science.test.exchange;

import com.science.test.utils.TestUtils;
import com.science.utils.Utils;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

/**
 * Unit test for simple App.
 */
public class AppTest
{
    @Test
    public void NormalizeMessageWithNonAlphabeticalCharacters() {
    		String message = "**!!** Binfinex DIFFERENT markets FOUND: * added: ['datbtc']";
		String normalizedMessage = Utils.normalizeBotMessage(message);
		String expectedMessage = "Binfinex DIFFERENT markets FOUND: * added: ['datbtc']";
		assertEquals(normalizedMessage, expectedMessage);
    }
    
    @Test
    public void NormalizeMessageWithAlphabeticalCharacters() {
    		String message = "Binfinex DIFFERENT markets FOUND: * added: ['datbtc']";
		String normalizedMessage = Utils.normalizeBotMessage(message);
		String expectedMessage = "Binfinex DIFFERENT markets FOUND: * added: ['datbtc']";
		assertEquals(normalizedMessage, expectedMessage);
    }
    
    @Test
    public void ParseMessagesForBitfinex() {
    		String message1 = "**!!** Binfinex DIFFERENT markets FOUND: * added: ['datbtc']";
    		List<String> currencyList1 = Utils.parseCurrencyString(message1);
    		List<String> expectedCurrencyList1 = new ArrayList<>(Arrays.asList("datbtc"));
    		TestUtils.IsElementsInListIdentical(currencyList1, expectedCurrencyList1);
    		
    		String message2 = "Binfinex markets changed: * added: ['qshusd']";
    		List<String> currencyList2 = Utils.parseCurrencyString(message2);
    		List<String> expectedCurrencyList2 = new ArrayList<>(Arrays.asList("qshusd"));
    		TestUtils.IsElementsInListIdentical(currencyList2, expectedCurrencyList2);
    				
    		String message3 = "Binfinex markets changed: * added: ['qshbtc']";
    		List<String> currencyList3 = Utils.parseCurrencyString(message3);
    		List<String> expectedCurrencyList3 = new ArrayList<>(Arrays.asList("qshbtc"));
    		TestUtils.IsElementsInListIdentical(currencyList3, expectedCurrencyList3);
    		
    		String message4 = "Binfinex markets changed: * added: ['qsheth']";
    		List<String> currencyList4 = Utils.parseCurrencyString(message4);
    		List<String> expectedCurrencyList4 = new ArrayList<>(Arrays.asList("qsheth"));
    		TestUtils.IsElementsInListIdentical(currencyList4, expectedCurrencyList4);
    }
    
    @Test
    public void ParseMessagesForBittrex() {
    		String message1 = "**!!** BTREX DIFFERENT MARKETS FOUND: * added markets: ['BTC-VIB', 'ETH-VIB']";
    		List<String> currencyList1 = Utils.parseCurrencyString(message1);
    		List<String> expectedCurrencyList1 = new ArrayList<>(Arrays.asList("BTC-VIB", "ETH-VIB"));
    		TestUtils.IsElementsInListIdentical(currencyList1, expectedCurrencyList1);
    		
    		String message2 = "BITTREX markets changed: * added markets: ['BTC-ENG', 'ETH-ENG']";
    		List<String> currencyList2 = Utils.parseCurrencyString(message2);
    		List<String> expectedCurrencyList2 = new ArrayList<>(Arrays.asList("BTC-ENG"));
    		TestUtils.IsElementsInListIdentical(currencyList2, expectedCurrencyList2);
    }
    
    @Test
    public void ParseMessagesForHitBTC() {
    		String message1 = "**!!** HitBTC DIFFERENT markets FOUND: * added: ['ETPBTC', 'ETPETH', 'ETPUSD']";
    		List<String> currencyList1 = Utils.parseCurrencyString(message1);
    		List<String> expectedCurrencyList1 = new ArrayList<>(Arrays.asList("ETPBTC", "ETPETH", "ETPUSD"));
    		TestUtils.IsElementsInListIdentical(currencyList1, expectedCurrencyList1);
    		
    		String message2 = "**!!** HitBTC DIFFERENT markets FOUND: * added: ['OTXBTC']";
    		List<String> currencyList2 = Utils.parseCurrencyString(message2);
    		List<String> expectedCurrencyList2 = new ArrayList<>(Arrays.asList("OTXBTC"));
    		TestUtils.IsElementsInListIdentical(currencyList2, expectedCurrencyList2);
    		
    		String message3 = "**!!** HitBTC DIFFERENT markets FOUND: * removed: ['BTCEUR', 'ETHEUR', 'LSKEUR', 'LTCEUR', 'STEEMEUR']";
    		List<String> currencyList3 = Utils.parseCurrencyString(message3);
    		List<String> expectedCurrencyList3 = new ArrayList<>(Arrays.asList("BTCEUR", "ETHEUR", "LSKEUR", "LTCEUR", "STEEMEUR"));
    		TestUtils.IsElementsInListIdentical(currencyList3, expectedCurrencyList3);
    }
}
