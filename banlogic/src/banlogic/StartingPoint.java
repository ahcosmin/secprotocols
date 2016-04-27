package banlogic;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class StartingPoint {
	public static void main(String[] args) throws FileNotFoundException, IOException {
		List<Formula> list = FormulaExtractor.extractProtocolFormulas();
		List<Formula> as = FormulaExtractor.extractAssumptions();
		ProtocolChecker pc = new ProtocolChecker(list, as);
		pc.checkProtocol();
	}
}
