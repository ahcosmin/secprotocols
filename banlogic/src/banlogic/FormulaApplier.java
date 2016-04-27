package banlogic;

import java.util.ArrayList;
import java.util.List;

public class FormulaApplier {

	private List<Formula> believes;
	
	FormulaApplier( List<Formula> assumptions){
		this.believes=new ArrayList<Formula>(assumptions);
	}
	//R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X
	public Formula applyR1Rule(Formula pr){
		System.out.println("	Try to apply formula R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X");
		Formula result=null;
		if(pr.getOperator()==Tokens.sees){
			String message=pr.getPrincipal()+"<->("+pr.getKey()+")"+pr.getFromPrincipal();
			Formula r= new Formula(pr.getPrincipal(),"",Tokens.believes,message,"");
			if(believes.contains(r)){
				String resultMessage=pr.getFromPrincipal()+" "+Tokens.said+" "+pr.getMessage();
				result=new Formula(pr.getPrincipal(),"", Tokens.believes,resultMessage,"");
				result.setCanApplyRule(true);
				believes.add(result);
				System.out.println("	"+r.toString()+" "+ pr.toString()+ "=>"+ result.toString());
			}
		}
		return result;
	}
	
	// R13 X,Y=> X - conjunction rule
		public List<Formula> applyR13Rule(Formula oldFormula) {
			System.out.println("	Try to apply formula R13 X,Y=> X; X,Y=>Y - conjunction rule");
			if(!oldFormula.getKey().equals("")){
				return new ArrayList<Formula>();
			}
			List<Formula> list= new ArrayList<Formula>();
			
			String oldformulaMessage = oldFormula.getMessage();
			String message=oldformulaMessage;
			String auxMessage="";
			for (String	predicate : Tokens.predicates) {
				if(oldformulaMessage.contains(predicate))
				{
					int index=oldformulaMessage.indexOf(predicate);
					auxMessage=oldformulaMessage.substring(0, index+predicate.length());
					message=oldformulaMessage.substring(index+predicate.length()+1);
					break;
				}
			}
			
			while (message.contains("{")) {
				int startIndexOfEncryptedSubmessage = message.indexOf('{');
				if(!message.substring(0,startIndexOfEncryptedSubmessage).contains(","))
					return new ArrayList<Formula>();
				int endIndexOfEncryptedSubmessage = message.indexOf('}');
				int firstCommaAfterEncryptedSubmessage = message.indexOf(",", endIndexOfEncryptedSubmessage);
				String submessage = "";
				if (firstCommaAfterEncryptedSubmessage != -1)
					submessage = message.substring(startIndexOfEncryptedSubmessage, firstCommaAfterEncryptedSubmessage);
				else
					submessage = message.substring(startIndexOfEncryptedSubmessage);
				Formula result=FormulaUtils.cloneRuleWithNewMessage(oldFormula, submessage, auxMessage);
				System.out.println("	"+oldFormula.toString()+ "=>"+ result.toString());
				believes.add(result);
				list.add(result);
				message = message.replace(submessage, "");
				Formula result2=FormulaUtils.cloneRuleWithNewMessage(oldFormula, message.substring(0,message.length()-1), auxMessage);
				System.out.println("	"+oldFormula.toString()+ "=>"+ result2.toString());
				believes.add(result2);
				list.add(result2);
				break;
			}

			if (message.contains(",")) {
				String[] splitMessage = message.split(",");
				for (String newMessage : splitMessage) {
					Formula result=FormulaUtils.cloneRuleWithNewMessage(oldFormula, newMessage, auxMessage);
					System.out.println("	"+oldFormula.toString()+ "=>"+ result.toString());
					believes.add(result);
					list.add(result);
				}
			}
			return list;
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
		public void applyR4Rule(Formula oldFormula){
			System.out.println(believes);
		}

		//R5 P belives Q controls X, P belives Q belives X => P belives X 
		public Formula applyR5Rule(Formula oldFormula){
			System.out.println("	Try to apply formula R5 P belives Q controls X, P belives Q belives X => P belives X");
			if(oldFormula.getOperator()!=Tokens.believes)
				return null;
			String formulaMessage=oldFormula.getMessage();
			if(formulaMessage.matches("(.*) "+Tokens.believes+" (.*)")){
				String[] messages=oldFormula.getMessage().split(" ");
				String agent=messages[0];
				String message=messages[2];
				String newMessage=agent +" "+Tokens.believes+" "+message;
				Formula searhFormula=new Formula(oldFormula.getPrincipal(),"",Tokens.controls,newMessage,"");
				if(believes.contains(searhFormula)){
					Formula result=new Formula(oldFormula.getPrincipal(),"",Tokens.believes,message,"");
					System.out.println("	"+oldFormula.toString()+" "+ searhFormula.toString()+ "=>"+ result.toString());
					return result;
				}
			}
			if(formulaMessage.matches("(.*) "+Tokens.controls+" (.*)")){
				String[] messages=oldFormula.getMessage().split(" ");
				String agent=messages[0];
				String message=messages[2];
				String newMessage=agent +" "+Tokens.believes+" "+message;
				Formula searhFormula=new Formula(oldFormula.getPrincipal(),"",Tokens.believes,newMessage,"");
				if(believes.contains(searhFormula)){
					Formula result=new Formula(oldFormula.getPrincipal(),"",Tokens.believes,message,"");
					System.out.println("	"+oldFormula.toString()+" "+ searhFormula.toString()+ "=>"+ result.toString());
					return result;
				}
			}
			return null;
		}
		
		public Formula mergeFormulas(Formula freshFormula, Formula believeFormula) {
			String message = composeMessage(freshFormula, believeFormula);
			Formula newFormula = new Formula(freshFormula.getPrincipal(), freshFormula.getFromPrincipal(),
					believeFormula.getOperator(), message, "");
			System.out.println(newFormula);
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

}
