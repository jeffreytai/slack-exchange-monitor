package com.science.twilio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioProcessor {

	private String personalPhoneNumber;
	
	// Singleton processor
	private static volatile TwilioProcessor instance = null;
	private boolean isTest;
	private Properties properties;
	private Properties keys;
	
	private TwilioProcessor(boolean isTest) {
		this.isTest = isTest;
		this.properties = new Properties();
		this.keys = new Properties();
		
		try {
			InputStream inputStream = TwilioProcessor.class.getClassLoader().getResourceAsStream("properties/twilio.properties");
			if (inputStream == null) {
				System.out.println("Unable to locate properties file");
				return;
			}
			
			properties.load(inputStream);
			inputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		this.personalPhoneNumber = properties.getProperty("personal_phone_number");
		String keyName = properties.getProperty("twilio_key_name");
		String tokenName = properties.getProperty("twilio_token_name");
		
		InputStream keyStream = null;
		try {
			if (isTest)
				keyStream = TwilioProcessor.class.getClassLoader().getResourceAsStream("keys/twilio-keys-test.txt");
			else
				keyStream = TwilioProcessor.class.getClassLoader().getResourceAsStream("keys/twilio-keys-live.txt");
			
			if (keyStream == null) {
				System.out.println("Unable to locate key file");
				return;
			}
			
			keys.load(keyStream);
			keyStream.close();			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		Twilio.init(keys.getProperty(keyName), keys.getProperty(tokenName));
	}
	
	public static synchronized TwilioProcessor getInstance(boolean isTest) {
		if (instance == null) {
			instance = new TwilioProcessor(isTest);
		}
		return instance;
	}
	
	public void sendMessage(List<String> phoneNumbers, String messageBody) {
		List<Message> messages = new ArrayList<>();
		try {
			for (String phoneNumber : phoneNumbers) {
				Message message = null;
				if (isTest) {
					message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(properties.getProperty("twilio_test_number")), messageBody).create();
				}
				else {
					message = Message.creator(new PhoneNumber(phoneNumber), new PhoneNumber(properties.getProperty("twilio_phone_number")), messageBody).create();
				}
				messages.add(message);
			}
		}
		catch (Exception ex) {
			System.out.println(ex.toString());
		}
	}
	
	public String getReceivingPhoneNumbers() {
		return this.personalPhoneNumber;
	}
}
