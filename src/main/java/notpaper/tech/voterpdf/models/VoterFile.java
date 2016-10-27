package notpaper.tech.voterpdf.models;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;

public class VoterFile implements Iterable<Voter> {
	private static final String NOT_YET_PROCESSED = "NOT_YET_PROCESSED";
	
	private File inputFile;
	private String assemblyNumber;
	
	public VoterFile(File inputFile) {
		this.inputFile = inputFile;
		this.assemblyNumber = NOT_YET_PROCESSED;
	}
	
	public String getAssemblyNumber() {
		return this.assemblyNumber;
	}

	@Override
	public Iterator<Voter> iterator() {
		return new VoterFileIterator(inputFile);
	}
	
	private class VoterFileIterator implements Iterator<Voter> {
		
		private String street = null;
		private BufferedReader br;
		private boolean done = false;
		
		public VoterFileIterator(File inputFile) {
			try {
				br = new BufferedReader(new FileReader(inputFile));
			} catch (FileNotFoundException e) {
				throw new RuntimeException("File not found when attempting to parse", e);
			}
			
			//TODO parse through initial meta data to find first street
			//make sure we grab the assemblyNumber
			try {
				String line = br.readLine();
				while(line != null) {
					
					switch(LineType.getLineType(line)) {
					case ASSEMBLY:
						assemblyNumber = line.substring(line.lastIndexOf(' '), line.length()).trim();
						break;
					case STREET:
						street = line.trim();
						break;
					default:
						continue;
					}
					
					//got the first street, stop parsing for now, meta data finished
					if (street != null) {
						break;
					}
				}
			} catch (IOException e) {
				throw new RuntimeException("IOException encountered while attempting to read input file", e);
			}
			
		}

		@Override
		public boolean hasNext() {
			return !done;
		}

		@Override
		public Voter next() {
			//if we're done iterating, raise NoSuchElementException
			if(!hasNext()) {
				throw new NoSuchElementException("There are no more lines to read");
			}
			
			//establish a variable of type Voter to eventually be returned
			Voter v = null;
			
			//try block is intended to catch all IOException that may occur during file reading
			try {
				String line = br.readLine();
				while(line != null) {
					
					switch(LineType.getLineType(line)) {
					case STREET:
						//upate the street value
						street = line;
						break;
						
					case VOTER:
						//construct a voter
						v = new Voter(line);
						break;
						
					default:
						//ignore for now unless Tom wants meta data
						break;
					}
					
					//if we found a voter, break out of loop
					if (v != null) {
						break;
					}
				}
				
				if (line == null) {
					done = true;
					IOUtils.closeQuietly(br);
				}
			} catch (IOException e) {
				IOUtils.closeQuietly(br);
				
				throw new RuntimeException("IOException encountered while attempting to read input file", e);
			}
			return v;
		}
	}
}
