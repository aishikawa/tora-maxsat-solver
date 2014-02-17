/*
 * Copyright 2014 A.Ishikawa
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package maxsat_solver.algorithms;

import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

import maxsat_solver.Instance;
import maxsat_solver.Solution;
import maxsat_solver.Util;

public class GeneticLocalSearch extends AlgorithmUsingOneFlipLocalSearch {

	private Random rand;
	
	private double ratio;
	private int numPopulation;
	private SortedSet<Solution> population;
	
	private Solution incumbent;
	
	public GeneticLocalSearch(Instance ins, Random r) {
		super(ins, r.nextLong());
		rand = r;		
		ratio = 1.0;
	}
	
	/**
	 * population number is set to (number of valiables) * r
	 * @param r
	 */
	public void setRatio(double r) {
		ratio = r;
	}
	
	@Override
	public Solution start() {
		long sTime = System.currentTimeMillis();
		
		initialize();
		
		while (System.currentTimeMillis() - sTime < TIME_LIMIT) {
			Solution[] array = new Solution[numPopulation];
			population.toArray(array);		
			Solution parent1 = array[rand.nextInt(numPopulation)];
			Solution parent2 = array[rand.nextInt(numPopulation)];

			boolean[] child = crossover(parent1.assignment, parent2.assignment);
			Solution sol = localSearch.getLocalOptimumSolution(child);
		
			if (incumbent.unsatisfiedValue > sol.unsatisfiedValue) {
				incumbent = sol;
				System.out.println("o "+incumbent.unsatisfiedValue);
			}
		
			if (population.contains(sol) == false) {
				population.add(sol);
				population.remove(population.last());
			}
		}
		
		return incumbent;
	}
	
	private void initialize() {
		numPopulation = (int)(instance.getNumValiables() * ratio);
		population = new TreeSet<Solution>();
		
		while(population.size() < numPopulation) {
			boolean[] assignment = Util.getRandomAssignment(instance.getNumValiables(), rand);
			long uv = instance.getUnsatisfiedValue(assignment);
			Solution sol = new Solution(assignment, uv);
			population.add(sol);
			if (incumbent == null || incumbent.unsatisfiedValue > sol.unsatisfiedValue) {
				incumbent = sol;
				System.out.println("o "+incumbent.unsatisfiedValue);
			}
		}
	}
	
	/**
	 * uniform crossover
	 */
	private boolean[] crossover(boolean[] parent1, boolean[] parent2) {
		int length = parent1.length;
		
		boolean[] child = new boolean[length];
		for (int i=0; i<length; i++) {
			boolean mask = rand.nextBoolean();
			if (mask) {
				child[i] = parent1[i];
			} else {
				child[i] = parent2[i];
			}
		}
		return child;
		
	}
}
