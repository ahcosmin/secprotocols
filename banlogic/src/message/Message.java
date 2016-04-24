package message;

import banlogic.Tokens;

public class Message {
	String message;
	
	public boolean isTimeStamp(){
		return message.startsWith(Tokens.timestamp);
	}
	
	public boolean isSharedKey(){
		return message.contains(Tokens.shared_key);
	}
}
