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

package maxsat_solver;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Instance {
	private int numVariables;
	private long totalWeight;
	private Clause[] clauses;
	
	// use for compute flip difference
	private Set<Clause>[] containedClauseSet;
	
	private class Clause {
		long weight;
		Set<Integer> variables;
		
		private Clause() {
			weight = 1;
			variables = new HashSet<Integer>();
		}
		
		@Override
		public String toString() {
			StringBuffer buff = new StringBuffer();
			buff.append("("+weight+", (");
			for (int v : variables) {
				buff.append(v+" ");
			}
			buff.append("))");
			return buff.toString();
		}
	}
	
	private Instance() {		
	}
	
	public static Instance makeInstance(List<String> instanceString) {
		Instance instance = new Instance();
		boolean wcnf = false;
		
		int clauseIndex = 0;
		boolean weight = false;
		boolean alwaysSatisfied = false;
		Clause clause = instance.new Clause();
		for (String line : instanceString) {
			if (line.startsWith("c")) {
				continue;
			} else if (line.startsWith("p")) {
				String[] splited = line.split("\\s+");
				if (splited[1].equals("wcnf")) {					
					wcnf = true;
					weight = true;
				}
				
				instance.numVariables = Integer.parseInt(splited[2]);
				int numClauses = Integer.parseInt(splited[3]);
				
				instance.clauses = new Clause[numClauses];
				
				instance.containedClauseSet = new Set[instance.numVariables];
				for (int i=0; i<instance.numVariables; i++) {
					instance.containedClauseSet[i] = new HashSet<Clause>();
				}				
			} else {
				String[] splited = line.split(" ");
				for (String s : splited) {
					if (s.equals("0") == false) {
						int v = Integer.parseInt(s);
						if (weight) {
							clause.weight = v;
							weight = false;
						} else {
							if (clause.variables.contains(-v)) {
								alwaysSatisfied = true;
							}
							clause.variables.add(v);
						}
					} else {
						instance.clauses[clauseIndex++] = clause;	
						instance.totalWeight += clause.weight;
						if (alwaysSatisfied == false) {           //we do not need a clause to compute flip difference if the clause is always satisfied 
							for (int v : clause.variables) {
								int index = v;
								if (v < 0) {
									index = -v;
								}
								instance.containedClauseSet[index-1].add(clause);
							}
						}
						if (wcnf) {
							weight = true;
						}
						alwaysSatisfied = false;
						clause = instance.new Clause();
					}
				}
			}
		}
		return instance;
	}
	
	public long getUnsatisfiedValue(boolean[] solution) {
		long ret = 0;
		for (Clause c : clauses) {
			boolean satisfy = false;
			for (int v : c.variables) {
				if ((v > 0 && solution[v-1] == true) || (v < 0 && solution[-v-1] == false)) {
					satisfy = true;
				}
			}
			if (satisfy) {
				ret += c.weight;
			}
		}
		return totalWeight - ret;
	}
	
	public int getNumValiables() {
		return numVariables;
	}

	public long getFlipDifference(boolean[] current, int index) {
		long ret = 0;
		Set<Clause> set = containedClauseSet[index];
		for (Clause c : set) {
			int numTrue = 0;
			boolean effect = false;
			for (int v : c.variables) {
				if (v > 0 && current[v-1] == true || v < 0 && current[-v-1] == false) {
					numTrue += 1;
					if (v-1 == index || -v-1 == index) {
						effect = true;
					}
				}
			}
			if (numTrue == 1 && effect) {
				ret += c.weight;
			} else if (numTrue == 0){
				ret -= c.weight;
			}
		}
		return ret;
	}
	
	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("numVariables: "+numVariables+"\n");
		buff.append("totalWeight: "+totalWeight+"\n");
		buff.append("clauses\n");
		for (Clause c : clauses) {
			buff.append(c+"\n");
		}

		buff.append("containedClauseSet\n");
		for (int i=0; i<containedClauseSet.length; i++){
			buff.append(containedClauseSet[i]);
		}
		
		return buff.toString();
	}
}


