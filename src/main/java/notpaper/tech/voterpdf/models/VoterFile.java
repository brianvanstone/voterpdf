package notpaper.tech.voterpdf.models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.NoSuchElementException;

import org.apache.commons.io.IOUtils;

public class VoterFile implements Iterable<Voter> {
	private static final String NOT_YET_PROCESSED = "NOT_YET_PROCESSED";
	
	private String assemblyNumber;
	private String scrubbedContents;
	
	public VoterFile(String scrubbedContents) {
		this.scrubbedContents = scrubbedContents;
		this.assemblyNumber = NOT_YET_PROCESSED;
	}
	
	public String getAssemblyNumber() {
		return this.assemblyNumber;
	}

	@Override
	public Iterator<Voter> iterator() {
		return new VoterFileIterator(scrubbedContents);
	}
	
	private class VoterFileIterator implements Iterator<Voter> {
		
		private String street = null;
		private BufferedReader br;
		private boolean done = false;
		
		public VoterFileIterator(String inputFile) {
			br = new BufferedReader(new StringReader(inputFile));
			
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
						break;
					}
					
					//got the first street, stop parsing for now, meta data finished
					if (street != null) {
						break;
					}
					
					line = br.readLine();
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
						v = new Voter(street, line);
						break;
						
					default:
						//ignore for now unless we want meta data
						break;
					}
					
					//if we found a voter, break out of loop
					if (v != null) {
						break;
					}
					
					line = br.readLine();
				}
				
				if (line == null) {
					done = true;
					IOUtils.closeQuietly(br);
				}
			} catch (IOException e) {
				IOUtils.closeQuietly(br);
				
				throw new RuntimeException("IOException encountered while attempting to read the pre-processed pdf file", e);
			}
			return v;
		}
	}
}
