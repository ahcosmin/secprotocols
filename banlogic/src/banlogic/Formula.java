package banlogic;

public class Formula {
	private String principal;
	private String operator;
	private String message;
	private String key;
	private String fromPrincipal;
	private Boolean canApplyRule;
	
	public Formula(String principal,String fromPrincipal, String operator, String message, String key) {
		this.principal = principal;
		this.fromPrincipal=fromPrincipal;
		this.operator = operator;
		this.message = message;
		this.key = key;
		this.canApplyRule=false;
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
		Formula r=(Formula)obj;
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

	public Boolean getCanApplyRule() {
		return canApplyRule;
	}

	public void setCanApplyRule(Boolean canApplyRule) {
		this.canApplyRule = canApplyRule;
	}
}
