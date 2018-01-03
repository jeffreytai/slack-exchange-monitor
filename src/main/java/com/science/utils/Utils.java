package com.science.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.ullink.slack.simpleslackapi.SlackChannel;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;

public class Utils {
	/**
	 * Reads from a file of keys in the following format:
	 * name: "key"
	 * @param filename
	 * @return Map of keys to values
	 */
	// TODO: Maybe change this to JSON format instead of a map
	public static Map<String, String> parseKeyFile(String filename){
		Map<String, String> keyMap = new HashMap<String, String>();
		try {
			try (Scanner scan = new Scanner(Utils.class.getResourceAsStream(filename))){
				while (scan.hasNextLine()) {
					String line = scan.nextLine();
					String key = line.substring(0, line.indexOf(":"));
					String value = line.substring(line.indexOf("\"") + 1, line.lastIndexOf("\""));
					keyMap.put(key, value);
				}
				scan.close();
			}
			return keyMap;
		}
		catch (Exception ex) {
			return null;
		}
	}
	
	/**
	 * Run base validations on a slack message
	 * -Return false if the message comes from a different channel
	 * -Return false if the message sender is the bot
	 * @param event
	 * @param session
	 * @return
	 */
	public static boolean validateSlackMessage(SlackMessagePosted event, SlackSession session) {
		// Only retrieve messages from the "testing" channel
		SlackChannel slackChannel = session.findChannelByName(Constants.LISTENING_CHANNEL);
		if (!slackChannel.getId().equals(event.getChannel().getId())) {
			return false;
		}
		
//		// Only retrieve messages from a specific user (monitor bot)
//		SlackUser monitorBotUser = session.findUserByUserName("Slack API Tester");
//		if (!monitorBotUser.getId().equals(event.getSender().getId())) {
//			return;
//		}
		
		// Ignore the messages sent by the bot
		if (session.sessionPersona().getId().equals(event.getSender().getId())) {
			return false;
		}
		
//		// Filter messages
//		String messageContent = event.getMessageContent();
//		if (!messageContent.contains("added")) {
//			return;
//		}
		
		return true;
	}
	
	/**
	 * Returns a list of the currency pairs included in a string
	 * @param currencyStr
	 * @return
	 */
	public static List<String> parseCurrencyString(String str){
		List<String> list = new ArrayList<String>();
		String normalizedStr = Utils.normalizeBotMessage(str);
		String pairs = normalizedStr.substring(normalizedStr.indexOf("[") + 1, normalizedStr.lastIndexOf("]"));
		for (String pair : pairs.split(",")) {
			String tradingPair = pair.substring(pair.indexOf("'") + 1,  pair.lastIndexOf("'"));
			list.add(tradingPair);
		}
		
		return list;
	}
	
	/**
	 * Remove any non-alphabetical characters from the beginning of the string
	 * @param message
	 * @return
	 */
	public static String normalizeBotMessage(String message) {
		String cleanMessage = message.replaceAll("^[^a-zA-Z]*", "");
		return cleanMessage;
	}
}
