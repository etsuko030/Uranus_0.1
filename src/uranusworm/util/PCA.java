package uranusworm.util;

import java.util.Vector;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class PCA {


	public Mat mean;
	public Mat covar;
	public Mat eigvec;
	public Mat eigval;
	public int cols;
	public int rows;
	
//	Mat redEigenvec;
//	public Mat half;		// for Bha-distance 
	
	public PCA(Vector<double[]> d){
		
		cols = d.elementAt(0).length;
		rows = d.size();
		
		mean = new Mat();
		covar = new Mat();
		
		Mat data = DataConverter.jvector2Mat(d);

		Core.calcCovarMatrix(data, covar, mean, Core.COVAR_NORMAL|Core.COVAR_ROWS, CvType.CV_64F);
		
		eigvec = new Mat();
		eigval = new Mat();
		Core.eigen(covar, eigval, eigvec);
		
		// project to 50% dimensions for Bha distance
//		int k = data.cols()/2;
//		redEigenvec = new Mat();
//		half = new Mat();
//		Core.PCACompute(data, mean, redEigenvec, k);
//		Core.PCAProject(data, mean, redEigenvec, half);
		
		// 95% projection
//		Core.PCACompute(tempdata, mean, redEigenvec);
//		Core.PCACompute(tempdata, mean, redEigenvec, 0.95);

	}
	
}

