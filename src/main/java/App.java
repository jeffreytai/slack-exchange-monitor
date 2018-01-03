import java.util.Map;

import com.science.exchange.Bittrex;
import com.science.exchange.Exchange;
import com.science.slack.listeners.ExchangeMessageListener;
import com.science.utils.Constants;
import com.science.utils.Utils;
import com.ullink.slack.simpleslackapi.SlackSession;
import com.ullink.slack.simpleslackapi.impl.SlackSessionFactory;
import com.ullink.slack.simpleslackapi.listeners.SlackMessagePostedListener;

public class App 
{
    public static void main(String[] args)
    {   		
    	
    		Exchange wrapper = new Bittrex();
    		String str = wrapper.getCurrencies();
    		
    		Map<String, String> kvMap = Utils.parseKeyFile(Constants.KEY_FILE_PATH + Constants.SLACK_KEY_FILE);
		String botAccessToken = kvMap.get(Constants.SLACK_KEY_NAME);
		SlackSession session = SlackSessionFactory.createWebSocketSlackSession(botAccessToken);
		try {
	    		session.connect();
	    		
	    		SlackMessagePostedListener listener = new ExchangeMessageListener();
	    		session.addMessagePostedListener(listener);
		}
		catch (Exception ex) {
			System.out.println("Error with connection");
		}
		
    		
//    		Bittrex wrapper = new Bittrex();
//    		wrapper.setAuthKeysFromTextFile(Constants.KEY_FILE_PATH + wrapper.BITTREX_KEY_FILE);
//        
//    		String rawResponse = wrapper.getMarketSummary("BTC-LTC");
//    		List<HashMap<String, String>> responseMapList = Bittrex.getMapsFromResponse(rawResponse);
//    				
//    		// In some cases, only 1 map is actually returned - if this is assured:
//    		HashMap<String, String> onlyMap = responseMapList.get(0);
//    				
//    		// See available information using present keys
//    		for (Entry<String, String> kv : onlyMap.entrySet()) {
//    			System.out.println(kv.getKey() + " " + kv.getValue());
//    		}
//    				
//    		// Get wanted value using a key found in the KeySet
//    		onlyMap.get("Volume");
//    			
//    		// Some responses have more than 1 map - the List must be traversed in these cases.
//    		String otherRawResponse = wrapper.getBalances();
//    		List<HashMap<String, String>> allBalancesMapList = Bittrex.getMapsFromResponse(otherRawResponse);
//    			
//    		for (HashMap<String, String> map : allBalancesMapList) {
//    			System.out.println(map);
//    		}
//    				
//    		// And then the wanted map can be used
//    			
////    		allBalancesMapList.get(3).get("Balance");
    	
    }
}
