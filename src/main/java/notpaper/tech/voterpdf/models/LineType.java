package notpaper.tech.voterpdf.models;

public enum LineType {
	
	STREET, VOTER, MISC;
	
	private static final String TWO_SPACE = "  ";
	private static final String ASTERISK = "*";
	
	public static LineType getLineType(String line) {
		if (isVoter(line)) {
			return VOTER;
		} else if (isStreet(line)) {
			return STREET;
		} else {
			return MISC;
		}
	}
	
	//TODO implement logic
	private static boolean isStreet(String line) {
		if (line.endsWith("(Cont..)")) {
			return true;
		}
		
		return false;
	}
	
	//TODO implement logic
	private static boolean isVoter(String line) {
		return line.startsWith(TWO_SPACE) || line.startsWith(ASTERISK);
	}
}
