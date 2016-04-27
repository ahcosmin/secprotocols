package banlogic;

import java.util.ArrayList;
import java.util.List;

public class Tokens {
	public static final String believes = "believes";
	public static final String controls = "controls";
	public static final String sees = "sees";
	public static final String said = "said";
	public static final String fresh = "fresh";
	public static final String shared_key = "<->";
	public static final String timestamp = "T";
	public static final List<String> predicates = new ArrayList<String>() {{
	    add("believes");
	    add("controls");
	    add("sees");
	    add("said");
	}};

}
