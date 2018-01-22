package uw;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;

import uranusworm.ft.Cell;
import uranusworm.ft.Uwimg;
import uranusworm.seg.Foreground;
import uranusworm.tool.ImageMani;
import uranusworm.tool.Moments;
import uranusworm.util.Configs;
import uranusworm.util.ImageViewer;
import uranusworm.util.ImgConverter;
import uranusworm.util.Printer;
import uranusworm.util.Scaler;
import uranusworm.util.Visu;

public class Test {

	public static void visualmodule(){
		
		Vector<Vector<double[]>> d = new Vector<Vector<double[]>>();
		
		Vector<double[]> first = new Vector<double[]>();
		double[] temp1 = {0.02, 0.45}; first.add(temp1);
		double[] temp2 = {0.94, 0.66}; first.add(temp2);
		d.addElement(first);
		
		Vector<double[]> second = new Vector<double[]>();
		double[] temp3 = {0.35, 0.22}; second.add(temp3);
		double[] temp4 = {0.37, 0.30}; second.add(temp4);
		d.addElement(second);
		
		Vector<double[]> third = new Vector<double[]>();
		double[] temp5 = {0.76, 0.51}; third.add(temp5);
		double[] temp6 = {0.88, 0.13}; third.add(temp6);
		d.addElement(third);
		
		String[] wd = new String[3];
		wd[0] = "first"; wd[1] = "second"; wd[2] = "third";
		
		new Visu(d, wd);
	}
	
//	public static void humoments(String filename) throws IOException, InterruptedException{
//		BufferedImage img = ImageIO.read(new File(filename));
//		Uwimg u = new Uwimg(img, "");
//	}
	
	public static void maskGeneration(String filename) throws IOException, InterruptedException{
		BufferedImage img = ImageIO.read(new File(filename));
		Mat m = ImgConverter.bfd2Mat(img);
		Mat mask = Foreground.salCutMask(m);
		new ImageViewer(m, "original");
		new ImageViewer(mask, "mask");
	}
	
	public static void humoments(String filename){
		Mat m = ImageMani.imread(filename);
		double[] mo = Moments.huMoments(m, m);
		Printer.printArray(mo);
	}
	
	public static void maskbinarise(){
		Mat mask = ImageMani.imread("015-m.jpg");
		Mat enf = Foreground.binariseMask(mask);
		ImageMani.imwrite("enf.jpg", enf);
	}
//	public static void dividingCells(String filename) throws IOException, InterruptedException{
//		
//		System.out.println("Now testing cell dividing function: obtaining coordinates of a single cell");
//		BufferedImage img = ImageIO.read(new File(filename));
//		System.out.println("Image size = "+img.getWidth()+"x"+img.getHeight());
//		new ImageViewer(img, "the image");
//		Uwimg u = new Uwimg(img, "");
//		System.out.println("Current configurations:");
//		System.out.println("Cell size = "+Configs.CELLSIZEX+"x"+Configs.CELLSIZEY);
//		System.out.println("Moving pace = "+Configs.MOVINGPACEX+"x"+Configs.MOVINGPACEY);
//		int cellr = 1;
//		int cellc = 1;
//		System.out.println("Appointed cell: row "+cellr+", col "+cellc);
////		Cell c = new Cell(u, cellr, cellc);
////		System.out.println("top-left point of the cell ("+c.x+", "+c.y+")");
//	}
	
	public static void savemask() throws IOException, InterruptedException{
	
//		Mat original = ImageMani.imread("002.png");
// 		Mat small = Scaler.rescale(original, 300);
 		
//		Mat mask = Foreground.salCutMask(original); 
//		Mat mask = Foreground.salCutMask(small); 
 		Mat mask = ImageMani.imread("005-m.png");

		System.out.println("num of channels = " +mask.channels());
		System.out.println(mask.dump());
	}
}
