package notpaper.tech.voterpdf.models;

import java.util.regex.Pattern;

public enum LineType {
	
	STREET, VOTER, ASSEMBLY, MISC;
	
	private static final String TWO_SPACE = "  ";
	private static final String ASTERISK = "*";
	
	public static LineType getLineType(String line) {
		if (isVoter(line)) {
			return VOTER;
		} else if (isStreet(line)) {
			return STREET;
		} else if (isAssemblyNum(line)) {
			return ASSEMBLY;
		} else {
			return MISC;
		}
	}
	
	private static boolean isAssemblyNum(String line) {
		return line.startsWith("Assembly");
	}

	private static final Pattern streetPattern = Pattern.compile("[A-Z ]+");
	private static boolean isStreet(String line) {
		if (line.endsWith("(Cont..)")) {
			return true;
		}
		
		 
		if (streetPattern.matcher(line).matches()) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isVoter(String line) {
		return line.startsWith(TWO_SPACE) || line.startsWith(ASTERISK);
	}
}
