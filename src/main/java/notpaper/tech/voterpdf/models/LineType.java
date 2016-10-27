package notpaper.tech.voterpdf.models;

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

	private static boolean isStreet(String line) {
		if (line.endsWith("(Cont..)")) {
			return true;
		}
		
		if (65 <= line.charAt(0) && line.charAt(0) <= 90 && !line.contains("#")) {
			return true;
		}
		
		return false;
	}
	
	//TODO implement logic
	private static boolean isVoter(String line) {
		return line.startsWith(TWO_SPACE) || line.startsWith(ASTERISK);
	}
}
