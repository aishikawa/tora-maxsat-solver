package main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;
import maxsat_solver.algorithms.Algorithm;
import maxsat_solver.algorithms.GeneticLocalSearch;
import maxsat_solver.algorithms.IteratedLocalSearch;
import maxsat_solver.algorithms.MultiStartLocalSearch;
import maxsat_solver.algorithms.SimulatedAnnealing;
import maxsat_solver.algorithms.TabuSearch;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	
	private static final String usage = "java -jar maxsat.jar -instance filename [options]";
	
	public static void main(String[] args) {
		Options options = new Options();
		
		Option help = new Option("help", "print this message");
		options.addOption(help);
		
		Option instanceOpt = OptionBuilder.withArgName("filename")
				.hasArg()
				.withDescription("instance file")
				.create("instance");
		options.addOption(instanceOpt);
		
		Option algorithmOpt = OptionBuilder.withArgName("algorithm")
				.hasArg()
				.withDescription("algorithm")
				.create("algorithm");
		options.addOption(algorithmOpt);
		
		Option randomOpt = OptionBuilder.withArgName("seed")
				.hasArg()
				.withDescription("seed of random number generator")
				.create("seed");
		options.addOption(randomOpt);
		
		Option timeOpt = OptionBuilder.withArgName("timelimit")
				.hasArg()
				.withDescription("timelimit in seconds")
				.create("timelimit");
		options.addOption(timeOpt);

		CommandLineParser parser = new BasicParser();
		CommandLine commandLine = null;
		try {
			commandLine = parser.parse(options, args);
		} catch (ParseException e) {
			System.err.println("Parsing failed. Reason: " + e.getMessage());
			System.exit(1);
		}
		
		if (commandLine.hasOption("help")) {
			HelpFormatter formatter = new HelpFormatter();
			formatter.printHelp(usage, options);
			System.exit(0);
		}
		
		String filename = null;
		Instance instance = null;
		if (commandLine.hasOption("instance")) {
			try {
				filename = commandLine.getOptionValue("instance");
				BufferedReader br = new BufferedReader(new FileReader(filename));
				List<String> data = new LinkedList<String>();				
				String line = br.readLine();
				while (line != null) {
					data.add(line);
					line = br.readLine();
				}				
				instance = Instance.makeInstance(data);
				br.close();
				
			} catch (FileNotFoundException e) {
				System.err.println("error: instance file " + filename + " was not found");
				System.exit(1);
			} catch (IOException e) {
				System.err.println("IOException");
				System.exit(1);
			}
		} else {
			System.err.println("usage: "+usage);
			System.exit(1);
		}
		
		Random random = new Random(1);
		if (commandLine.hasOption("seed")) {
			long seed = 0;
			try {
				seed = Long.parseLong(commandLine.getOptionValue("seed"));
			} catch (NumberFormatException e) {
				System.err.println("NumberFormatException");
				System.exit(1);
			}
			random.setSeed(seed);
		}		
		
		ReadParameter property = new ReadParameter();
		try {
			property.readPropeties("maxsat_solver.properties");
		} catch (FileNotFoundException e) {
			System.err.println("maxsat_solver.properties was not found");
			System.exit(1);
		} catch (IOException e) {
			System.err.println("error: fail to read maxsat_solver.properties");
			System.exit(1);
		}
		
		Algorithm algorithm = null;
		if (commandLine.hasOption("algorithm")) {			
			String algorithmStr = commandLine.getOptionValue("algorithm");			
			if (algorithmStr.equals("mls")) {
				// multi-start local search
				MultiStartLocalSearch mls = new MultiStartLocalSearch(instance, random);
				algorithm = mls;				
			} else if (algorithmStr.equals("ils")) {
				// iterated local search
				IteratedLocalSearch ils = new IteratedLocalSearch(instance, random);
				ils.setChangeRation(property.CHANGE_RATIO);
				algorithm = ils;
			} else if (algorithmStr.equals("gls")) {
				// genetic local search
				GeneticLocalSearch gls = new GeneticLocalSearch(instance, random);
				gls.setRatio(property.POPULATION_RATIO);
				algorithm = gls;
			} else if (algorithmStr.equals("ts")) {
				// tabu search
				TabuSearch ts = new TabuSearch(instance, random);
				ts.setTabuTenureRatio(property.TABU_TENURE_RATIO);
				algorithm = ts;
			} else if (algorithmStr.equals("sa")) {				
				// simulated annealing
				SimulatedAnnealing sa = new SimulatedAnnealing(instance, random);
				sa.setCoolingFactor(property.COOLING_FACTOR);
				sa.setAlpha(property.ALPHA);
				sa.setInitialMovingProbability(property.INITIAL_MOVING_PROBABILITY);
				algorithm = sa;
			} else {
				System.err.println("Algorithm option error \""+algorithmStr+"\" ");
			}
		} else {
			System.err.println("error: missing algorithm option");
			System.err.println("usage: "+usage);
			System.exit(1);
		}
		
		if (commandLine.hasOption("timelimit")) {
			try {
				int timelimit = Integer.parseInt(commandLine.getOptionValue("timelimit"));
				algorithm.setTimeLimit(timelimit * 1000);
			} catch (NumberFormatException e) {
				System.err.println("NumberFormatException timilimit");
				System.exit(1);
			}
		}
		
		Solution sol = algorithm.start();
		Util.printValues(sol.assignment);
	}
}
