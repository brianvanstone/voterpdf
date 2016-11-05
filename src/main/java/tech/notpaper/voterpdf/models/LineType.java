package tech.notpaper.voterpdf.models;

import java.util.regex.Pattern;

public enum LineType {
	
	BLANK, STREET, VOTER_NOSEQ, VOTER, ASSEMBLY, MISC;
	
	private static final String TWO_SPACE = "  ";
	private static final String ASTERISK = "*";
	
	public static LineType getLineType(String line) {
		if ((line == null) || isblank(line)) {
			return BLANK;
		} else if (isVoterNoSeq(line)) {
			return VOTER_NOSEQ;
		} else if (isVoter(line)) {
			return VOTER_NOSEQ;
		} else if (isStreet(line)) {
			return STREET;
		} else if (isAssemblyNum(line)) {
			return ASSEMBLY;
		} else {
			return MISC;
		}
	}
	
	private static boolean isblank(String line) {
		return line.trim().isEmpty();
	}

	private static boolean isAssemblyNum(String line) {
		return line.startsWith("Assembly");
	}
	
	private static final Pattern voterNoSeqPattern = Pattern.compile("[\\*A-Za-z\\- ]+\\d+");
	private static boolean isVoterNoSeq(String line) {
		return voterNoSeqPattern.matcher(line).matches();
	}

	private static final Pattern streetPattern = Pattern.compile("[A-Z ]+");
	private static boolean isStreet(String line) {
		if (line.endsWith("(Cont..)")) {
			return true;
		}
		
		switch(line) {
		case "SELECTION CRITERIA":
		case "WE HEREBY CERTIFY THAT THE FOLLOWING LIST OF ELECTORS IS COMPLETE AND CORRECT AS OF THIS DATE":
		case "REGISTRARS OF VOTERS":
			return false;
		}
		 
		if (streetPattern.matcher(line).matches()) {
			return true;
		}
		
		return false;
	}
	
	private static boolean isVoter(String line) {
		return !line.startsWith("* Indicates") && (line.startsWith(TWO_SPACE) || line.startsWith(ASTERISK));
	}
}
