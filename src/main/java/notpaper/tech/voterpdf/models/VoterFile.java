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
					
					//TODO handle each different type of line
					switch(LineType.getLineType(line)) {
					case STREET:
						//TODO update the street value
						break;
					case VOTER:
						//create a new voter and return it, leave br open for future reading
						break;
					case MISC:
						//ignore for now unless Tom wants meta data
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
