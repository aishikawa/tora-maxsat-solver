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

import java.util.Random;

public class Util {
	
	public static boolean[] getRandomAssignment(int numVariable, Random rand) {
		boolean[] ret = new boolean[numVariable];
		for (int i=0; i<numVariable; i++) {
			ret[i] = rand.nextBoolean();
		}
		return ret;
	}
	
	public static void printValues(boolean[] values) {
		System.out.print("v ");
		for (int i=0; i<values.length; i++) {
			int x = i+1;
			if (values[i] == false) {
				x = -x;
			}
			System.out.print(x+" ");
		}
		System.out.println();
	}
}
