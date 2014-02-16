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
