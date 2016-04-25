package banlogic;

import java.util.ArrayList;
import java.util.List;

public class FormulaApplier {

	private List<Rule> protocol;
	private List<Rule> belives;
	
	FormulaApplier(List<Rule> protocol, List<Rule> assumptions){
		this.protocol=protocol;
		this.belives=new ArrayList<Rule>(assumptions);
	}
	//R1	P belives Q<->(K)P, P sees {X}K => P belives Q said X
	private Boolean applyR1Rule(Rule pr){
		Boolean validRule=false;
		if(pr.getOperator()==Tokens.sees){
			String message=pr.getPrincipal()+"<->("+pr.getKey()+")"+pr.getFromPrincipal();
			Rule r= new Rule(pr.getPrincipal(),"",Tokens.believes,message,"");
			if(belives.contains(r)){
				validRule=true;
				String resultMessage=pr.getFromPrincipal()+" "+Tokens.said+" "+pr.getMessage()+" "+pr.getKey();
				Rule result=new Rule(pr.getPrincipal(),"", Tokens.believes,resultMessage,"");
				belives.add(result);
				System.out.println(r.toString()+" "+ pr.toString()+ "=>"+ result.toString());
			}
		}
		return validRule;
	}
	public void applyRules(){
		applyR1Rule(protocol.get(0));
	}
}
