package banlogic;

import java.util.ArrayList;
import java.util.List;

public class FormulaApplier {

	private List<Formula> believes;
	private List<Formula> protocol;

	FormulaApplier(List<Formula> assumptions) {
		this.believes = new ArrayList<Formula>(assumptions);
	}

	// R1 P belives Q<->(K)P, P sees {X}K => P belives Q said X
	public Formula applyR1Rule(Formula pr) {
		Formula result = null;
		if (pr.getOperator() == Tokens.sees) {
			System.out.println("	Try to apply formula R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X");
			String message = pr.getPrincipal() + "<->(" + pr.getKey() + ")" + pr.getFromPrincipal();
			Formula r = new Formula(pr.getPrincipal(), "", Tokens.believes, message, "");
			if (believes.contains(r)) {
				String resultMessage = pr.getFromPrincipal() + " " + Tokens.said + " " + pr.getMessage();
				result = new Formula(pr.getPrincipal(), "", Tokens.believes, resultMessage, "");
				result.setCanApplyRule(true);
				believes.add(result);
				System.out.println("	" + r.toString() + " " + pr.toString() + "=>" + result.toString());
			}
		}
		return result;
	}

	// R13 X,Y=> X - conjunction rule
	public void applyR13Rule(Formula oldFormula) {
		String message = oldFormula.getMessage();

		while (message.contains("{")) {
			int startIndexOfEncryptedSubmessage = message.indexOf('{');
			int endIndexOfEncryptedSubmessage = message.indexOf('}');
			int firstCommaAfterEncryptedSubmessage = message.indexOf(",", endIndexOfEncryptedSubmessage);
			String submessage = "";
			if (firstCommaAfterEncryptedSubmessage != -1)
				submessage = message.substring(startIndexOfEncryptedSubmessage, firstCommaAfterEncryptedSubmessage);
			else
				submessage = message.substring(startIndexOfEncryptedSubmessage);
			believes.add(FormulaUtils.cloneRuleWithNewMessage(oldFormula, submessage));
			message = message.replace(submessage, "");
			break;
		}

		if (message.contains(",")) {
			String[] splitMessage = message.split(",");
			for (String newMessage : splitMessage) {
				believes.add(FormulaUtils.cloneRuleWithNewMessage(oldFormula, newMessage));
			}
		}
	}

	// P believes fresh(X) => P believes fresh (X,Y)
	public void applyR12Rule(Formula oldFormula) {
		List<Formula> freshFormulas = FormulaUtils.getFreshFormulas(believes);
		List<Formula> believesFormulas = FormulaUtils.getBelievesFormulas(believes);

		for (Formula freshFormula : freshFormulas) {
			for (Formula believesFormula : believesFormulas) {
				if (freshFormula.getPrincipal().equals(believesFormula.getPrincipal())) {
					String freshMessage = extractParameter(freshFormula.getMessage());
					if (!believesFormula.getMessage().contains(freshMessage)
							&& !believesFormula.getMessage().equals(freshMessage))
						believes.add(mergeFormulas(freshFormula, believesFormula));
				}
			}
		}
	}

	// P believes fresh(X), P believes Q said X => P believes Q belives X
	public void applyR4Rule(Formula oldFormula) {
		List<Formula> believesFormulas = FormulaUtils.getBelievesFormulas(believes);
		List<Formula> freshMessageFormulas = FormulaUtils.getFormulasWithFreshMessage(believesFormulas);
		List<Formula> saidMessageFormulas = FormulaUtils.getFormulasWithSaidMessage(believesFormulas);

		for (Formula f1 : freshMessageFormulas) {
			for (Formula f2 : saidMessageFormulas) {
				if (f1.getPrincipal().equals(f2.getPrincipal())) {
					String freshParameter = extractParameter(f1.getMessage());
					String[] saidParameters = f2.getMessage().split(" ");

					if (freshParameter.equals(saidParameters[2])) {
						Formula newFormula = new Formula(f2.getPrincipal(), "", f1.getOperator(),
								saidParameters[0] + " " + Tokens.believes + " " + freshParameter, "");
						System.out.println(newFormula);
						believes.add(newFormula);
					}
				}
			}
		}
		
	}

	public Formula mergeFormulas(Formula freshFormula, Formula believeFormula) {
		String message = composeMessage(freshFormula, believeFormula);
		Formula newFormula = new Formula(freshFormula.getPrincipal(), freshFormula.getFromPrincipal(),
				believeFormula.getOperator(), message, "");
		return newFormula;
	}

	public String composeMessage(Formula freshFormula, Formula believesFormula) {
		String freshMessage = extractParameter(freshFormula.getMessage());
		return Tokens.fresh + "(" + freshMessage + "," + believesFormula.getMessage() + ")";
	}

	public String extractParameter(String message) {
		int startIndex = message.indexOf("(");
		int endIndex = message.indexOf(")");
		return message.substring(startIndex + 1, endIndex);
	}

	public void applyRules() {
		applyR1Rule(protocol.get(0));
		applyR13Rule(protocol.get(0));
		applyR12Rule(protocol.get(0));
		applyR4Rule(protocol.get(0));
	}
}
