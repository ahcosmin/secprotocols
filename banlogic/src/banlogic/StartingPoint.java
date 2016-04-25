package banlogic;

import java.io.Console;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class StartingPoint {
	public static void main(String[] args) throws FileNotFoundException, IOException{
		List<Rule> list=FormulaExtractor.extractProtocolFormulas();
		List<Rule> as= FormulaExtractor.extractAssumptions();
		FormulaApplier fa=new FormulaApplier(list,as);
		fa.applyRules();
	}
}
