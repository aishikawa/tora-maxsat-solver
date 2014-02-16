package maxsat_solver.algorithms;

import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;

public class MultiStartLocalSearch extends AlgorithmUsingOneFlipLocalSearch {
	private Random rand;
	
	public MultiStartLocalSearch(Instance ins, Random r) {
		super(ins, r.nextLong());
		rand = r;
	}
	
	/**
	 * start multi-start local search and return the best one among searched solutions;
	 */
	@Override
	public Solution start() {
		long sTime = System.currentTimeMillis();
		Solution best = null;		
		while(System.currentTimeMillis() - sTime < TIME_LIMIT) {
			boolean[] initial = Util.getRandomAssignment(instance.getNumValiables(), rand);
			Solution localOpt = localSearch.getLocalOptimumSolution(initial);
			if (best == null || best.unsatisfiedValue > localOpt.unsatisfiedValue) {
				best = localOpt;
				System.out.println("o "+best.unsatisfiedValue);
			}
		}		
		return best;
	}
}
