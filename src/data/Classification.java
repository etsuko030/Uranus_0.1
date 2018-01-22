package data;

import java.util.Vector;

import org.apache.commons.math3.linear.MatrixUtils;
import org.apache.commons.math3.linear.RealMatrix;

import uranusworm.util.Calculate;
import uranusworm.util.Distance;

public class Classification {

	Vector<DataPoint> testData;
	Model[] model;
	
	public Vector<double[]> maha_dis;
	
	String[] testDataLabels;
	public RealMatrix confusion;
	
	public Classification(Vector<DataPoint> test, Model[] classes){
		testData = test;
		model = classes;
		
		checkLegitimacy();
		getMahalanobisDistance();
	}
	
	private void checkLegitimacy(){
		
		boolean dimension_equal = true;
		int dim = testData.elementAt(0).value.length;
		
		for (int i = 0; i < model.length; i ++){
			if (dim != model[i].dimension){
				dimension_equal = false;
				break;
			}
		}
		assert dimension_equal;
	}
	
	private void getMahalanobisDistance(){
	// used projected data for computing Mahalanobis distance
		
		maha_dis = new Vector<double[]>();

		for (int j = 0; j < testData.size(); j ++){ // for every data point
			double[] temp = new double[model.length];	// distance from one point to every class
			// compute mdis
			for (int x = 0; x < model.length; x ++){
				temp[x] = Distance.Mahalanobis(testData.elementAt(j), model[x]);
				System.out.print("mdis = "+temp[x]);
				System.out.print("  ");
			}
			maha_dis.addElement(temp);
			System.out.println();
		}
			
		assert maha_dis != null;
		assert maha_dis.size() == testData.size();
	}
	
	public void doClassification(){
		
		for (int i = 0; i < testData.size(); i ++){
			int closest = Calculate.minID(maha_dis.elementAt(i));
			String classified = model[closest].class_label;
			testData.elementAt(i).assignClass(classified);
		}
		checkClassified();
		generateConfusionMatrix();
	}
	
	private void checkClassified(){
		
		for (int i = 0; i < testData.size(); i ++){
			assert testData.elementAt(i).classified.equals("not classified") == false: "Test data no. " + i + "not classified";
		}
	}
	
	private void generateConfusionMatrix(){
	// Revise / test
		
		// rows = original classes of test data
		// columns = classified classes of data
		
		int num = DataPoint.numberOfLabels(testData);
		Vector<double[]> matrix = new Vector<double[]>();

		testDataLabels = DataPoint.getLabels(testData);
		assert num == testDataLabels.length;
		
		for (int i = 0; i < num; i ++){
			double[] temp = new double[model.length];
			for (int j = 0; j < model.length; j ++)
				temp[j] = 0;
			matrix.addElement(temp);
		}
		
		System.out.println("Working out confusion matrix...");
		for (int x = 0; x < testData.size(); x ++){
			for (int i = 0; i < num; i ++){
				for (int j = 0; j < model.length; j ++){
					if (testData.elementAt(x).label.equals(testDataLabels[i]) && testData.elementAt(x).classified.equals(model[j].class_label))
						matrix.elementAt(i)[j] ++;
				}
			}
		}
		
		confusion = MatrixUtils.createRealMatrix(num, model.length);
		for (int i = 0; i < num; i ++) confusion.setRow(i, matrix.elementAt(i));	//?

	}
	
	public void printConfusion(){
				
		System.out.println("Confusion Matrix:");
		System.out.println("Vertical | True class; Horizontal -> Classified class");
		System.out.println("Words:");
		for (int i = 0; i < model.length; i ++)
			System.out.print("\t" + model[i].class_label);
		System.out.println();
		for (int i = 0; i < model.length; i ++){
			double[] nowRow = confusion.getRow(i);
			int[] intRow = new int[nowRow.length];
			for (int j = 0; j < nowRow.length; j ++) intRow[j] = (int)nowRow[j];
			System.out.print(testDataLabels[i] + "\t");
			for (int j = 0; j < nowRow.length; j ++)
				System.out.print("\t" + intRow[j]);
			System.out.println();
		}
	}
}
