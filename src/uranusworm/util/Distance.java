package uranusworm.util;

import java.util.Vector;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealVector;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfDouble;

import data.DataPoint;
import data.Model;

public class Distance {
	
	
	// Bhattacharyya coefficient for two distributions
	// a, b are original data samples
	// better if a, b are dimension-reduced Principal Components
	public static double Bhattacharyya (Mat a, Mat b){
		
		// if not dimension-reduced
//		int k = a.cols()/2;
//		Mat temp1 = new Mat();
//		Mat temp2 = new Mat();
//		Mat pa = new Mat();
//		Mat pb = new Mat();
//		Core.PCACompute(a, temp1, temp2, k);
//		Core.PCAProject(a, temp1, temp2, pa);
//		Core.PCACompute(b, temp1, temp2, k);
//		Core.PCAProject(b, temp1, temp2, pb);
//		
		
		Mat cova = new Mat();
		Mat covb = new Mat();
		Mat meana = new Mat();
		Mat meanb = new Mat();
		Core.calcCovarMatrix(a, cova, meana, Core.COVAR_NORMAL|Core.COVAR_ROWS);
		Core.calcCovarMatrix(b, covb, meanb, Core.COVAR_NORMAL|Core.COVAR_ROWS);
		
		// if not dimension-reduced
//		Core.calcCovarMatrix(pa, cova, meana, Core.COVAR_NORMAL|Core.COVAR_ROWS);
//		Core.calcCovarMatrix(pb, covb, meanb, Core.COVAR_NORMAL|Core.COVAR_ROWS);
		
//		System.out.println("cov");
//		System.out.println(cova.dump());
//		System.out.println(covb.dump());
		
		double detcova = Core.determinant(cova);
		double detcovb = Core.determinant(covb);
		Mat cov = new Mat();
		Core.add(cova, covb, cov);
//		cov = cov.mul(cov, 0.5);
		
//		Core.scaleAdd(cov, 0.5, new Mat(), cov);
		for (int i = 0; i<cov.rows();i++)
			for (int j = 0; j<cov.cols();j++) {
				cov.put(i, j, 0.5* cov.get(i, j)[0]);
			}
		
		double detcov = Core.determinant(cov);
		
		Mat inv = new Mat(); // inverse of cov
		Core.invert(cov, inv);

		Mat mabsub = new Mat();
		Core.subtract(meana, meanb, mabsub);

		Mat mabsub_tp = new Mat();
		Core.transpose(mabsub, mabsub_tp);
		
		Mat temp = new Mat();
		Core.gemm(mabsub, inv, 1, new Mat(), 0, temp);
//		System.out.println(temp.dump());
//		System.out.println(mabsub_tp.dump());
		
		Mat res = new Mat();
		Core.gemm(temp, mabsub_tp, 1, new Mat(), 0, res);
//		System.out.println(res.dump());
	
//		Mat temp = mabsub_tp.mul(inv);
//		System.out.println(temp.dump());
//		
//		Mat res = temp.mul(mabsub);
//		System.out.println(res.dump());
		
		double s2 = Math.sqrt(detcov/(Math.sqrt(detcova*detcovb)));
		// s1 = 0.125 * (mabsub_tp*inv*mabsub)
		double s1 = 0.125 * res.get(0, 0)[0]; 
		
		double bdis = 1/(Math.exp(s1)*s2);
		return bdis;
	}
	
	
	public static double Euclidean(double[] a1, double[] a2){
		
		RealVector x = MatrixUtils.createRealVector(a1);
		RealVector y = MatrixUtils.createRealVector(a2);
		
		return x.getDistance(y);
	}
	
	public static double Mahalanobis(double[] data, Vector<double[]> eigvec, double[] eigval){
	// Compute Mahalanobis distance from double[]s
		
		assert data.length == eigvec.elementAt(0).length && data.length == eigval.length;
		int dim = eigvec.elementAt(0).length;
		
		// if any eigval[i] == 0 ->?
		for (int i = 0; i < dim; i ++){	
			eigval[i] += Configs.MDIS_EPSILON;
		}
		
		for (int i = 0; i < dim; i ++)
			assert eigval[i]!=0;
		
		
		RealVector v1 = MatrixUtils.createRealVector(data);
		RealVector[] vec = new RealVector[dim];
		for (int i = 0; i < dim; i ++){
			vec[i] = MatrixUtils.createRealVector(eigvec.elementAt(i));
		}
		double[] d = new double[dim];
		for (int i = 0; i < dim; i ++){
			d[i] = v1.dotProduct(vec[i]) / Math.sqrt(eigval[i]);
		}
		RealVector dis = MatrixUtils.createRealVector(d);
		
		return vectorLength(dis);
	}
	
	public static double Mahalanobis(DataPoint data, Model m){
	// Compute Mahalanobis distance from a data point to a pre-built model
		
		assert data.value.length == m.dimension;
		int dim = data.value.length;
		
		// if any eigval[i] == 0 ->?
		for (int i = 0; i < dim; i ++){	
			m.eigenvalue[i] += Configs.MDIS_EPSILON;
		}
//		
//		for (int i = 0; i < dim; i ++)
//			assert eigval[i]!=0;
		
		RealVector v1 = MatrixUtils.createRealVector(data.value);
		RealVector[] vec = new RealVector[dim];
		for (int i = 0; i < dim; i ++){
			vec[i] = MatrixUtils.createRealVector(m.eigenvector.elementAt(i));
		}
		double[] d = new double[dim];
		for (int i = 0; i < dim; i ++){
			d[i] = v1.dotProduct(vec[i]) / Math.sqrt(m.eigenvalue[i]);
		}
		RealVector dis = MatrixUtils.createRealVector(d);
		
		return vectorLength(dis);
	}
	
	public static double vectorLength(RealVector v){
		
		double[] v0 = new double[v.getDimension()];
		for (int i = 0; i < v0.length; i ++) v0[i] = 0;
		
		RealVector zero = MatrixUtils.createRealVector(v0);
		
		return v.getDistance(zero);
	}

//	public MahaDis(RealVector x, RealVector y, PCA p){
//		
//		
//		int k = p.k;
//		RealVector v = x.subtract(y);
//		
//		double[] d = new double[k];
//		for (int i = 0; i < k; i ++) d[i] = oneDirDis(v, i, p);
//		RealVector dis = MatrixUtils.createRealVector(d);
//		
//		MDis = PCA.vectorLength(dis);
//	}
//
//	private double oneDirDis(RealVector v, int i, PCA p){
//		
//		double d = v.dotProduct(p.eigVectors[i]) / Math.sqrt(p.eigValues[i]);
//		
//		return d;
//	}
}
