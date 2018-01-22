package uranusworm.tool;

import java.util.Vector;

import org.opencv.core.Mat;

import uranusworm.ft.Cell;
import uranusworm.ft.Uwimg;
import uranusworm.util.Configs;
import uranusworm.util.DataConverter;
import uranusworm.util.Printer;


public class Colour {

	public static double[] colourHistogram(Uwimg img){
		
		int b = Configs.COLOURBINNUM;
		
		Vector<Vector<Mat>> patches = Cell.getMaskFilteredPatches(img);
		Mat[] p = DataConverter.vecOfVec2Array(patches);
		assert p!=null;
		Vector <double[]> c = getPatchColour(p);
		assert c!=null;
		
		int num = c.elementAt(0).length;
//		System.out.println("num="+num);
		double[] h = new double[b*num];
		for (int i = 0; i < num; i ++){
			double[] temp = getOneChannelHist(c, i);
//			System.out.println("temp length ="+temp.length);
			System.arraycopy(temp, 0, h, i*b, b);
		}
		
		Printer.printArrayInARow(h);
		
		return h;
	}
	
	public static Vector<double[]> getPatchColour(Mat[] p){
		Vector<double[]> c = new Vector<double[]>();
		for (int i = 0; i < p.length; i ++){
			if(p[i]!=null)
				c.addElement(patchColourMean(p[i]));
		}
		return c;
	}
	
	public static double[] patchColourMean (Mat p){
		// compute average for channels
		
		double[] m = new double[p.channels()];
	//	System.out.println("patch colour channels: " +p.channels());
		int h = p.rows();
		int w = p.cols();
		
		for ( int y = 0; y < h; y ++)
			for ( int x = 0; x < w; x ++){
				double [] bgr = p.get(y, x);
				for (int i = 0; i < p.channels(); i ++)
					m[i] += bgr [i];
			}
		for (int i = 0; i < p.channels(); i ++){
			m[i] = m[i]/(h*w);
//			System.out.println("color "+ i + ": " + m[i]);
		}
//		// to 0-1
//		for (int i = 0; i < p.channels(); i ++){
//			m[i] = m[i]/255.0;
//		}

		
//		return m/255.0;
		return m;
	}
	
	private static double[] getOneChannelHist(Vector<double[]> color, int index){
		// for RGB 0 - 255
		int min = Configs.RGBMIN;
		int range = Configs.RGBRANGE;
		int b = Configs.COLOURBINNUM;
		double bin = (double)range / (double) b;
		
		double[] h = new double[b];
		for (int i = 0; i < b; i ++) h[i] = 0;
		
		for (int i = 0; i < color.size(); i ++){
			double value = color.elementAt(i)[index];
//			System.out.println("value="+value);
			int label = (int)((value-(double)min)/(double)bin);
//			System.out.println("label="+label);
			h[label]++;
		}
		System.out.println("one channel:");
		Printer.printArrayInARow(h);
		
		// normalise against number of patches -???
//		int size = color.size();
//		for (int i = 0; i < h.length; i++){
//			h[i] = h[i]/(double)size;
////			System.out.println("hi="+h[i]);
//		}
//		System.out.println("one channel:");
//		Printer.printArrayInARow(h);
		return h;
	}
	
	public static double cellChannelMean(Mat cell, int channel){
		// RGB mean [0,1] for all pixels in p 
		// opencv channel 0/1/2 : blue/green/red
		
		double m = 0;
		double [] bgr;
		
		int h = cell.rows();
		int w = cell.cols();

		for ( int y = 0; y < h; y ++)
			for ( int x = 0; x < w; x ++){
				bgr = cell.get(y, x);
				m += bgr [channel];
			}
		m = m/(h*w);
		
//		return m/255.0;
		return m;
	}

	public static double cellChannelSD (Mat cell, int channel) {
		// RGB standard deviation for all pixels in p 
		// opencv channel 0/1/2 : blue/green/red
		
		double m = 0;
		double msq = 0;
		double bgr [];
		
		int h = cell.rows();
		int w = cell.cols();
		
		for ( int y = 0; y < h; y ++)
			for ( int x = 0; x < w; x ++){
				bgr = cell.get(y, x);
				m += bgr [channel];
				msq += (bgr [channel] * bgr [channel]);
			}
		m = m /(h*w);
		msq = msq /(h*w);
		
		return Math.sqrt ( (msq - m*m)/(255.0*255.0) );
	}
	
	
}
