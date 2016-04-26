package banlogic;

import java.util.ArrayList;
import java.util.List;

public class ProtocolChecker {
	
	private List<Formula> protocol;
	private List<Formula> belives;
	private FormulaApplier fa;
	public ProtocolChecker(List<Formula> protocol, List<Formula> assumptions){
		this.protocol=protocol;
		this.belives=new ArrayList<Formula>(assumptions);
		fa=new FormulaApplier(belives);
	}
	public void checkProtocol(){
		for (Formula protocolFormula : protocol) {
			protocolFormula.setCanApplyRule(true);
			belives.add(protocolFormula);
			while(rulesCanByApplied()){
				Formula rule=getFormulaWithRulesCanBeApplied();
				applyRules(rule);
				rule.setCanApplyRule(false);
			}
		}
		
	}
	private Boolean rulesCanByApplied(){
		for (Formula rule : belives) {
			if(rule.getCanApplyRule())
				return true;
		}
		return false;
	}
	private Formula getFormulaWithRulesCanBeApplied(){
		for (Formula formula : belives) {
			if(formula.getCanApplyRule())
				return formula;
		}
		return null;
	}
	
	private void applyRules(Formula formula){
		Formula r1=fa.applyR1Rule(formula);
		r1.setCanApplyRule(true);
		belives.add(r1);
	}

}
