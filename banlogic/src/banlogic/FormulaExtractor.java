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

	public static List<Rule> extractProtocolFormulas() throws FileNotFoundException, IOException {
		List<Rule> protocolFormulas = new ArrayList<Rule>();

		try (BufferedReader br = new BufferedReader(new FileReader(protocolFilename))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				List<Rule> currentLineRules = extractRules(currentLine);
				protocolFormulas.addAll(currentLineRules);
			}
		}

		return protocolFormulas;
	}

	public static List<Rule> extractAssumptions() throws FileNotFoundException, IOException {
		List<Rule> assumptions = new ArrayList<Rule>();
		try (BufferedReader br = new BufferedReader(new FileReader(assumptionsFilename))) {
			String currentLine;
			while ((currentLine = br.readLine()) != null) {
				String[] splitAssumptions = currentLine.split(",");
				assumptions.add(createRule(splitAssumptions[0].trim(), splitAssumptions[1].trim(),
						splitAssumptions[2].trim(), ""));
			}
		}
		return assumptions;
	}
	
	public static List<Rule> getFormulasForNextMessage() throws FileNotFoundException, IOException{
		List<Rule> protocolFormulas = new ArrayList<Rule>();

		try (BufferedReader br = new BufferedReader(new FileReader(protocolFilename))) {
			String currentLine = "";
			for(int counter = 0; counter < currentMessage; counter++){
				currentLine = br.readLine();
			}
			
			List<Rule> currentLineRules = extractRules(currentLine);
			protocolFormulas.addAll(currentLineRules);
			currentMessage++;
		}
		return protocolFormulas;
	}

	public static List<Rule> extractRules(String currentLine) {
		String firstPrincipal = extractFirstPrincipal(currentLine);
		String secondPrincipal = extractSecondPrincipal(currentLine);
		String message = extractMessage(currentLine);
		String messageKey = extractMessageKey(currentLine);

		Rule[] rules = { createRule(firstPrincipal, Tokens.said, message, messageKey),
				createRule(secondPrincipal, Tokens.sees, message, messageKey) };

		return Arrays.asList(rules);
	}

	public static String extractMessage(String currentLine) {
		int messageToIndex = currentLine.indexOf(":");
		String message = currentLine.substring(messageToIndex + 1);
		int a = message.indexOf('{');
		int b = message.lastIndexOf('}');
		return message.substring(a + 1, b);
	}

	public static String extractMessageKey(String message) {
		int keyTokenIndex = message.lastIndexOf("}");
		return message.substring(keyTokenIndex + 1);
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

	public static Rule createRule(String principal, String operator, String message, String key) {
		Rule rule = new Rule(principal, operator, message, key);
		return rule;
	}
}
