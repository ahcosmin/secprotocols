package banlogic;

import java.util.ArrayList;
import java.util.List;

public class FormulaApplier {

	private List<Formula> belives;
	
	FormulaApplier( List<Formula> assumptions){
		this.belives=new ArrayList<Formula>(assumptions);
	}
	//R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X
	public Formula applyR1Rule(Formula pr){
		Formula result=null;
		if(pr.getOperator()==Tokens.sees){
			System.out.println("	Try to apply formula R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X");
			String message=pr.getPrincipal()+"<->("+pr.getKey()+")"+pr.getFromPrincipal();
			Formula r= new Formula(pr.getPrincipal(),"",Tokens.believes,message,"");
			if(belives.contains(r)){
				String resultMessage=pr.getFromPrincipal()+" "+Tokens.said+" "+pr.getMessage();
				result=new Formula(pr.getPrincipal(),"", Tokens.believes,resultMessage,"");
				result.setCanApplyRule(true);
				belives.add(result);
				System.out.println("	"+r.toString()+" "+ pr.toString()+ "=>"+ result.toString());
			}
		}
		return result;
	}
}
