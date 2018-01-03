package com.science.slack.listeners;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.science.exchange.Binance;
import com.science.exchange.Bittrex;
import com.science.exchange.Exchange;
import com.science.exchange.MessageProcessor;
import com.science.utils.Utils;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

/**
 * Listens to the channel for any message and filters out non-exchange related messages 
 * Excludes messages sent on other channels and messages sent from the bot itself
 * @author jeffreytai
 *
 */
public class ExchangeMessageListener implements SlackMessagePostedListener {
	
	private final static Logger logger = LogManager.getLogger(ExchangeMessageListener.class);
	
	@Override
	public void onEvent(SlackMessagePosted event, SlackSession session) {
		if (Utils.validateSlackMessage(event, session)) {
			String message = event.getMessageContent();
			
			if (message.contains("added") && message.contains("market")) {
				logger.info("Processing message {}", message);
				Exchange exchange = null;
				
				/**
				 * Sample message formats:
				 * Tweet from RichieLA: b"By the power of Greyskull ... I HAVE THE $POWR"
				 * **!!** Binfinex DIFFERENT markets FOUND: * added: ['datbtc']
				 * 
				 * **!!** HitBTC DIFFERENT markets FOUND: * added: ['ETPBTC', 'ETPETH', 'ETPUSD']
				 * **!!** HitBTC DIFFERENT markets FOUND: * added: ['OTXBTC']
				 * **!!** HitBTC DIFFERENT markets FOUND: * removed: ['BTCEUR', 'ETHEUR', 'LSKEUR', 'LTCEUR', 'STEEMEUR']
				 * 
				 * **!!** BTREX DIFFERENT MARKETS FOUND: * added markets: ['BTC-VIB', 'ETH-VIB']
				 * **!!** BTREX DIFFERENT currencies FOUND: * added currencies: [('VIB', 'Vibe')]
				 * **!!** BTREX DIFFERENT currencies FOUND: * added currencies: [('VIB', 'Viberate')] * removed currencies: [('VIB', 'Vibe')]
				 * 
				 * BITTREX markets changed: * added markets: ['BTC-ENG', 'ETH-ENG']
				 * BITTREX currencies changed: * added currencies [('ENG', 'Enigma')]
				 * 
				 * Binfinex markets changed: * added: ['qshusd']
				 * Binfinex markets changed: * added: ['qshbtc']
				 * Binfinex markets changed: * added: ['qsheth']
				 */
				
				message = Utils.normalizeBotMessage(message);
				String exchangeName = message.split(" ")[0].toLowerCase();
				List<String> tradingPairs = Utils.parseCurrencyString(message);
				
				switch (exchangeName) {
					case "btrex": // the bot has bittrex spelled as btrex
					case "bittrex":
						exchange = new Bittrex();
						break;
					case "binance":
						exchange = new Binance();
						break;
					case "binfinex": // the bot has bitfinex misspelled as binfinex
						break;
					case "kraken":
					case "hitbtc":
					case "polo":
						break;
					default:
						break;
				}
							
				logger.info("Received the following trading pairs for {}: {}", exchange.getName(), tradingPairs.toString());
				
				MessageProcessor messageProcessor = new MessageProcessor(exchange);
				messageProcessor.process(tradingPairs);
			}
		}
	}
}
