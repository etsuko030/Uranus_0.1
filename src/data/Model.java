package data;

import java.util.Vector;

import uranusworm.util.DataConverter;
import uranusworm.util.PCA;

public class Model {

	public int number;		// number of points
	public int dimension;	// dimension of points
	public String class_label;	// class label
	
	public DataPoint centre;
	
	// PCA
	public double[] mean;
	Vector<double[]> covariance_matrix;
	public Vector<double[]> eigenvector;
	public double[] eigenvalue;
	
	public Model(Vector<DataPoint> data){
		
		number = data.size();
		dimension = data.elementAt(0).value.length;
		computePCA(data);
		
		centre = new DataPoint(mean, "centre");
	}
	
	private void computePCA(Vector<DataPoint> data){
		
		PCA p = new PCA(DataPoint.pureDataFromPoints(data));
		mean = DataConverter.rowMat2Double(p.mean);
		covariance_matrix = DataConverter.mat2Jvector(p.covar);
		eigenvector = DataConverter.mat2Jvector(p.eigvec); 
		eigenvalue = DataConverter.colMat2Double(p.eigval);
		System.out.println("covar:");
		System.out.println(p.covar.dump());
		System.out.println("eigvec:");
		System.out.println(p.eigvec.dump());		
		System.out.println("eigval:");
		System.out.println(p.eigval.dump());		
	}
	
	public void setClassLabel(String label){
		class_label = label;
	}
}
