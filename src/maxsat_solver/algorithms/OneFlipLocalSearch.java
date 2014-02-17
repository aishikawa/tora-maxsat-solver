package maxsat_solver.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;

public class OneFlipLocalSearch {
	private int index;
	private int[] flipVariable;
	
	private Instance instance;
	private boolean firstImprove;
	
	public OneFlipLocalSearch(Instance ins, long seed) {
		instance = ins;
		int numVariables = instance.getNumValiables();
		index = 0;
		flipVariable = new int[numVariables];
		Random rand = new Random(seed);
		
		List<Integer> list = new ArrayList<Integer>(numVariables);
		for (int i=0; i<numVariables; i++) {
			list.add(i);
		}		
		Collections.shuffle(list, rand);
		for (int i=0; i<numVariables; i++) {
			flipVariable[i] = list.get(i);
		}
		
		firstImprove = true;
	}
	
	public void setFirstImprove() {
		firstImprove = true;
	}
	
	public void setBestImprove() {
		firstImprove = false;
	}
	
	public Solution getLocalOptimumSolution(boolean[] initial) {
		boolean[] current = Arrays.copyOf(initial, initial.length);
		long currentUnsatisfiedValue = instance.getUnsatisfiedValue(current);
		
		while (true) {
			long bestDiff = 0;
			int bestFlipVariable = -1;
			for (int n=0; n<instance.getNumValiables(); n++) {
				long diff = instance.getFlipDifference(current, flipVariable[index]);
				if (diff < bestDiff) {
					bestDiff = diff;
					bestFlipVariable = flipVariable[index];
					if (firstImprove) {
						proceedIndex();
						break;
					}
				}
				proceedIndex();
			}
			if (bestDiff < 0) {
				current[bestFlipVariable] = !current[bestFlipVariable];
				currentUnsatisfiedValue += bestDiff;
			} else {
				break;
			}
		}		
		return new Solution(current, currentUnsatisfiedValue);
	}
	
	private void proceedIndex() {
		index += 1;
		if (index >= instance.getNumValiables()) {
			index = 0;
		}
	}
}
