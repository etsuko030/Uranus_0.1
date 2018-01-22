package uranusworm.util;

import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

import data.DataPoint;


public class Projector {
	// Class for projecting high-dimensional data to lower dimensions
	// for visualisation
	
	public static Mat project2D (Mat data){
	// 	Using OpenCV PCA functionalities
	//	Transform high-dimensional data to 2D data
		
		// 2D points
		Mat m = new Mat();
	
		Mat mean_t = new Mat();
		Mat eigen_t = new Mat();
		Core.PCACompute(data, mean_t, eigen_t, 2);
		Core.PCAProject(data, mean_t, eigen_t, m);
		
		return m;
	}
	
	public static Mat reduction (Mat data, double percentage){
	// 	Using OpenCV PCA functionalities
	//	Transform high-dimensional data to lower-dimensional data
		
		// 2D points
		Mat m = new Mat();
	
		Mat mean_t = new Mat();
		Mat eigen_t = new Mat();
		Core.PCACompute(data, mean_t, eigen_t, percentage);
		Core.PCAProject(data, mean_t, eigen_t, m);
		
		return m;
	}
	
	public static Vector<Vector<double[]>> projector(Vector<Vector<double[]>> v, double percentage){
		
		int n = v.size();
//		int col = v.elementAt(0).elementAt(0).length;
		
//		int nrow = 0;
		int row[] = new int[n];
//		for (int i = 0; i < n; i ++)
//			row[i] = 0;
		for (int i = 0; i < n; i ++)
			row[i] = v.elementAt(i).size();
		
		
	//	test - print number of rows of each category:
//		System.out.println("row nums:");
//		for (int x = 0; x<row.length;x++)
//			System.out.println("row " +x+" = "+ row[x]);
		
	// 1. Make a big matrix and project data to 2D as whole
		Mat big = DataConverter.jvv2MatBig(v);
//		Vector<Mat> ms = DataConverter.jvv2MatVec(v);

		int newDim = 0;
		Mat all = new Mat();
		// -> 2D
		if (percentage == 2){
			all = project2D(big);
			newDim = 2;
		}
		// -> other dimensions
		else if (percentage<1 && percentage>0){
			all = reduction(big, percentage);
			newDim = all.cols();
		}
		else {
			System.err.println("Wrong percentage assigned: percentage = " + percentage +".");
			System.err.println("Percentage is specified in class DataSet should be between 0 to 1, or 2 when 2D projection is wanted.");
			System.err.println("Dimension will not be reduced.");
			all = big;
			newDim = all.cols();
		}
		
	// 2. Separate result into different groups
		Vector<Vector<double[]>> result = new Vector<Vector<double[]>>();
					
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
			Mat p = all.submat(rowStart, rowEnd, 0, newDim);	// "colEnd" is "where the column ends" - column 2 is excluded
			
			Vector<double[]> t = DataConverter.mat2Jvector(p);
			result.addElement(t);
		}
		
		return result;
	}
	
	public static Vector<DataPoint> pointsProjection(Vector<DataPoint> points, double percentage){
		
		Vector<DataPoint> re = new Vector<DataPoint>();
		
		Mat temp1 = DataConverter.jvector2Mat(DataPoint.pureDataFromPoints(points));
		Mat temp2 = reduction(temp1, percentage);
		Vector<double[]> projected = DataConverter.mat2Jvector(temp2);
		
		for (int i = 0; i < projected.size(); i ++){
			DataPoint temp_point = new DataPoint(projected.elementAt(i), points.elementAt(i).label);
			re.addElement(temp_point);
		}
		
		return re;
	}
	
	public static Vector<DataPoint> pointsProjection_2D(Vector<DataPoint> points, int dimension){
		
		assert dimension == 2;
		
		Vector<DataPoint> re = new Vector<DataPoint>();
		
		Mat temp1 = DataConverter.jvector2Mat(DataPoint.pureDataFromPoints(points));
		Mat temp2 = project2D(temp1);
		Vector<double[]> projected = DataConverter.mat2Jvector(temp2);
		
		for (int i = 0; i < projected.size(); i ++){
			DataPoint temp_point = new DataPoint(projected.elementAt(i), points.elementAt(i).label);
			re.addElement(temp_point);
		}
		
		return re;
	}
}
