package notpaper.tech.voterpdf.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;

public class VoterFile implements Iterable<Voter> {
	private File inputFile;
	
	public VoterFile(File inputFile) {
		this.inputFile = inputFile;
	}

	@Override
	public Iterator<Voter> iterator() {
		return new VoterFileIterator(inputFile);
	}
	
	private class VoterFileIterator implements Iterator<Voter> {
		
		private String street;
		private BufferedReader br;
		private boolean done = false;
		
		public VoterFileIterator(File inputFile) {
			//TODO fetch meta data here if Tom say's he wants it
			try {
				br = new BufferedReader(new FileReader(inputFile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("File not found when attempting to parse", e);
			}
		}

		@Override
		public boolean hasNext() {
			return !done;
		}

		@Override
		public Voter next() {
			try {
				String line = br.readLine();
				while(line != null) {
					
					//determine if the current line being read is a street
					if(isStreet(br.readLine())) {
						
					}
				}
				
				if (line == null) {
					done = true;
				}
			} catch (IOException e) {
				throw new RuntimeException("IOException encountered while attempting to read input file", e);
			}
			return null;
		}
	}
	
	//TODO implement logic
	private static boolean isStreet(String line) {
		return true;
	}
	
	//TODO implement logic
	private static boolean isVoter(String line) {
		return true;
	}
	
	//TODO implement logic
	private static String lineToStreet(String line) {
		return "";
	}
}
