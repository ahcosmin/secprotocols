package banlogic;

import java.util.ArrayList;
import java.util.List;

public class ProtocolChecker {

	private List<Formula> protocol;
	private List<Formula> belives;
	private FormulaApplier fa;
	private Formula result1 = new Formula("A", "", Tokens.believes, "A<->(Kab)B", "");
	private Formula result2 = new Formula("B", "", Tokens.believes, "A<->(Kab)B", "");

	public ProtocolChecker(List<Formula> protocol, List<Formula> assumptions) {
		this.protocol = protocol;
		this.belives = new ArrayList<Formula>(assumptions);
		fa = new FormulaApplier(belives);
	}

	public void checkProtocol() {
		for (Formula protocolFormula : protocol) {
			protocolFormula.setCanApplyRule(true);
			belives.add(protocolFormula);
			while (rulesCanByApplied()) {
				Formula rule = getFormulaWithRulesCanBeApplied();
				applyRules(rule);
				rule.setCanApplyRule(false);
			}
		}
		if (belives.contains(result1) && belives.contains(result2))
			System.out.println("Authentication proved!");
		else
			System.out.println("Could not prove authentication!");
	}

	private Boolean rulesCanByApplied() {
		for (Formula rule : belives) {
			if (rule.getCanApplyRule())
				return true;
		}
		return false;
	}

	private Formula getFormulaWithRulesCanBeApplied() {
		for (Formula formula : belives) {
			if (formula.getCanApplyRule())
				return formula;
		}
		return null;
	}

	private void applyRules(Formula formula) {
		System.out.println("###################################################################");
		System.out.println("Belives: " + belives);
		System.out.println("Apply formulas for " + formula);
		addResult(fa.applyR1Rule(formula));
		System.out.println("___________________________________________________________________");
		addResult(fa.applyR5Rule(formula));
		System.out.println("___________________________________________________________________");
		List<Formula> results = fa.applyR13Rule(formula);
		for (Formula aux : results) {
			addResult(aux);
		}
		System.out.println("___________________________________________________________________");

	}

	private void addResult(Formula formula) {
		if (formula != null) {
			formula.setCanApplyRule(true);
			if (!belives.contains(formula))
				belives.add(formula);
		}

	}

}
