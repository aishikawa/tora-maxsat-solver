package maxsat_solver.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;

public class SimulatedAnnealing extends Algorithm {

	private double COOLING_FACTOR = 0.95;
	private double ALPHA = 10.0;
	private double INITIAL_MOVING_PROBABILITY = 0.9;
	
	private int[] flipVariable;
	
	private Random rand;
	
	private Solution incumbent; 
	
	public SimulatedAnnealing(Instance ins, Random r) {
		super(ins);
		rand = r;
		
		int numVariables = instance.getNumValiables();
		flipVariable = new int[numVariables];
		List<Integer> list = new ArrayList<Integer>(numVariables);
		for (int i=0; i<numVariables; i++) {
			list.add(i);
		}		
		Collections.shuffle(list, rand);
		for (int i=0; i<numVariables; i++) {
			flipVariable[i] = list.get(i);
		}
		
		boolean[] randamAssignment = Util.getRandomAssignment(numVariables, rand);
		long uv = instance.getUnsatisfiedValue(randamAssignment);
		incumbent = new Solution(randamAssignment, uv);
	}

	@Override
	public Solution start() {
		long sTime = System.currentTimeMillis();
		double initialTemperature = initialTemperature();
		double temperature = initialTemperature;
		
		boolean[] initial = Util.getRandomAssignment(instance.getNumValiables(), rand);
		long initialUV = instance.getUnsatisfiedValue(initial);
		Solution current = new Solution(initial, initialUV);
		
		while (System.currentTimeMillis() - sTime < TIME_LIMIT) {
			int numMove = search(current, temperature);
			if (numMove == 0) {
				temperature = initialTemperature;
			} else {
				temperature *= COOLING_FACTOR; 
			}
		}
		
		return incumbent;
	}
	

	private double initialTemperature() {
		int numVariables = instance.getNumValiables();
				
		boolean[] initialAssignment = Util.getRandomAssignment(numVariables, rand);
		long initialUV = instance.getUnsatisfiedValue(initialAssignment);
	
		double min = 0.0;
		double max = initialUV;
		
		double temperature = 0.0;
		int size = (int)(numVariables * ALPHA);
		while ((double)max/min > 1.01) {
			temperature = (min + max) / 2;
			int numMove = search(new Solution(Arrays.copyOf(initialAssignment, numVariables), initialUV), temperature);
			if ((double)numMove/size < INITIAL_MOVING_PROBABILITY) {
				min = temperature;
			} else {
				max = temperature;
			}
		}
		
		return temperature;
	}
	
	
	private int search(Solution initial, double temperature) {
		int numVariables = instance.getNumValiables();
		Solution current = initial;
		
		int size = (int)(numVariables * ALPHA);
		int numMove = 0;
		for (int index=0; index<size; index++) {
			int flipIndex = flipVariable[index % numVariables];
			long diff = instance.getFlipDifference(current.assignment, flipIndex);
			if (diff < 0 || (diff > 0 && rand.nextDouble() < Math.exp(-diff/temperature))) {
				current.assignment[flipIndex] = !current.assignment[flipIndex];
				current.unsatisfiedValue += diff;
				numMove += 1;
				if (current.unsatisfiedValue < incumbent.unsatisfiedValue) {
					incumbent = new Solution(current);
					System.out.println("o "+current.unsatisfiedValue);
				}
			}
		}
		
		return numMove;
	}
	
	public void setCoolingFactor(double f) {
		COOLING_FACTOR = f;
	}
	
	public void setAlpha(double a) {
		ALPHA = a;
	}
	
	public void setInitialMovingProbability(double p) {
		INITIAL_MOVING_PROBABILITY = p;
	}

}
