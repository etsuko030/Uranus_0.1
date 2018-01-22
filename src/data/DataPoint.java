package data;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Vector;

public class DataPoint {

	public String label;
	public double[] value;
	
	public String classified;
	
	public DataPoint(double[] vector, String keyword){
		value = vector;
		label = keyword;
		classified = "not classified";
		
		assert vector!=null;
		assert label!= null;
	}
	
	protected void renewValue(double[] newValue){
		value = newValue;
	}
	
	protected void assignClass(String classified_label){
		classified = classified_label;
	}
	
	public static Vector<double[]> pureDataFromPoints(Vector<DataPoint> p){
		
		Vector<double[]> d = new Vector<double[]>();
		
		for (int i = 0; i < p.size(); i ++)
			d.addElement(p.elementAt(i).value);
		
		return d;
	}
	
	protected static Vector<DataPoint> renewPoints(Vector<DataPoint> points, Vector<double[]> newValues){
		
		assert points.size() == newValues.size();
		
		for (int i = 0; i < points.size(); i ++)
			points.elementAt(i).renewValue(newValues.elementAt(i));
		
		return points;
	}
	
	public static int numberOfLabels(Vector<DataPoint> data){
		
		String temp = data.elementAt(0).label;
		int num = 1;
		
		for (int i = 0; i < data.size(); i ++){
			if (!data.elementAt(i).label.equals(temp)){
				num ++;
				temp = data.elementAt(i).label;
			}
		}
		
		return num;
	}
	
	public static String[] getLabels(Vector<DataPoint> data){
		
//		int num = numberOfLabels(data);
////		System.out.println("num = " + num);
//		String[] temp = new String[num];
//		String current = data.elementAt(0).label;
////		System.out.println("current = " + current);
//		int index = 0;
//		
//		
//		
//		
//		System.out.println("data.size = " + data.size());
		String[] names = new String[data.size()];
		for (int i = 0; i < names.length; i ++){
			names[i] = data.elementAt(i).label;
		}
		
		HashSet<String> set = new HashSet<String> (Arrays.asList(names));
		System.out.println("label set size = " + set.size());
		String[] result = new String[set.size()];
		set.toArray(result);
//		
//		for (int i = 0; i < data.size(); i ++){
//			if (!data.elementAt(i).label.equals(current)){
//				temp[index] = data.elementAt(i).label;
//				index ++;
//				current = temp[index];
//				System.out.println("new current = " + current);
//			}
//		}
//		
//		return temp;
		
		return result;
	}
	
	
}
