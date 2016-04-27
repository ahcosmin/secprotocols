package banlogic;

import java.util.ArrayList;
import java.util.List;

public class FormulaUtils {
	public static List<Formula> getFreshFormulas(List<Formula> toSearchList) {
		List<Formula> filteredList = new ArrayList<Formula>();
		for (Formula f : toSearchList) {
			if (f.getMessage().contains(Tokens.fresh)) {
				filteredList.add(f);
			}
		}
		return filteredList;
	}

	public static List<Formula> getBelievesFormulas(List<Formula> toSearchList) {
		List<Formula> filteredList = new ArrayList<Formula>();
		for (Formula f : toSearchList) {
			if (f.getOperator().equals(Tokens.believes)) {
				filteredList.add(f);
			}
		}
		return filteredList;
	}

	public static List<Formula> getFormulasWithFreshMessage(List<Formula> toSearchList) {
		List<Formula> freshMessageFormulas = new ArrayList<Formula>();
		for (Formula f : toSearchList) {
			if (f.getMessage().contains(Tokens.fresh)) {
				freshMessageFormulas.add(f);
			}
		}
		return freshMessageFormulas;
	}

	public static List<Formula> getFormulasWithSaidMessage(List<Formula> toSearchList) {
		List<Formula> saidMessageFormulas = new ArrayList<Formula>();
		for (Formula f : toSearchList) {
			if (f.getMessage().contains(Tokens.said)) {
				saidMessageFormulas.add(f);
			}
		}
		return saidMessageFormulas;
	}

	public static Formula cloneRuleWithNewMessage(Formula oldFormula, String newMessage, String auxMessage) {
		if (!auxMessage.equals(""))
			newMessage = auxMessage + " " + newMessage;
		Formula newFormula = new Formula(oldFormula.getPrincipal(), oldFormula.getFromPrincipal(),
				oldFormula.getOperator(), newMessage, oldFormula.getKey());
		return newFormula;
	}
}
