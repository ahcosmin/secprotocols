package banlogic;

public class Rule {
	private String principal;
	private String operator;
	private String message;
	private String key;
	private String fromPrincipal;
	
	public Rule(String principal,String fromPrincipal, String operator, String message, String key) {
		this.principal = principal;
		this.fromPrincipal=fromPrincipal;
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
	public String getFromPrincipal(){
		return fromPrincipal;
	}
	
	public String toString(){
		return principal + " " + operator + " " + message + " "  + key;
	}
	@Override
	public boolean equals(Object obj) {
		Rule r=(Rule)obj;
		if(!this.principal.equals(r.principal))
			return false;
		if(!this.operator.equals(r.operator))
			return false;
		if(!this.message.equals(r.message))
			return false;
		if(!this.key.equals(r.key))
			return false;
		if(!this.fromPrincipal.equals(r.fromPrincipal))
			return false;
		return true;
	}
}
