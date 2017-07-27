package day_01.algorithm;

import java.util.Random;

public class RandomDemo {
	
    static double doubleRandom(double min,double max){

		Random r = new Random();
		
		double x = r.nextDouble(); //0-1
		double result=min + x * (max-min);
		
		return result;
		
	}
	
}
