package tech.notpaper.voterpdf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;

import tech.notpaper.voterpdf.models.Voter;
import tech.notpaper.voterpdf.models.VoterFile;

/**
 * Entry point for parsing voter data
 *
 */
@SuppressWarnings("deprecation")
public class Driver {

	private static final String ARG_INPUT_FILE = "i";
	private static final String ARG_OUTPUT_FILE = "o";
	private static final String ARG_INPUT_DIR = "inputDir";
	
	private static final String delimiter = ",";
	private static final String CRLF = "\r\n";
	private static boolean debug = true;
	

	public static void main(String[] argsIn) throws IOException {
		// parse command line arguments
		CommandLine args = parseArgs(argsIn);

		if (args.hasOption("inputDir")) {
			
			//grab the input Directory
			File inputDir = new File(args.getOptionValue(ARG_INPUT_DIR));
			
			if (!inputDir.isDirectory()) {
				throw new IllegalArgumentException("The path denoted by '" + args.getOptionValue(ARG_INPUT_DIR) + "' is not a valid directory.");
			}
			
			for (final File fileEntry : inputDir.listFiles()) {
				String inFileName = fileEntry.getName();
				String outFileName = inFileName.substring(0, inFileName.lastIndexOf(".")) + ".csv";
				processFile(fileEntry.getAbsolutePath(), Paths.get(inputDir.getAbsolutePath(), "output", outFileName).toString());
			}
		} else {
			processFile(args.getOptionValue(ARG_INPUT_FILE), args.getOptionValue(ARG_OUTPUT_FILE));
		}
	}

	private static void processFile(String inputFilePath, String outputFilePath) throws IOException {
		// Check that input file actually exists
		File inputFile = new File(inputFilePath);
		if (!inputFile.exists()) {
			throw new FileNotFoundException("The file indicated by '" + inputFilePath + "' could not be found.");
		}

		// scrub the PDF into flat text
		PDDocument doc = PDDocument.load(inputFile);
		String scrubbedContents = new PDFTextStripper().getText(doc);
		
		if(debug) {
			File debugFile = new File("C:\\Users\\Brian\\workspace_voter_data\\voterpdf\\temp.txt");
			try (BufferedWriter dbw = new BufferedWriter(new FileWriter(debugFile)) ) {
				dbw.write(scrubbedContents);
			}
			
		}

		// create the VoterFile object
		VoterFile voterFile = new VoterFile(scrubbedContents);

		// debug counter
		int i = 0;

		BufferedWriter bw = new BufferedWriter(new FileWriter(outputFilePath));
		bw.write(String.join(delimiter, Voter.getCSVHeaders()));
		bw.write(CRLF);
		for (Voter v : voterFile) {
			if (v == null) {
				break;
			}

			bw.write(v.asCSVRow(delimiter));
			bw.write(CRLF);

			if (debug) {
				i++;
				//System.out.println(i);
			}
		}
		bw.close();

		if (debug) {
			System.out.println("# Voters Found: " + i);
		}
	}

	@SuppressWarnings("static-access")
	private static CommandLine parseArgs(String[] args) {
		// create options for file path
		Options options = new Options();
		Option inputFile = OptionBuilder.withArgName("file").hasArg()
				.withDescription("The input pdf file to be used").create(ARG_INPUT_FILE);
		options.addOption(inputFile);
		Option outputFile = OptionBuilder.withArgName("file").hasArg()
				.withDescription("The output file to be written").create(ARG_OUTPUT_FILE);
		options.addOption(outputFile);
		Option inputDir = OptionBuilder.withArgName("inputDir").hasArg()
				.withDescription("The batch input directory from which every file is to be processed. Output will go to this dir/output")
				.create(ARG_INPUT_DIR);
		options.addOption(inputDir);

		// establish parser
		CommandLineParser parser = new DefaultParser();
		CommandLine cmd = null;
		try {
			cmd = parser.parse(options, args);
		} catch (ParseException e) {
			e.printStackTrace();
			throw new RuntimeException("Exception encountered during command line parsing", e);
		}

		// return cmd object containing args
		return cmd;
	}
}
