package tech.notpaper.voterpdf.models;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Voter {
	
	private String street;
	private String name;
	private int seqNo;
	private String party = "";
	private String streetNo;
	private String unit = "";
	
	private static final Pattern namePattern = Pattern.compile("^(\\D+)");
	
	protected Voter(String street, String line) {
		String origLine = line;
		this.street = street;
		
		if (line.startsWith("*")) {
			line = line.substring(1);
		}
		
		line = line.trim();
		System.out.println(line);
		
		Matcher m = namePattern.matcher(line);
		int end;
		if (m.find()) {
			this.name = m.group(1);
			end = m.end();
		} else {
			throw new IllegalArgumentException("The input line is not in the correct format: [" + origLine + "]");
		}
		
		line = line.substring(end);
		
		String[] tokens = line.split(" ");
		try {
			
			this.seqNo = Integer.parseInt(tokens[0]);
			this.party = tokens[1];
			if (tokens.length > 2) {
				this.streetNo = tokens[2];
			}
			if (tokens.length > 3) {
				this.unit =  tokens[3].trim().equals("BASEMEN") ? "BASEMENT" : tokens[3];
			}
		} catch (IndexOutOfBoundsException e) {
			throw new IllegalArgumentException("The input line is not in the correct format: [" + origLine + "]", e);
		}
	}

	protected Voter(String street, String line, int seqNo) {
		String origLine = line;
		this.street = street;
		
		if (line.startsWith("*")) {
			line = line.substring(1);
		}
		
		line = line.trim();
		System.out.println(line);
		
		Matcher m = namePattern.matcher(line);
		int end;
		if (m.find()) {
			this.name = m.group(1);
			end = m.end();
		} else {
			throw new IllegalArgumentException("The input line is not in the correct format: [" + origLine + "]");
		}
		
		line = line.substring(end);
		//line will look like "51"
		this.streetNo = line.substring(0,line.length()-Integer.toString(seqNo).length());
		this.seqNo = seqNo;
		
	}

	public String getStreet() {
		return street;
	}

	public String getName() {
		return name;
	}

	public int getSeqNo() {
		return seqNo;
	}

	public String getParty() {
		return party;
	}

	public String getStreetNo() {
		return streetNo;
	}

	public String getUnit() {
		return unit;
	}
	
	public String asCSVRow() {
		return asCSVRow(",");
	}
	
	public String asCSVRow(String delimiter) {
		StringBuilder sb = new StringBuilder();
		appendWithComma(sb, name);
		appendWithComma(sb, street);
		appendWithComma(sb, Integer.toString(seqNo));
		appendWithComma(sb, party);
		if (!unit.isEmpty()) {
			appendWithComma(sb, streetNo);
			sb.append(unit);
		} else {
			sb.append(streetNo);
		}
		
		return sb.toString();
	}
	
	private static void appendWithComma(StringBuilder sb, String string) {
		sb.append(string);
		sb.append(",");
	}
	
	private static final String[] headers = {"NAME", "STREET", "SEQ_NO", "PARTY", "STREET_NO"};
	private static final List<String> headersList = Arrays.asList(headers);
	public static List<String> getCSVHeaders() {
		return headersList;
	}
}
