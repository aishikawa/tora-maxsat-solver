package maxsat_solver.algorithms;

import maxsat_solver.Instance;
import maxsat_solver.Solution;

public abstract class Algorithm {
	protected long TIME_LIMIT = 5*60*1000;
	protected Instance instance;

	protected Algorithm(Instance ins) {
		instance = ins;
	}
	
	/**
	 * set time limit
	 * @param t time limit in millisecond
	 */
	public void setTimeLimit(long t) {
		TIME_LIMIT = t;
	}
	
	public abstract Solution start();
}
