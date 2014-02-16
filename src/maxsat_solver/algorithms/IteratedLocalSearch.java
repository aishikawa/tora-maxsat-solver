package maxsat_solver.algorithms;


import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;

public class IteratedLocalSearch extends AlgorithmUsingOneFlipLocalSearch{
	private Random rand;
	
	private double CHANGE_RATIO = 0.3;	
	private List<Integer> changeIndex;
	
	public IteratedLocalSearch(Instance ins, Random r) {
		super(ins, r.nextLong());
		rand = r;
		
		changeIndex = new LinkedList<Integer>();
		for (int i=0; i<instance.getNumValiables(); i++) {
			changeIndex.add(i);
		}
	}
	
	public void setChangeRation(double r) {
		CHANGE_RATIO = r;
	}
	
	/**
	 * start iterated local search and return the best one among searched solutions;
	 */
	@Override
	public Solution start() {
		long sTime = System.currentTimeMillis();
		Solution best = null;
		boolean[] initial = Util.getRandomAssignment(instance.getNumValiables(), rand);
		
		while(System.currentTimeMillis() - sTime < TIME_LIMIT) {
			Solution localOpt = localSearch.getLocalOptimumSolution(initial);
			if (best == null || best.unsatisfiedValue > localOpt.unsatisfiedValue) {
				best = localOpt;
				System.out.println("o "+best.unsatisfiedValue);
			}
			initial = kick(best.assignment);
		}		
		return best;
	}
	
	private boolean[] kick(boolean[] assignment) {
		boolean[] ret = Arrays.copyOf(assignment, assignment.length);
		Collections.shuffle(changeIndex, rand);
		int numChange = (int)(assignment.length * CHANGE_RATIO);
		List<Integer> list = changeIndex.subList(0, numChange);
		for (int index : list) {
			ret[index] = !ret[index];
		}
		return ret;
	}
}
