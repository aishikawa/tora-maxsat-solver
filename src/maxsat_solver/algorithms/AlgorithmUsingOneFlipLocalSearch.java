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
