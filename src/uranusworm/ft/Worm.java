package uranusworm.ft;

import java.util.Vector;

import uranusworm.util.DataConverter;
import uranusworm.util.PCA;

public class Worm {

//	public Uwimg[] images;
	public String word;
	public Vector<double[]> data;	// records of raw data (feature vectors)
//	public WormGene component;
	public int imgNum;		// rows
	public int dim;			// cols
	
//	public PCA eigen;
	
	public double[] centre;	// PCA mean
	public Vector<double[]> covarMatrix;
	public Vector<double[]> eigenVector;
	public double[] eigenValue;
	
	public Worm (String kw, Uwimg[] samples){
		
		assert samples!=null;

		word = kw;
		imgNum = samples.length;
		data = corporate(samples);
		dim = data.elementAt(0).length;
		
		eigenDecomp();
		printWorm();

//		component = new WormGene(DataConverter.jvector2Mat(data));
		
	}
	
	// REVISE
	public void updateBOWdata(){
		System.out.println("Worm "+ word + " updating:");
//		data = corporate(images, 0);
		eigenDecomp();
		dim = data.elementAt(0).length;
		System.out.println("Updated.");
	}
	
	private Vector<double[]> corporate(Uwimg[] samples){
		// put feature vector together
		
		Vector<double[]> d = new Vector<double[]>();
		for (int i = 0; i < samples.length; i ++){
//			System.out.print("ftv=");
//			for (int j = 0; j < samples[i].ftvector.length; j ++){
//				System.out.print(samples[i].ftvector[j]+" ");
//			}
//			System.out.println();
			d.addElement(samples[i].ftvector);
		}
		assert d!= null;
		return d;
	}
	
	private void eigenDecomp(){
	// do PCA to class data - eigen decomposition
		
		assert data!= null;
		PCA eigen = new PCA(data);
		
		assert DataConverter.mat2Jvector(eigen.mean).size()==1;
		centre = DataConverter.mat2Jvector(eigen.mean).elementAt(0);
		
		covarMatrix = DataConverter.mat2Jvector(eigen.covar);
		eigenVector = DataConverter.mat2Jvector(eigen.eigvec);
		eigenValue = DataConverter.colMat2Double(eigen.eigval);
	
	}
	
	private void printWorm(){
		System.out.println("Construction finished for: " + word);
//		System.out.println("data: ");
//		VecOperation.printVectorArray(data);
	}
}
