package uranusworm.util;

import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfFloat;

import data.DataSet;

public class DataConverter {
	
// 1. converter between OpenCV Mat (used for matrix data) and Java Vector<double[]>
	public static Mat jvector2Mat(Vector<double[]> v){
		Mat m = new Mat(v.size(), v.elementAt(0).length, CvType.CV_64FC1);
		for (int i = 0; i < v.size(); i++){
			double[] data = v.elementAt(i);
			for (int j = 0; j< v.elementAt(0).length; j++){
				double[] temp = new double[1];
				temp[0] = data[j];
				m.put(i, j, temp[0]); // ?
			}
		}
		return m;
	}
	
	public static Vector<double[]> mat2Jvector(Mat m){
		Vector<double[]> v = new Vector<double[]>();
		for (int i = 0; i < m.rows(); i++ ){
			double[] temp = new double[m.cols()];
			for (int j = 0; j < m.cols(); j++ ){
				temp[j] = m.get(i, j)[0];
			}
			v.addElement(temp);
		}
		return v;
	}
	
	// turn vector of vector of double to a list of Mat
	public static Vector<Mat> jvv2MatVec(Vector<Vector<double[]>> v){
		
		Vector<Mat> ms = new Vector<Mat>();	
		
		for (int i = 0; i < v.size(); i ++){
			Mat temp = DataConverter.jvector2Mat(v.elementAt(i));
			ms.addElement(temp);
		}
		
		return ms;
	}

	// turn vector of vector of double to a Mat
	public static Mat jvv2MatBig(Vector<Vector<double[]>> v){
		
		Vector<Mat> ms = new Vector<Mat>();	
		int nrow, col;
		col = v.elementAt(0).elementAt(0).length;
		nrow = 0;
		
		for (int i = 0; i < v.size(); i ++){
			Mat temp = DataConverter.jvector2Mat(v.elementAt(i));
			ms.addElement(temp);
			for (int j = 0; j < v.elementAt(i).size(); j++){
				nrow ++;
			}
		}
		Mat big = new Mat(nrow, col, CvType.CV_64FC1);
		Core.vconcat(ms, big);
		
		return big;
	}

// 2. Convert a Vector of an Array (of Mat) to an Array (of Mat)
	public static Mat[] vecOfArray2Array(Vector<Mat[]> vec){
		
		// to record length of each array in Vector
		int[] len = new int[vec.size()];
		for (int i = 0; i < vec.size(); i ++){
			len[i] = vec.elementAt(i).length;
		}
		// length of the result array:
		int num = 0;
		for (int i = 0; i < len.length; i ++){
			num += len[i];
		}
		
		// copy to new array
		Mat[] ar = new Mat[num];
		int count = 0;
		for (int i = 0; i < vec.size(); i ++)
			for (int j = 0; j < vec.elementAt(i).length; j ++){
				ar[count] = vec.elementAt(i)[j];
				count ++;
			}
		return ar;
	}
	
	public static Mat[] vecOfVec2Array(Vector<Vector<Mat>> v){
		
		
		int[] length = new int[v.size()];
		for (int i = 0; i < v.size(); i ++){
			length[i] = v.elementAt(i).size();
		}
		
		int len = Calculate.sumOfArray(length);
		Mat[] res = new Mat[len];
		
		int index = 0;
		for (int i = 0; i < v.size(); i ++){
			Vector<Mat> vtemp = v.elementAt(i);
			for (int j = 0; j < vtemp.size(); j ++){
				res[index] = v.elementAt(i).elementAt(j);
				index ++;
			}
		}
		
		return res;
	}

// 3. Mat of Float (from HOG Descriptor) to double[]
	public static double[] mof2Array (MatOfFloat m){
		
		double[] result = new double[m.rows()];
		for (int i = 0; i < m.rows(); i ++){
			result[i] = m.get(i, 0)[0];
		}
		return result;
	}

// 4. concatenate two arrays (without merging identical entries)
	public static double[] concatArray(double[] a1, double[] a2){
		double[] result = new double[a1.length + a2.length];
		for (int i = 0; i<a1.length; i++)
			result[i] = a1[i];
		for (int i = 0; i<a2.length; i++)
			result[a1.length+i] = a2[i];
		
		return result;
	}
	
// 5. normalise double[] vector
	public static double[] normaliseDoubleArray (double[] d){
		
		double sum = 0;
		int n = d.length;
		for (int i = 0; i < n; i ++){
			sum += d[i];
		}
		
		double[] result = new double[n];
		
		for (int i = 0; i < n; i ++){
			result[i] = d[i]/sum;
		}
		
		return result;
	}

// 6. convert double[] to Mat (col)
	public static Mat double2Mat(double[] d){ // row
		Mat result = new Mat(1, d.length, CvType.CV_64FC1);
		for (int i = 0; i < d.length; i ++){
			double[] temp = new double[1];
			temp[0] = d[i];
			result.put(0, i, temp);
		}
		return result;
	}
	
	public static double[] rowMat2Double(Mat m){
		
		assert m.rows()==1:"mat rows = " + m.rows();
		double[] result = new double[m.cols()];

		for (int i = 0; i < m.cols(); i ++){
			double[] temp = new double[1];
			m.get(0, i, temp);
			result[i] = temp[0];
		}
		
		return result;
	}

	public static double[] colMat2Double(Mat m){
		
		assert m.cols()==1:"mat cols = " + m.cols();
		double[] result = new double[m.rows()];

		for (int i = 0; i < m.rows(); i ++){
			double[] temp = new double[1];
			m.get(i, 0, temp);
			result[i] = temp[0];
		}
		
		return result;
	}
	
	// combine two datasets for dimensionality reduction
	public static Vector<Vector<double[]>> corporateDataSets (DataSet s1, DataSet s2){
		
		
		return null;
	}
	
	// or combine two Vector<Vector<double[]>>s
	public static Vector<Vector<double[]>> corporateSets (DataSet s1, DataSet s2){
		
		return null;
	}
	

}
