package uranusworm.util;

import java.util.Vector;

import org.apache.commons.math3.linear.RealMatrix;
import org.opencv.core.Mat;

import data.DataPoint;

public class Printer {

	public static void printArray(double[] a){
		System.out.println("Array length = "+a.length+" :");
		for (int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
			if ((i+1)%5==0) System.out.println();
		}
	}
	
	public static void printArrayInARow(double[] a){
		for (int i = 0; i < a.length; i++){
			System.out.print(a[i]+" ");
		}
		System.out.println("["+a.length+"]");

	}
	
	public static void printMatArray(Mat m){
		// do not use Mat dump()
		
		Vector<double[]> vec = DataConverter.mat2Jvector(m);
		for (int i = 0; i < vec.size(); i++){
			printArrayInARow(vec.elementAt(i));
		}
	}
	
	public static void printRealMatrix(RealMatrix m){

	}
	
	public static void printDataPointArray(Vector<DataPoint> p){
		
		System.out.println(p.size() + "data points " +p.elementAt(0).value.length+" dimensions");
		for (int i = 0; i < p.size(); i ++){
			for (int j = 0; j < p.elementAt(0).value.length; j++){
				System.out.print(p.elementAt(i).value[j]+" ");
			}
			System.out.println();
		}
	}
}
