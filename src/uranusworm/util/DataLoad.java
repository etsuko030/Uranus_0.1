package uranusworm.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;

public class DataLoad {

	Scanner scanfile;
	Vector<double[]> value;
	Vector<String> label;
	
	public DataLoad(String filepath) throws FileNotFoundException{
		
		File featureTxt = new File(filepath);
		scanfile = new Scanner(featureTxt);
		
		loadVectors();
		loadClasses();
	}
	
	private void loadVectors(){
		value = new Vector<double[]>();
		
	}
	
	private void loadClasses(){
		label = new Vector<String>();
		
	}
//	 OOOOOOOOOOOO
//	private void readFileData() throws IOException{
//		
//		read_file = new File(read_path);
////		FileReader fr = new FileReader(read_file.getAbsoluteFile());
////		BufferedReader rd = new BufferedReader(fr);
//		
//		Scanner scan = new Scanner(read_file);
//		String line;
//		Vector<double[]> c = new Vector<double[]>();
//	    while (scan.hasNextLine()) {
//
//	    	line = scan.nextLine();
////	    	System.out.println(line);
//	    	double[] temp = string2Array(line);
//	    	c.addElement(temp);
//	    }
//	    scan.close();
//	    imData = new BoVW(c.size(), c);
//	}
//	
	private double[] string2Array(String s){
		
		int len = s.length();
		int size = 0;
		for (int i = 0; i < len; i ++){
			if (s.charAt(i)==',') size++;
		}// size of double array
		
		Scanner scan = new Scanner(s);
		scan.useDelimiter(",");
		double[] temp = new double[size];
		for (int i = 0; i < size; i ++){
			temp[i] = scan.nextDouble();
	    }
	    scan.close();
		return temp;
	}
}
