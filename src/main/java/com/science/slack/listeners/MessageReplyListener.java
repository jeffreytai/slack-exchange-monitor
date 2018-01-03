package com.science.slack.listeners;

import com.science.utils.Utils;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.events.SlackMessagePosted;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

/**
 * Listens to the channel for any message and replies back with metadata
 * Excludes messages sent on other channels and messages sent from the bot itself
 * @author jeffreytai
 *
 */
public class MessageReplyListener implements SlackMessagePostedListener {

	@Override
	public void onEvent(SlackMessagePosted event, SlackSession session) {
		if (Utils.validateSlackMessage(event, session)) {
			String msg = String.format("New message received: \"%s\" from %s on Channel %s", event.getMessageContent(), event.getSender().getUserName(), event.getChannel().getName());
			session.sendMessage(event.getChannel(), msg);
		}
	}
}
