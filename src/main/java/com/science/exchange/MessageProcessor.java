package com.science.exchange;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.science.twilio.TwilioProcessor;

public class MessageProcessor {
	
	private final static Logger logger = LogManager.getLogger(MessageProcessor.class);

	private Exchange exchange;
	
	public MessageProcessor(Exchange exchange) {
		this.exchange = exchange;		
	}
	
	public void process(List<String> tradingPairs) {		
		for (String tradingPair : tradingPairs) {
			logger.info("New trading pair: {}", tradingPair);
		}
		
		TwilioProcessor twilioProcessor = TwilioProcessor.getInstance(false);
		List<String> destinationPhoneNumbers = new ArrayList<>(Arrays.asList(
				twilioProcessor.getReceivingPhoneNumbers()
			));
		String messageBody = String.format("New tokens on %s: %s", exchange.getName(), String.join(", ", tradingPairs));
		
		twilioProcessor.sendMessage(destinationPhoneNumbers, messageBody);
	}
}
