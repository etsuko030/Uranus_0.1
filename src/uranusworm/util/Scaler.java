package uranusworm.util;

import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class Scaler {

	public static Mat rescale(Mat img, int size){
	// set the longest edge to specified size 
		
		int orgHeight, orgWidth, newH, newW;
		
		orgWidth = img.cols();
		orgHeight = img.rows();
		
		Mat res = new Mat();	// result
		
		if (orgWidth>size || orgHeight>size){
			
			System.out.println("Image original size: "+orgWidth+"x"+orgHeight);
			
			if (orgWidth>=orgHeight){
			newW = size;
			double tempH = ((double)(orgHeight*newW)/(double)orgWidth);
			newH = (int)Math.floor(tempH);
			}
			else {
				newH = size;
				double tempW = ((double)(orgWidth*newH)/(double)orgHeight);
				newW = (int)Math.floor(tempW);
			}		
			System.out.println("New size: "+newW+"x"+newH);
		
			Size r = new Size(newW, newH);
			Imgproc.resize(img, res, r);
		}
		
		else {
			System.out.println("This image has a suitable size: "+orgWidth+"x"+orgHeight+": skip cropping");
			res = img; // do not crop
		}
		
		return res;
	}
	
	
	public static Mat crop(Mat img, int size){
	//	crop out the left-top part of the size of size x size
		
		int orgHeight, orgWidth, newH, newW;
		
		orgWidth = img.rows();
		orgHeight = img.cols();
		
		Mat res = new Mat();
		
		if (orgWidth>size || orgHeight>size){
			
			System.out.println("Image original size: "+orgWidth+"x"+orgHeight);
			
			if (orgWidth>=orgHeight){
			newW = size;
			double tempH = ((double)(orgHeight*newW)/(double)orgWidth);
			newH = (int)Math.floor(tempH);
			}
			else {
				newH = size;
				double tempW = ((double)(orgWidth*newH)/(double)orgHeight);
				newW = (int)Math.floor(tempW);
			}		
			System.out.println("New size: "+newW+"x"+newH);
			Rect crop = new Rect(0, 0, newW, newH);
			res = new Mat(img, crop);
		}
		
		else {
			System.out.println("This image has a suitable size: skip cropping");
			res = img; // do not crop
		}
		
		return res;
	}
}
