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
