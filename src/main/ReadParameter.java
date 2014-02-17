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

package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ReadParameter {
	/** Iterated Local Search : change (number of variables * CHANGE_RATIO) variables at local optimum assignment */
	public double CHANGE_RATIO = 0.3;
	
	/** Genetic Local Search : population number is set to (number of variables) * POPULATION_RATIO */
	public double POPULATION_RATIO = 1.0;

	/** Tabu Search : tabu tenure is set to (number of variables) * TABU_TENURE_RATIO */
	public double TABU_TENURE_RATIO = 0.1;

	/** Simulated Annealing : NEW_TEMPERATURE = OLD_TEMPERATURE * COOLING_FACTOR */
	public double COOLING_FACTOR = 0.95;
	/** Simulated Annealing : search (ALPHA * NEIGHBORHOOD_SIZE) solutions at constant temperature. */
	public double ALPHA = 10.0;
	/** Simulated Annealing : initial temperature is set to move with probability INITIAL_MOVING_PROBABILITY */  
	public double INITIAL_MOVING_PROBABILITY = 0.9;
	
	
	public void readPropeties(String filename) throws IOException {
		Properties property = new Properties();
		InputStream is = new FileInputStream(new File(filename));
		property.load(is);
			
		CHANGE_RATIO = Double.parseDouble(property.getProperty("CHANGE_RATIO"));
			
		POPULATION_RATIO = Double.parseDouble(property.getProperty("POPULATION_RATIO"));
			
		TABU_TENURE_RATIO = Double.parseDouble(property.getProperty("TABU_TENURE_RATIO"));
		
		COOLING_FACTOR = Double.parseDouble(property.getProperty("COOLING_FACTOR"));
		ALPHA = Double.parseDouble(property.getProperty("ALPHA"));
		INITIAL_MOVING_PROBABILITY = Double.parseDouble(property.getProperty("INITIAL_MOVING_PROBABILITY"));

		is.close();		
	}
}
