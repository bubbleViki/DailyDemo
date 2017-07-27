package day_01.algorithm;

public class ArrayDemo {
	
    static double maxFind(double[] a){
    	double max=a[0];
    	for(int i=0;i<a.length;i++){
    		if(a[i]>max)
    			max=a[i];
    	}
    	
    	return max;
    	
    }	
	
    
    static double avgCalculate(double[] a){
    	
    	double avg=0;
    	double sum=0;
    	
    	for(int i=0;i<a.length;i++){
    		sum+=a[i];
    	}    		
    	avg=sum/a.length;
    	
    	return avg;
    	
    }
    
    static double[] copyArray(double[] a) {
		double[] b=new double[a.length];
		for(int i =0;i<a.length;i++){
			b[i]=a[i];
		}
		return b;
	}
    
    static void reverseArray(double[] a){
    	for(int i=0;i<a.length/2;i++){
    		double temp = a[i];
    		a[i]=a[a.length-1-i];
    		a[a.length-1-i]=temp;
    		
    	}
    }
    
	public static void main(String args[]){
		
		//generate a array
		double [] a= new double[10];
		
		for(int i=0;i<10;i++){
			a[i]=RandomDemo.doubleRandom(0, 10);
			System.out.println(a[i]);
		}
		
		
		//find max number
		double maxNum=maxFind(a);
		System.out.println("\nMAX="+maxNum);
	
		//calculate the AVG
		double avg = avgCalculate(a);
		System.out.println("\nAVG="+avg);
		
		
		//array copy
		double[] b = copyArray(a);
		System.out.println("\nb[]=");
		for(int i=0;i<b.length;i++){
			System.out.println(b[i]);
		}
		
		//reverse array element
		reverseArray(a);
		System.out.println("\nReverseArray");
		for(int i=0;i<a.length;i++){
			System.out.println(a[i]);
		}
	}
	
	
	
	
}
