package uranusworm.util;

import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.Mat;

public class Normalise {
	
	
	public static Vector<Vector<double[]>> meanVariance(Vector<Vector<double[]>> v){
	// normalise data against standard deviation and mean
		
//		System.out.println(v.elementAt(0).elementAt(0)[0]);
		Vector<Mat> ms = DataConverter.jvv2MatVec(v);
		Mat big = new Mat();
		Core.vconcat(ms, big);
		System.out.println("Before norm:");
		Printer.printMatArray(big);
		
		Mat nb = normaliseMeanVariance(big);
		System.out.println("After norm:");
		Printer.printMatArray(nb);
		
		int n = v.size();
		int col = v.elementAt(0).elementAt(0).length;
		int row[] = new int[n];
//		for (int i = 0; i < n; i ++)
//			row[i] = 0;
		for (int i = 0; i < n; i ++)
			row[i] = v.elementAt(i).size();
		
		Vector<Vector<double[]>> norm = new Vector<Vector<double[]>>();

		int rowStart, rowEnd;
		for (int i = 0; i < n; i ++){
		// for each category

			if (i == 0){
				rowStart = 0;
				rowEnd = row[0];
			}
			else {
				rowStart = 0;
				rowEnd = row[0];
				for (int j = 0; j < i; j++){
						rowStart += row[j];
						rowEnd += row[j+1];
				}
			}
			// show submat division:
//			System.out.println("start, end = "+rowStart+", "+rowEnd);
			
			// separate according to number of records
			Mat p = nb.submat(rowStart, rowEnd, 0, col);	// "colEnd" is "where the column ends" - column 'col' is excluded
			
			Vector<double[]> t = DataConverter.mat2Jvector(p);
			
//			System.err.println("TEST: array after conversion");
//			for (int b = 0; b < t.size(); b++){ 
//				Printer.printArray(t.elementAt(b));
//				System.out.println();
//			}
//			System.err.println("TEST: conversion finished");
			
			norm.addElement(t);
		}
		
		return norm;
	}
	
	public static Vector<double[]> meanVarianceWhole(Vector<double[]> v){
	// normalise data against standard deviation and mean
		
		Mat original = DataConverter.jvector2Mat(v);
		Mat norm = normaliseMeanVariance(original);
		
		Vector<double[]> result = DataConverter.mat2Jvector(norm);

		return result;
	}
	
	public static Mat normaliseMeanVariance(Mat m){
		
		Vector<double[]> matrix = DataConverter.mat2Jvector(m);
		int num = matrix.size();
		int dim = matrix.elementAt(0).length;
		assert num!=0;
		
		double[] mean = new double[dim];
		for (int i = 0; i < dim; i ++){ 
			mean[i] = 0;
		}
		for (int i = 0; i < dim; i ++)
			for (int j = 0; j < num; j ++){
				mean[i] += matrix.elementAt(j)[i];
			}
		for (int i = 0; i < dim; i ++){ 
			mean[i] = mean[i]/(double)num;
		}
		
		for (int i = 0; i < dim; i ++)
			for (int j = 0; j < num; j ++){
				matrix.elementAt(j)[i] = matrix.elementAt(j)[i] - mean[i];
			}
		
		double[] d = new double[dim];
		for (int i = 0; i < dim; i ++){ 
			d[i] = 0;
		}
		for (int i = 0; i < dim; i ++){
			for (int j = 0; j < num; j ++){
				d[i] += matrix.elementAt(j)[i]*matrix.elementAt(j)[i];
			}
			d[i] = d[i]/(double)num;
//			System.out.print("d"+i+"="+d[i]+' ');
		}
		
		
//		System.out.println("dim="+dim);
		for (int i = 0; i < dim; i ++){
//			assert d[i]!=0;
			for (int j = 0; j < num; j ++){
				if (d[i]!=0)
					matrix.elementAt(j)[i] = matrix.elementAt(j)[i]/(Math.sqrt(d[i]));
				else matrix.elementAt(j)[i] = matrix.elementAt(j)[i];
			}
		}
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println("print before conversion");
//		for (int i = 0; i < num; i ++) Printer.printArrayInARow(matrix.elementAt(i));
//		System.out.println();
//		System.out.println();
//		System.out.println();
//		System.out.println();
		Mat result = DataConverter.jvector2Mat(matrix);

		return result;
	}
	
}
