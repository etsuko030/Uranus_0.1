package uranusworm.util;

public class Calculate {

	public static int sumOfArray(int[] a){
		int sum = 0;
		for (int i = 0; i < a.length; i ++){
			sum += a[i];
		}
		return sum;
	}
	
	public static double sumOfArray(double[] a){
		double sum = 0;
		for (int i = 0; i < a.length; i ++){
			sum += a[i];
		}
		return sum;
	}
	
	public static int minID(double[] a){
		
		
		int minID = -1;
		double min = 10000000;
		
		for (int i = 0; i < a.length; i ++)
	        	if (a[i] < min) {
	        		min = a[i];
	        		minID = i;
	            }    	
	    
		return minID;
	}
	
}
