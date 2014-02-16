package maxsat_solver.algorithms;

import maxsat_solver.Instance;


public abstract class AlgorithmUsingOneFlipLocalSearch extends Algorithm {
	protected OneFlipLocalSearch localSearch;

	protected AlgorithmUsingOneFlipLocalSearch(Instance ins, long seed) {
		super(ins);		
		localSearch = new OneFlipLocalSearch(ins, seed);
	}
	
	
	public void setBestImprove() {
		localSearch.setBestImprove();
	}
	
	public void setFirstImprove() {
		localSearch.setFirstImprove();
	}
}
