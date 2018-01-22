package uranusworm.util;

public class Configs {
	// Experiment configurations
	
	public Configs(){
		
	}
	
	// epsilon added to eigenvalues in mahalanobis distance
	public static double MDIS_EPSILON = 0.000001;
	
	// version of programmes used
	public static int SALCUTVER = 1;	// 1: desktop
										// 0: laptop
	
	// file folders include masks or not
	public static boolean MASKPRESENT = true;
	
	// code of feature choices
	public static int HUMOMENTS = 100;
	public static int FEATURECOLOURBGR = 101;
	public static int FEATURECOLOURYUV = 102;
	public static int FEATURECOLOURHSV = 103;
	
	public static int RGBHOG = 110;
	
	public static int BOWRGBHOG = 200;
	public static boolean BAGOFWORDS = false;
	
	// moment invariants
	public static int ORDERP = 5;
	public static int ORDERQ = 5;
	public static boolean GSINTENSITY = true; // use grey-scale intensity
											  // else use 0-1 value for 0-255 according to mask
	
	
	// parameters in colour feature
	public static int COLOURBINNUM = 4;
	public static int RGBMAX = 255;
	public static int RGBMIN = 0;
	public static int RGBRANGE = RGBMAX - RGBMIN +1 ;
	
	// parameters for segmentation
	public static int SEGMETHOD = 1;	// choose segmentation method
	public static int SIMPLETHRSHOLD = 0;
	public static int SALGRABCUT = 1;
	
	
	// parameters in Lloyds.java
	public static double DISMEASUREVAL = 0.0005;
	public static int FPSCENTRES = 0;
	public static int RANDOMCENTRES = 1;
	public static int INITALCENTRES = FPSCENTRES;
	public static double CENTREEPSILON = 0.0001;
	
	// parameters in dimension reduction
	public static boolean DATANORM = true;
	public static double REDUCTIONRATE = 0.95;
	public static double BHAREDUCTIONRATE = 0.50;
	
	// parameters in cutting image patches
	public static int CELLSIZEX = 5;
	public static int CELLSIZEY = 5;
	public static int MOVINGPACEX = 5;
	public static int MOVINGPACEY = 5;
	
	// parameters in Bag of Visual Words method
	public static int VOCABULARY_SIZE = 100;
	
	
	// HOG configuration: only these default are supported in OpenCV
	public static int HOGCELLSIZE = 8;
	public static int HOGBLOCKSIZE = 16;
	public static int HOGNBINS = 9;
	// block stride size
	public static int HOGBSWIDTH = HOGBLOCKSIZE;
	public static int HOGBSHEIGHT = HOGBLOCKSIZE;
	// detecting window size; usually set to 64x128
	public static int HOGWINWIDTH = 64;
	public static int HOGWINHEIGHT = 128;
	
	
	// key point diameter for SIFT
	public static int KEYPOINTDIAMETER = 1;
	
}
