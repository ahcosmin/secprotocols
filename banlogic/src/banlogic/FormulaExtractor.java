package banlogic;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FormulaExtractor {
	private final static String protocolFilename = "protocol1.txt";
	private final static String assumptionsFilename = "assumptions1.txt";
	private static int currentMessage = 1;

	public static List<Formula> extractProtocolFormulas() throws FileNotFoundException, IOException {
		List<Formula> protocolFormulas = new ArrayList<Formula>();

		try (BufferedReader br = new BufferedReader(new FileReader(protocolFilename))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				List<Formula> currentLineRules = extractFormulas(currentLine);
				protocolFormulas.addAll(currentLineRules);
			}
		}

		return protocolFormulas;
	}

	public static List<Formula> extractAssumptions() throws FileNotFoundException, IOException {
		List<Formula> assumptions = new ArrayList<Formula>();
		try (BufferedReader br = new BufferedReader(new FileReader(assumptionsFilename))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				String[] splitAssumptions = currentLine.split(",");
				assumptions.add(createFormula(splitAssumptions[0].trim(),"", splitAssumptions[1].trim(),
						splitAssumptions[2].trim(), ""));
			}
		}
		return assumptions;
	}
	
	public static List<Formula> getFormulasForNextMessage() throws FileNotFoundException, IOException{
		List<Formula> protocolFormulas = new ArrayList<Formula>();

		try (BufferedReader br = new BufferedReader(new FileReader(protocolFilename))) {
			String currentLine = "";
			for(int counter = 0; counter < currentMessage; counter++){
				currentLine = br.readLine();
			}
			
			List<Formula> currentLineFormulas = extractFormulas(currentLine);
			protocolFormulas.addAll(currentLineFormulas);
			currentMessage++;
		}
		return protocolFormulas;
	}

	public static List<Formula> extractFormulas(String currentLine) {
		String firstPrincipal = extractFirstPrincipal(currentLine);
		String secondPrincipal = extractSecondPrincipal(currentLine);
		String message = extractMessage(currentLine);
		String messageKey = extractMessageKey(currentLine,message);
		//String from=extractFrom(currentLine);
		Formula[] rules = { //createRule(firstPrincipal, Tokens.said, message, messageKey),
				createFormula(secondPrincipal,firstPrincipal, Tokens.sees, message, messageKey) };

		return Arrays.asList(rules);
	}

	/*public static String extractFrom(String message){
		int keyTokenIndex = message.indexOf(" from");
		return message.substring(keyTokenIndex + 1);
	}*/
	
 	public static String extractMessage(String currentLine) {
		int messageToIndex = currentLine.indexOf(":");
		String message = currentLine.substring(messageToIndex + 1);
		int a = message.indexOf('{');
		int b = message.lastIndexOf('}');
		String newMessage=message.substring(a + 1, b);
		if(!newMessage.matches("(.*)\\}(.*)\\{(.*)")){
			return newMessage;
		}
		int c= message.indexOf(" from");
		return message.substring(0,c);
	}

	public static String extractMessageKey(String currentLine, String message) {
		if(message.matches("\\{(.*)\\}(.*)")){
			return "";
		}
		int keyTokenIndex = currentLine.lastIndexOf("}");
		int b= currentLine.indexOf(" from");
		if(b!=-1)
			return currentLine.substring(keyTokenIndex + 1,b);
		return currentLine.substring(keyTokenIndex + 1);
	}

	public static String extractFirstPrincipal(String message) {
		return createSplitPrincipals(message)[0];
	}

	public static String extractSecondPrincipal(String message) {
		return createSplitPrincipals(message)[1];
	}

	public static String[] createSplitPrincipals(String message) {
		int messageToIndex = message.indexOf(":");
		String principals = message.substring(0, messageToIndex);
		return principals.split("->");
	}

	public static Formula createFormula(String principal,String fromPrincipal,String operator, String message, String key) {
		Formula formula = new Formula(principal,	fromPrincipal, operator, message, key);
		return formula;
	}
}
