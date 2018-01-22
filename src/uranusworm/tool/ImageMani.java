package uranusworm.tool;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class ImageMani {

	public static Mat imread(String filename){
		Mat m = Imgcodecs.imread (filename, Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		return m;
	}
	
	public static BufferedImage imreadJava(String filename) throws IOException{
		BufferedImage img = ImageIO.read(new File(filename));
		return img;
	}
	
	public static boolean imwrite(String filename, Mat img){
		return Imgcodecs.imwrite(filename, img);
	}
	
	public static Mat imcreate(Mat original){
		// create a new image with same size and type
		Mat res = new Mat(original.rows(), original.cols(), original.type());
		return res;
	}
//	public static boolean imwriteJava(String filename, BufferedImage img){
//		
//		return Imgcodecs.imwrite(filename, img);
//	}
}
