package maxsat_solver.algorithms;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;

public class TabuSearch extends Algorithm {

	private Random rand;
	
	private long tabuTenure;
	private long[] tabuList;
	
	public TabuSearch(Instance ins, Random r) {
		super(ins);
		rand = r;
						
		int numValiable = instance.getNumValiables();
		
		tabuTenure = (long)(numValiable * 0.1);
		tabuList = new long[numValiable];
		for (int i=0; i<numValiable; i++) {
			tabuList[i] = Long.MIN_VALUE/2;
		}
	}
	
	public void setTabuTenureRatio(double r) {
		tabuTenure = (long)(instance.getNumValiables() * r);
	}
	
	@Override
	public Solution start() {
		long sTime = System.currentTimeMillis();
		
		long numIteration = 0;
		
		boolean[] current = Util.getRandomAssignment(instance.getNumValiables(), rand);
		long currentUV = instance.getUnsatisfiedValue(current);
		Solution incumbent = new Solution(Arrays.copyOf(current, current.length), currentUV);
		System.out.println("o "+currentUV);
		
		while(System.currentTimeMillis() - sTime < TIME_LIMIT) {
			long bestDiff = Long.MAX_VALUE;
			List<Integer> bestIndices = new ArrayList<Integer>();
			for (int index=0; index<instance.getNumValiables(); index++) {
				if (numIteration - tabuList[index] > tabuTenure) {
					long diff = instance.getFlipDifference(current, index);
					if (bestDiff > diff) {
						bestDiff = diff;
						bestIndices.clear();
						bestIndices.add(index);
					} else if (bestDiff == diff) {
						bestIndices.add(index);
					}
				}
			}
			int flipIndex = bestIndices.get(rand.nextInt(bestIndices.size()));
			current[flipIndex] = !current[flipIndex];
			currentUV += bestDiff;

			if (currentUV < incumbent.unsatisfiedValue) {
				incumbent = new Solution(Arrays.copyOf(current, current.length), currentUV);
				System.out.println("o "+currentUV);
			}
			
			tabuList[flipIndex] = numIteration;
			numIteration += 1;
		}
		return incumbent;
	}

}
