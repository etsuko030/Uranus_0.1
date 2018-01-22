package data;

import java.util.Vector;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;
import org.opencv.core.Mat;

import uranusworm.ft.Uwimg;
import uranusworm.ft.Worm;
import uranusworm.util.Calculate;
import uranusworm.util.Configs;
import uranusworm.util.DataConverter;
import uranusworm.util.Distance;
import uranusworm.util.Normalise;
import uranusworm.util.PCA;
import uranusworm.util.Printer;
import uranusworm.util.Projector;

public class DataSet {

	public Vector<DataPoint> points;	// all data points from input data
										// number of points = original.size();
										// dimension = original.elementAt(i).value.length;
	
	public DataSet(Vector<Uwimg[]> images){
		
		Vector<DataPoint> original = initialise(images);
//		normalisation_done = false;
		if (Configs.DATANORM){
			points = normalise(original);
			System.out.println("normalised matrix");
			Printer.printDataPointArray(points);
			System.out.println("normalised matrix ends");
		}
		else points = original;
		checkCompleteness();
	}
	
	private DataSet(){
		
	}
	
	public class SubSet extends DataSet{
	// data with the same label
		
		String label;
		
		public SubSet(Vector<DataPoint> p){
			super();
			points = p;
			checkCompleteness();
			checkSameLabel();
		}
		
		private void checkSameLabel(){

			boolean labelsame = true;
			String temp_label = points.elementAt(0).label;
			
			for (int i = 0; i < points.size(); i ++){
				if (!temp_label.equals(points.elementAt(i).label)){ 
					labelsame = false;
					break;
				}
			}
			assert labelsame == true;
			label = temp_label;
			System.out.println("Correct subset -- "+ labelsame);
			System.out.println("Subset label = " + label);
		}
	}
	
	public class ProjectedSet extends DataSet{
		
		public int original_dim;
		public int current_dim;
		
		public ProjectedSet(Vector<DataPoint> p, int original_dimension) {
			super();
			points = p;
			original_dim = original_dimension;
			current_dim = p.elementAt(0).value.length;
			checkCompleteness();
		}

	}
	
	public class VisuSet extends DataSet{
		
		public VisuSet(Vector<DataPoint> p) {
			super();
			points = p;
			checkCompleteness();
		}

	}
	
	private Vector<DataPoint> initialise(Vector<Uwimg[]> images){
		
		Vector<DataPoint> p = new Vector<DataPoint>();
		
		for (int i = 0; i < images.size(); i ++){
			for (int j = 0; j < images.elementAt(i).length; j ++){
				DataPoint temp = new DataPoint(images.elementAt(i)[j].ftvector, images.elementAt(i)[j].keyword);
				p.addElement(temp);
			}
		}
		
		
		return p;
	}

	private void checkCompleteness(){
	//	 check that every point has the same dimensions?
		
		boolean completeness = true;
		boolean dimension_equal = true;
		boolean label_exist = true;
		int dimension = points.elementAt(0).value.length;
		
		for (int i = 0; i < points.size(); i ++){
			if (dimension != points.elementAt(i).value.length){
				dimension_equal = false;
				break;
			}
			if (points.elementAt(i).label == null){ 
				label_exist = false;
				break;
			}
		}
		completeness = dimension_equal && label_exist;
		assert completeness;
		System.out.println("Check data complete! -- "+completeness);
		System.out.println("Dimension = "+ dimension + ", number of points = " + points.size());
	}
	
	private Vector<DataPoint> normalise(Vector<DataPoint> original){
		
		System.out.println("normalising data  ...");
		Vector<double[]> to_be_normed = DataPoint.pureDataFromPoints(original);
		Vector<DataPoint> normed = DataPoint.renewPoints(original, Normalise.meanVarianceWhole(to_be_normed));
//		normalisation_done = true;
		return normed;
	}
	
	// return a dimension-reduced set
	public ProjectedSet dimensionReduction(){
		int original_dimension = points.elementAt(0).value.length;
		return new ProjectedSet(Projector.pointsProjection(points, Configs.REDUCTIONRATE), original_dimension);
	}
	
	// return a 2D set for visualisation
	public VisuSet to2D(){
		return new VisuSet(Projector.pointsProjection_2D(points, 2));
	}
	
	public Vector<DataPoint> collectPointsLabelled(String label){
	//	get all points labelled x
		
		Vector<DataPoint> result = new Vector<DataPoint>();
		for (int i = 0; i < points.size(); i ++){
			if(label.equals(points.elementAt(i).label))
				result.addElement(points.elementAt(i));
		}
		return result;
	}
	
	public Vector<DataPoint> collectPointsClassified(String classified){
	//	get all points classified as x
		
		Vector<DataPoint> result = new Vector<DataPoint>();
		for (int i = 0; i < points.size(); i ++){
			if(classified.equals(points.elementAt(i).classified))
				result.addElement(points.elementAt(i));
		}
		return result;
	}
	
	public Model computeModel(){
		return new Model(points);
	}
	
	
	
//	xxx
//	
//
//	int wormNum;
//	int[] rows;
//	int cols;	// dim
//	int rows_t;
//	public String[] keyword;
//	
//	double percentage = Configs.REDUCTIONRATE;	// percentage of dimension reduction
//	double bhapercent = Configs.BHAREDUCTIONRATE;	
//	
//	Vector<Vector<double[]>> data;
//	Vector<double[]> centre;
//	Vector<Vector<double[]>> covar;
//	Vector<Vector<double[]>> eigvec;
//	Vector<double[]> eigval;
//	
//	Vector<Vector<double[]>> norm_data;
//	public Vector<Vector<double[]>> reduced_data;
//	int reduced_dim;
//	Vector<double[]> reduced_centre;
//	Vector<Vector<double[]>> reduced_covar;
//	Vector<Vector<double[]>> reduced_eigvec;
//	Vector<double[]> reduced_eigval;
//	
//	private boolean reduction_done;
//	
//	public Vector<Vector<double[]>> mdis;
//	// double[wormNum] for each image to record mdis to each class
//	// Vector<double[]> for images from the same class
//	
//	public Vector<int[]> wIdx;	// closest word index for images
//	public RealMatrix confusion;
//	Vector<Vector<double[]>> bhareduced_data;	// reduced by bha specific rate
//	public RealMatrix bhaCoeff;
//	
//	
//	
//	public DataSet(Worm[] w){
//		
//		wormNum = w.length;
//		cols = w[0].dim;
//		rows = new int[wormNum];
//		keyword = new String[wormNum];
//		data = new Vector<Vector<double[]>>();
//		centre = new Vector<double[]>();
//		covar = new Vector<Vector<double[]>>();
//		eigvec = new Vector<Vector<double[]>>();
//		eigval = new Vector<double[]>();
//		
//		for (int i = 0; i < wormNum; i ++){
//			keyword[i] = w[i].word;
//			rows[i] = w[i].imgNum;
////			Vector<double[]> temp = new Vector<double[]>();
//			for (int j = 0; j < w[i].imgNum; j ++){}
//			data.addElement(w[i].data);
//			centre.addElement(w[i].centre);
//			covar.addElement(w[i].covarMatrix);
//			eigvec.addElement(w[i].eigenVector);
//			eigval.addElement(w[i].eigenValue);
//		}
//		rows_t = Calculate.sumOfArray(rows);
//		
//		if(Configs.DATANORM) 
//			dataNormalisation(data);
//		
//	}
//
//	
//	private void dataNormalisation(Vector<Vector<double[]>> d){
//		// normalise data against standard deviation and mean
//		
//		System.out.println("normalising data  ...");
//		norm_data = Normalise.meanVariance(d);
//		assert norm_data != null;
//	}
//	
//	public void reduceDimension(){
//	// reducing dimension of data according to PCA eigenvalues
//	// without separate training set
//		
//		System.out.println("Reducing data to the first " + percentage*100 + "% amount of eigenvalues...");
//		if(Configs.DATANORM){
//			reduced_data = Projector.projector(norm_data, percentage);
//		}
//		else reduced_data = Projector.projector(data, percentage);
//		reduced_dim = reduced_data.elementAt(0).elementAt(0).length;
//		System.out.println("Dimension reduction finished: current dimension = " + reduced_dim + ", original dimension = " + cols);
//		
//		// compute new mean & covariance matrices for reduced data
//		
//		reduced_centre = new Vector<double[]>();
//		reduced_covar = new Vector<Vector<double[]>>();
//		reduced_eigvec = new Vector<Vector<double[]>>();
//		reduced_eigval = new Vector<double[]>();
//		
//		for (int i = 0; i < wormNum; i ++){
//			
//			PCA eigen = new PCA(reduced_data.elementAt(i));
//			System.out.println("eigenmean");
//			System.out.println(eigen.mean.dump());
//			assert DataConverter.mat2Jvector(eigen.mean).size()==1;
//			reduced_centre.addElement(DataConverter.rowMat2Double(eigen.mean));
//			reduced_covar.addElement(DataConverter.mat2Jvector(eigen.covar));
//			reduced_eigvec.addElement(DataConverter.mat2Jvector(eigen.eigvec));
//			reduced_eigval.addElement(DataConverter.colMat2Double(eigen.eigval));
//		
//		}
//		
//		
//	//	compute reduced dimension data for computing Bhattacharyya coefficient
//		
//		bhaCoeff = MatrixUtils.createRealMatrix(wormNum, wormNum);
//		
//		// reduce data by another rate
//		System.out.println("Reducing data to the first " + bhapercent*100 + "% amount of eigenvalues (for Bha co-)...");
//		bhareduced_data = Projector.projector(data, bhapercent);
//		System.out.println(bhareduced_data.elementAt(0).elementAt(0).length);
//		for (int i = 0; i < wormNum; i ++)
//			for (int j = 0; j < wormNum; j ++){
//				Mat mi = DataConverter.jvector2Mat(bhareduced_data.elementAt(i));
//				Mat mj = DataConverter.jvector2Mat(bhareduced_data.elementAt(j));
//				double temp = Distance.Bhattacharyya(mi, mj);
//				bhaCoeff.setEntry(i, j, temp);
//			}
//		
//		System.out.println("Bha coefficient:");
//		System.out.println("Words:");
//		for (int i = 0; i < wormNum; i ++)
//			System.out.print("\t" + keyword[i]);
//		System.out.println();
//		for (int i = 0; i < wormNum; i ++){
//			double[] nowRow = bhaCoeff.getRow(i);
////			int[] intRow = new int[nowRow.length];
////			for (int j = 0; j < nowRow.length; j ++) intRow[j] = (int)nowRow[j];
//			for (int j = 0; j < nowRow.length; j ++)
//				System.out.print("\t" + nowRow[j]);
//			System.out.println();
//		}
//		
//		reduction_done = true;
//	}
//	
//	public void reduceDimension(DataSet other){
//	// reducing dimension of data according to PCA eigenvalues
//	// with a separate training set
//		
//		// put data together
//		Vector<Vector<double[]>> together = DataConverter.corporateDataSets(this, other);
//		
//		System.out.println("Reducing data to the first " + percentage*100 + "% amount of eigenvalues...");
//		Vector<Vector<double[]>> reduced_whole = Projector.projector(together, percentage);
//		reduced_dim = reduced_whole.elementAt(0).elementAt(0).length;
//		System.out.println("Dimension reduction finished: current dimension = " + reduced_dim + ", original dimension = " + cols);
//		
//		// seperate data
//		Vector<Vector<double[]>> reduced_other = new Vector<Vector<double[]>>();
//		reduced_data = new Vector<Vector<double[]>>();	// this data set is test set
//		
//		for (int i = 0; i < other.wormNum; i ++){
//			Vector<double[]> temp = reduced_data.elementAt(i);
//			reduced_other.addElement(temp);
//		}
//		for (int i = 0; i < wormNum; i ++){
//			Vector<double[]> temp = reduced_data.elementAt(other.wormNum+i);
//			reduced_data.addElement(temp);
//		}
//		
//		// compute new mean & covariance matrices for reduced data
//		
//		reduced_covar = new Vector<Vector<double[]>>();
//		reduced_eigvec = new Vector<Vector<double[]>>();
//		reduced_eigval = new Vector<double[]>();
//		
//		for (int i = 0; i < wormNum; i ++){
//			
//			PCA eigen = new PCA(reduced_data.elementAt(i));
//			
//			assert DataConverter.mat2Jvector(eigen.mean).size()==1;
//			reduced_centre.addElement(DataConverter.rowMat2Double(eigen.mean));
//			reduced_covar.addElement(DataConverter.mat2Jvector(eigen.covar));
//			reduced_eigvec.addElement(DataConverter.mat2Jvector(eigen.eigvec));
//			reduced_eigval.addElement(DataConverter.colMat2Double(eigen.eigval));
//		
//		}
//		
//	//	compute reduced dimension data for computing Bhattacharyya coefficient
//		
//		bhaCoeff = MatrixUtils.createRealMatrix(wormNum, wormNum);
//		
//		// reduce data by another rate
//		System.out.println("Reducing data to the first " + bhapercent*100 + "% amount of eigenvalues (for Bha co-)...");
//		bhareduced_data = Projector.projector(together, bhapercent);
//		System.out.println(bhareduced_data.elementAt(0).elementAt(0).length+" dimensions for Bha coefficient");
//		for (int i = 0; i < other.wormNum; i ++)
//			for (int j = 0; j < wormNum; j ++){
//				Mat mi = DataConverter.jvector2Mat(bhareduced_data.elementAt(i));
//				Mat mj = DataConverter.jvector2Mat(bhareduced_data.elementAt(i+j)); //?
//				double temp = Distance.Bhattacharyya(mi, mj);
//				bhaCoeff.setEntry(i, j, temp);
//			}
//		
//		System.out.println("Bha coefficient:");
//		System.out.println("Words:");
//		for (int i = 0; i < other.wormNum; i ++)
//			System.out.print("\t" + other.keyword[i]);
//		System.out.println();
//		for (int i = 0; i < wormNum; i ++){
//			double[] nowRow = bhaCoeff.getRow(i);
//			System.out.print(keyword[i]+ "\t");
////			int[] intRow = new int[nowRow.length];
////			for (int j = 0; j < nowRow.length; j ++) intRow[j] = (int)nowRow[j];
//			for (int j = 0; j < nowRow.length; j ++)
//				System.out.print("\t" + nowRow[j]);
//			System.out.println();
//		}
//		
//		reduction_done = true;
//	}
//	
//	public void classification(DataSet training){
//		assert reduction_done;
//		getMDis(training);
//		assignWord(training);
//		getConfusionMatrix(training);
//	}
//	
//	private void getMDis(DataSet training){
//	// used projected data for computing Mahalanobis distance
//		
//		mdis = new Vector<Vector<double[]>>();
//		for (int i = 0; i < wormNum; i ++){ // every class
//			Vector<double[]> nowdata = new Vector<double[]>();
//			for (int j = 0; j < rows[i]; j ++){ // every image in this class
//				double[] temp = new double[wormNum];	// distance between one image to all classes
//				// compute mdis
//				for (int x = 0; x < wormNum; x ++){
//					temp[x] = Distance.Mahalanobis(reduced_data.elementAt(i).elementAt(j), reduced_eigvec.elementAt(x), reduced_eigval.elementAt(x));
//					System.out.println("mdis = "+temp[x]);
//				}
//				nowdata.addElement(temp);
//			}
//			mdis.addElement(nowdata);
//		}
//		assert mdis != null;
//	}
//	
//	private void assignWord(DataSet training){	// classify
//	// ??
//		
//		wIdx = new Vector<int[]>();
//		for (int i = 0; i < wormNum; i ++){ // every class
//			int[] thisWord = new int[rows[i]];
//			for (int j = 0; j < rows[i]; j ++){ // every image in this class
//				double[] temp = mdis.elementAt(i).elementAt(j);
//				thisWord[j] = Calculate.minID(temp);
//			}
//			wIdx.addElement(thisWord);
//		}
//		
//	}
//	
//	private void getConfusionMatrix(DataSet training){
//		
//		double[][] c = new double[training.wormNum][wormNum];
//		for (int i = 0; i < training.wormNum; i ++)
//			for (int j = 0; j < wormNum; j ++)
//				c[i][j] = 0;
//		//
//		System.out.println("Working out confusion matrix...");
//		for (int i = 0; i < training.wormNum; i ++)
//			for (int s = 0; s < wIdx.elementAt(i).length; s ++) 
//				for (int j = 0; j < wormNum; j ++){
//					int classified_index = wIdx.elementAt(i)[s];
//					String classified = training.keyword[classified_index];
//					String real = keyword[j];
//					System.out.print("Classified as: "+classified + "; real class: " + real +", ");
//					if (classified.matches(real)) {
//						System.out.println("correct");
//						c[i][j]++;
//					} else {
//						System.err.println("wrong");
//					}
//				}
//		
//		confusion = MatrixUtils.createRealMatrix(training.wormNum, wormNum);
//		for (int i = 0; i < training.wormNum; i ++) confusion.setRow(i, c[i]);
//		
//		// print confusion matrix
//		System.out.println("Confusion Matrix:");
//		System.out.println("Column | True class; Row -> Classified class");
//		System.out.println("Words:");
//		for (int i = 0; i < training.wormNum; i ++)
//			System.out.print("\t" + training.keyword[i]);
//		System.out.println();
//		for (int i = 0; i < wormNum; i ++){
//			double[] nowRow = confusion.getRow(i);
//			int[] intRow = new int[nowRow.length];
//			for (int j = 0; j < nowRow.length; j ++) intRow[j] = (int)nowRow[j];
//			System.out.print(keyword[i] + "\t");
//			for (int j = 0; j < nowRow.length; j ++)
//				System.out.print("\t" + intRow[j]);
//			System.out.println();
//		}
//	}
//	
//
////	private void getBha(){
////	//	use reduced dimension data for computing Bhattacharyya coefficient
////		
////		bhaCoeff = MatrixUtils.createRealMatrix(wormNum, wormNum);
////
////		// reduce data by another rate
////		System.out.println("Reducing data to the first " + Configs.BHAREDUCTIONRATE*100 + "% amount of eigenvalues (for Bha co-)...");
////		bhareduced_data = Projector.projector(this, Configs.BHAREDUCTIONRATE);
////		System.out.println(bhareduced_data.elementAt(0).elementAt(0).length);
////		for (int i = 0; i < wormNum; i ++)
////			for (int j = 0; j < wormNum; j ++){
////				Mat mi = DataConverter.jvector2Mat(bhareduced_data.elementAt(i));
////				Mat mj = DataConverter.jvector2Mat(bhareduced_data.elementAt(j));
////				double temp = Distance.Bhattacharyya(mi, mj);
////				bhaCoeff.setEntry(i, j, temp);
////			}
////		
////		System.out.println("Bha coefficient:");
////		System.out.println("Words:");
////		for (int i = 0; i < wormNum; i ++)
////			System.out.print("\t" + word[i].word);
////		System.out.println();
////		for (int i = 0; i < wormNum; i ++){
////			double[] nowRow = bhaCoeff.getRow(i);
//////			int[] intRow = new int[nowRow.length];
//////			for (int j = 0; j < nowRow.length; j ++) intRow[j] = (int)nowRow[j];
////			for (int j = 0; j < nowRow.length; j ++)
////				System.out.print("\t" + nowRow[j]);
////			System.out.println();
////		}
////		
////	}
//
//	public class TrainingSet extends DataSet{
//		
//		public TrainingSet(Worm[] w) {
//			super(w);	// call superclass constructor
//		}
//	}
//	
//	public class TestSet extends DataSet{
//
//		public TestSet(Worm[] w) {
//			super(w);	// call superclass constructor
//		}
//		
//	}
}
