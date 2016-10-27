package notpaper.tech.voterpdf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

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
		
		/*
		//TODO remove this DEBUG code for pdfbox
		PDDocument doc = PDDocument.load(inputFile);
	    String text = new PDFTextStripper().getText(doc);
	    try (PrintStream out = new PrintStream(new FileOutputStream(outputFilePath))) {
	        out.print(text);
	    }
	    */
		
		//create the VoterFile object
		VoterFile voterFile = new VoterFile(inputFile);
		
		//TODO parse the VoterFile into Voters
		for(Voter v : voterFile) {
			break;
		}
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
