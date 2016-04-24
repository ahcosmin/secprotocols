package banlogic;

public class Rule {
	String principal;
	String operator;
	String message;
	String key;
	
	public Rule(String principal, String operator, String message, String key) {
		this.principal = principal;
		this.operator = operator;
		this.message = message;
		this.key = key;
	}

	public String getPrincipal() {
		return principal;
	}
	
	public String getOperator() {
		return operator;
	}
	
	public String getMessage() {
		return message;
	}
	
	public String getKey() {
		return key;
	}
	
	public String toString(){
		return principal + " " + operator + " " + message + " "  + key;
	}
}
