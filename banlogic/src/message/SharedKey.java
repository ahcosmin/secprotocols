package message;

public class SharedKey extends Message{
	public String firstPrincipal;
	public String secondPrincipal;
	public String sharedKey;
	
	public SharedKey(String firstPrincipal, String secondPrincipal, String sharedKey) {
		super();
		this.firstPrincipal = firstPrincipal;
		this.secondPrincipal = secondPrincipal;
		this.sharedKey = sharedKey;
	}

	public String getFirstPrincipal() {
		return firstPrincipal;
	}

	public String getSecondPrincipal() {
		return secondPrincipal;
	}

	public String getSharedKey() {
		return sharedKey;
	}
}
