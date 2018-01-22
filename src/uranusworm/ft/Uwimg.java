package uranusworm.ft;

import java.io.IOException;
import java.util.Vector;

import org.opencv.core.Mat;


import uranusworm.seg.Foreground;
import uranusworm.util.Configs;

import uranusworm.util.Printer;

public class Uwimg {
	
	String location;		// directory+filename
	public String keyword;			// linked keyword (word used to retrieve the image)
	
	Mat img;				// the image
	Mat greyscale;				
	Mat imgmask;	
	
	//	BufferedImage image;	//
	//	BufferedImage grey;		// greyscale
	//	BufferedImage mask;		// region of foreground object
//	int rows, cols;			// rows = height, cols = width
	
	// generated cell information
	Vector<Cell[]> patches;	// length = col / size = row
	int cellRows, cellCols;
	
	// image feature list
	Vector<Feature> feature;		// feature
	public double[] ftvector;		// feature vector
	
	
//	// CONSTRUCTORS
	
	public Uwimg(Mat m, String loc) throws IOException, InterruptedException{
		initialise(m, loc);
		computeFeature();
		generateFeatureVector();
	}
	
	public Uwimg(Mat m, Mat msk, String loc){
		initialise(m, msk, loc);
		computeFeature();
		generateFeatureVector();
	}
//	
//	public Uwimg(BufferedImage img, String loc) throws IOException, InterruptedException{
//		initialise(img, loc);
//		computeFeature();
//		generateFeatureVector();
//	}
//	
//	public Uwimg(BufferedImage img, BufferedImage msk, String loc){
//		initialise(img, msk, loc);
//		computeFeature();
//		generateFeatureVector();
//	}
	
	// initialising methods
	private void initialise(Mat m, String loc) throws IOException, InterruptedException{
		
		img = m;
		location = loc;

		greyscale = Foreground.getGrey(m);
		
		imgmask = generateMatMask();
//		generateCellInfo();
	}
	
	private void initialise(Mat m, Mat msk, String loc){
		
		img = m;
		location = loc;
	
		greyscale = Foreground.getGrey(m);
		
		imgmask = msk;
//		generateCellInfo();
	}	
	
	// initialising methods
//	private void initialise(BufferedImage img, String loc) throws IOException, InterruptedException{
//		image = img;
//		location = loc;
//		rows = image.getHeight();
//		cols = image.getWidth();
//		
//		grey = getGreyscale();
//		
//		mask = generateMask();
//		generateCellInfo();
//	}
//	
//	private void initialise(BufferedImage img, BufferedImage msk, String loc){
//		image = img;
//		location = loc;
//		rows = image.getHeight();
//		cols = image.getWidth();
//		
//		grey = getGreyscale();
//		
//		mask = msk;
//		generateCellInfo();
//	}	
	
	// set keyword from outside
	public void setWord(String w){
		keyword = w;
	}

	// dividing cells
	private void generateCellInfo(){

		patches = Cell.divideImg(this);
		cellCols = patches.size();
		cellRows = patches.elementAt(0).length;
	}
	
//	private BufferedImage getGreyscale(){
//		return ImgConverter.mat2Bfd(Foreground.getGrey(ImgConverter.bfd2Mat(image)));
//	}
//	
//	private BufferedImage generateMask() throws IOException, InterruptedException{
//		return ImgConverter.mat2Bfd(Foreground.salCutMask(ImgConverter.bfd2Mat(image)));
//	}
	
	private Mat generateMatMask() throws IOException, InterruptedException{
		return Foreground.salCutMask(img);
	}
	
	// add features to feature list
	private void computeFeature(){
		
		feature = new Vector<Feature>();
		
		Feature hu_moments = new Feature(this, Configs.HUMOMENTS);
		feature.addElement(hu_moments);
		
//		Feature colour_his = new Feature(this, Configs.FEATURECOLOURBGR);
//		feature.addElement(colour_his);
		
	}
	
	// combine feature list to a single feature vector
	private void generateFeatureVector(){
//		ftvector = hu_moments.vector;
		ftvector = Feature.generateFtVector(feature);
		System.out.println("feature vector:");
		Printer.printArrayInARow(ftvector);
	}

}
