package uranusworm.seg;

import java.io.File;
import java.io.IOException;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import uranusworm.util.Configs;

public class Foreground {

	Mat mask;
	
	public Foreground(){
		
	}
	
	public static Mat getGrey(Mat m){
		Mat mg = new Mat(m.rows(), m.cols(), m.type());
		Imgproc.cvtColor(m, mg, Imgproc.COLOR_RGB2GRAY);
		return mg;
	}
	
	public static Mat simpleTshMask (Mat m){

		// original to grey-scale
		Mat mg = getGrey(m);
		// mask
		Mat mask = new Mat(m.rows(), m.cols(), m.type());
		Imgproc.threshold(mg, mask, 0, 255, Imgproc.THRESH_BINARY + Imgproc.THRESH_OTSU);
		
		Core.bitwise_not(mask, mask, new Mat());	// ?
		return mask;
	}

	public static Mat salCutMask (Mat m) throws IOException, InterruptedException{
		
		Imgcodecs.imwrite("input.png", m);
//		Mat newimg = Imgcodecs.imread("input.jpg", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
//		new Displayer(newimg, "original");
		
//		Runtime rt = Runtime.getRuntime();
		
		String progName = new String();
		if (Configs.SALCUTVER==1) progName = "SalCut-d.exe";
		else if (Configs.SALCUTVER==0) progName = "SalCut.exe";
		
		Process salcut = new ProcessBuilder(progName, "input.png", "reg.png", "output.png").start();
		salcut.waitFor();
//		salcut.getOutputStream().write('\n');
//		salcut.getOutputStream().write('\n');
//		System.out.println("alive = "+salcut.isAlive());

		// mask
		Mat mask = Imgcodecs.imread("output.png", Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
//		
//		File f1 = new File("input.png");
//		File f2 = new File("reg.png");
//		File f3 = new File("output.png");
//		
//		f1.delete();
//		f2.delete();
//		f3.delete();
//		

//		Core.bitwise_not(mask, mask, new Mat());
		return mask;
	}
	
	public static boolean isPixelInsideMask(Mat mask, int row, int col){
		if (mask.get(row, col)[0]>128) return true;
		else return false;
	}
	
	public static Mat binariseMask(Mat mask){
	//	transform mask to binary image
		
		
		// NOT USABLE
		
		Mat res = new Mat(mask.rows(), mask.cols(), CvType.CV_8U);
//		Mat res = Mat.ones(mask.rows(), mask.cols(), CvType.CV_8U);
		
//		Core.bitwise_and(mask, res, res);
//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Imgproc.threshold(mask, res, 127, 255, Imgproc.THRESH_BINARY);
		
//		Imgproc.threshold(res, res, 127, 255, Imgproc.THRESH_BINARY_INV);
//		Imgproc.threshold(res, res, 127, 255, Imgproc.THRESH_TOZERO);
		
		return res;
	}
}
