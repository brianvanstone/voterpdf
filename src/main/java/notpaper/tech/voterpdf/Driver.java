package notpaper.tech.voterpdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import notpaper.tech.voterpdf.models.Voter;
import notpaper.tech.voterpdf.models.VoterFile;

/**
 * Entry point for parsing voter data
 *
 */
@SuppressWarnings("deprecation")
public class Driver 
{
	
	private static final String ARG_INPUT_FILE = "i";
    private static final String ARG_OUTPUT_FILE = "o";
    
    private static final String delimiter = ",";
	private static final String CRLF = "\r\n";
	
    public static void main( String[] argsIn ) throws IOException
    {
    	//parse command line arguments
        CommandLine args = parseArgs(argsIn);
        
        //get input pdf file path from args
        String inputFilePath = args.getOptionValue(ARG_INPUT_FILE);
					
		//get output file path from args
		String outputFilePath = args.getOptionValue(ARG_OUTPUT_FILE);
					
		//Check that input file actually exists
		File inputFile = new File(inputFilePath);
		if(!inputFile.exists()) {
			throw new FileNotFoundException("The file indicated by '" + inputFilePath + "' could not be found.");
		}
		
		//scrub the PDF into flat text
		PDDocument doc = PDDocument.load(inputFile);
	    String scrubbedContents = new PDFTextStripper().getText(doc);
		
		//create the VoterFile object
		VoterFile voterFile = new VoterFile(scrubbedContents);
		
		//TODO parse the VoterFile into Voters
		//and write the voters to file
		int i = 0;
		
		BufferedWriter bw =  new BufferedWriter(new FileWriter(outputFilePath));
		bw.write(String.join(delimiter,Voter.getCSVHeaders()));
		bw.write(CRLF);
		for(Voter v : voterFile) {
			if (v == null) {
				break;
			}
			
			i++;
			
			bw.write(v.asCSVRow(delimiter));
			bw.write(CRLF);
			
			System.out.println(i);
		}
		bw.close();
		
		System.out.println("# Voters Found: " + i);
    }
    
	@SuppressWarnings("static-access")
	private static CommandLine parseArgs(String[] args) {
    	//create options for file path
    	Options options = new Options();
    	Option inputFile = OptionBuilder.withArgName("file").isRequired().hasArg().withDescription("The input pdf file to be used").create(ARG_INPUT_FILE);
    	options.addOption(inputFile);
    	Option outputFile = OptionBuilder.withArgName("file").isRequired().hasArg().withDescription("The output file to be written").create(ARG_OUTPUT_FILE);
    	options.addOption(outputFile);
    	
    	//establish parser
    	CommandLineParser parser = new DefaultParser();
    	CommandLine cmd = null;
    	try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception encountered during command line parsing", e);
		}
    	
    	//return cmd object containing args
    	return cmd;
    }
}
