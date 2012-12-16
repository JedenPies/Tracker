package pl.jedenpies.web.traces.validation;

public class SyntaxValidator {
	
	public static final SyntaxValidator USERNAME_VALIDATOR = 
			new SyntaxValidator(new Rule[] { Rule.NOT_EMPTY, Rule.ONLY_LETTERS_AND_DIGITS });
	public static final SyntaxValidator PASSWORD_VALIDATOR = 
			new SyntaxValidator(new Rule[] { Rule.AT_LEAST_ONE_DIGIT });
	public static final SyntaxValidator EMAIL_VALIDATOR =
			new SyntaxValidator(new Rule[] { Rule.EMAIL_ADDRESS });
	
	public static class Rule {
		
		public static final Rule ONLY_LETTERS_AND_DIGITS = new Rule("^[a-zA-Z0-9]+$");
		public static final Rule AT_LEAST_ONE_DIGIT = new Rule("^.*[0-9]+.*$");
		// TODO fix it it's not the best:
		public static final Rule EMAIL_ADDRESS = 
				new Rule("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
		public static final Rule NOT_EMPTY = 
				new Rule("^.+$");
		
		private String regex;
		private boolean negation;
		
		public Rule(String regex) {
			this(regex, false);
		}
	
		public Rule(String regex, boolean negation) {
			this.regex = regex;
			this.negation = negation;
		}
		
		public boolean checkRule(String text) {
			return negation ? !text.matches(regex) : text.matches(regex);
		}		
	}

	private Rule[] rules;
	
	public SyntaxValidator(Rule[] rules) {
		this.rules = rules;
	}
	
	public boolean validate(String text) {
		for (Rule rule : rules) {
			if (!rule.checkRule(text)) return false;
		}
		return true;
	}
}
