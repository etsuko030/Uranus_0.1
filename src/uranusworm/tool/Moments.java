package uranusworm.tool;

import org.opencv.core.Mat;

import uranusworm.seg.Foreground;
import uranusworm.util.Configs;

public class Moments {

	public static double[] huMoments(Mat binary){
	// note: Hu invariant moments only need a binary image of shape
		
        double[] moments = new double[7];

        double
        n20 = normalisedCentralMoment(binary, binary, 2, 0),
        n02 = normalisedCentralMoment(binary, binary, 0, 2),
        n30 = normalisedCentralMoment(binary, binary, 3, 0),
        n12 = normalisedCentralMoment(binary, binary, 1, 2),
        n21 = normalisedCentralMoment(binary, binary, 2, 1),
        n03 = normalisedCentralMoment(binary, binary, 0, 3),
        n11 = normalisedCentralMoment(binary, binary, 1, 1);
        
        //First moment
        moments[0] = n20 + n02;
        
        //Second moment
        moments[1] = Math.pow((n20 - n02), 2) + Math.pow(2 * n11, 2);
        
        //Third moment
        moments[2] = Math.pow(n30 - (3 * n12), 2)
                   + Math.pow((3 * n21 - n03), 2);
        
        //Fourth moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n21 + n03), 2);
        
        //Fifth moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (3 * n21 - n03) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
        //Sixth moment
        moments[5] = (n20 - n02)
                     * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                     + 4 * n11 * (n30 + n12) * (n21 + n03);
        
        //Seventh moment
        moments[6] = (3 * n21 - n30) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (3 * n12 - n30) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
//        //Eighth moment
//        moments[7] = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
//                         - (n20 - n02) * (n30 + n12) * (n03 + n21);
        
        return moments;
	}
	
	public static double[] huMoments(Mat grey, Mat mask){
    // (a not used version)
		
        double[] moments = new double[7];

        double
        n20 = normalisedCentralMoment(grey, mask, 2, 0),
        n02 = normalisedCentralMoment(grey, mask, 0, 2),
        n30 = normalisedCentralMoment(grey, mask, 3, 0),
        n12 = normalisedCentralMoment(grey, mask, 1, 2),
        n21 = normalisedCentralMoment(grey, mask, 2, 1),
        n03 = normalisedCentralMoment(grey, mask, 0, 3),
        n11 = normalisedCentralMoment(grey, mask, 1, 1);
        
        //First moment
        moments[0] = n20 + n02;
        
        //Second moment
        moments[1] = Math.pow((n20 - n02), 2) + Math.pow(2 * n11, 2);
        
        //Third moment
        moments[2] = Math.pow(n30 - (3 * n12), 2)
                   + Math.pow((3 * n21 - n03), 2);
        
        //Fourth moment
        moments[3] = Math.pow((n30 + n12), 2) + Math.pow((n21 + n03), 2);
        
        //Fifth moment
        moments[4] = (n30 - 3 * n12) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (3 * n21 - n03) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
        //Sixth moment
        moments[5] = (n20 - n02)
                     * (Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2))
                     + 4 * n11 * (n30 + n12) * (n21 + n03);
        
        //Seventh moment
        moments[6] = (3 * n21 - n30) * (n30 + n12)
                     * (Math.pow((n30 + n12), 2) - 3 * Math.pow((n21 + n03), 2))
                     + (3 * n12 - n30) * (n21 + n03)
                     * (3 * Math.pow((n30 + n12), 2) - Math.pow((n21 + n03), 2));
        
//        //Eighth moment
//        moments[7] = n11 * (Math.pow((n30 + n12), 2) - Math.pow((n03 + n21), 2))
//                         - (n20 - n02) * (n30 + n12) * (n03 + n21);
        
        return moments;
	}
	
	// 21-dim invariant moments with colour weights
	public static double[] huMomentsBGR(Mat img, Mat mask){
        
        double[] moments = new double[7*3];

        double
        n20b = normalisedCentralMoment(img, mask, 2, 0, 0),
        n20g = normalisedCentralMoment(img, mask, 2, 0, 1),
        n20r = normalisedCentralMoment(img, mask, 2, 0, 2),
        
        n02b = normalisedCentralMoment(img, mask, 0, 2, 0),
        n02g = normalisedCentralMoment(img, mask, 0, 2, 1),
        n02r = normalisedCentralMoment(img, mask, 0, 2, 2),
        
        n30b = normalisedCentralMoment(img, mask, 3, 0, 0),
        n30g = normalisedCentralMoment(img, mask, 3, 0, 1),
        n30r = normalisedCentralMoment(img, mask, 3, 0, 2),
                        
        n12b = normalisedCentralMoment(img, mask, 1, 2, 0),
        n12g = normalisedCentralMoment(img, mask, 1, 2, 1),
        n12r = normalisedCentralMoment(img, mask, 1, 2, 2),
        
        n21b = normalisedCentralMoment(img, mask, 2, 1, 0),
        n21g = normalisedCentralMoment(img, mask, 2, 1, 1),     
        n21r = normalisedCentralMoment(img, mask, 2, 1, 2),    
        
        n03b = normalisedCentralMoment(img, mask, 0, 3, 0),
        n03g = normalisedCentralMoment(img, mask, 0, 3, 1),
        n03r = normalisedCentralMoment(img, mask, 0, 3, 2),
        
        n11b = normalisedCentralMoment(img, mask, 1, 1, 0),
        n11g = normalisedCentralMoment(img, mask, 1, 1, 1),
        n11r = normalisedCentralMoment(img, mask, 1, 1, 2);
        
        //First moment BGR
        moments[0] = n20b + n02b;
        moments[1] = n20g + n02g;
        moments[2] = n20r + n02r;
        
        //Second moment BGR
        moments[3] = Math.pow((n20b - n02b), 2) + Math.pow(2 * n11b, 2);
        moments[4] = Math.pow((n20g - n02g), 2) + Math.pow(2 * n11g, 2);
        moments[5] = Math.pow((n20r - n02r), 2) + Math.pow(2 * n11r, 2);
        
        //Third moment BGR
        moments[6] = Math.pow(n30b - (3 * n12b), 2)
                   + Math.pow((3 * n21b - n03b), 2);
        moments[7] = Math.pow(n30g - (3 * n12g), 2)
                + Math.pow((3 * n21g - n03g), 2);
        moments[8] = Math.pow(n30r - (3 * n12r), 2)
                + Math.pow((3 * n21r - n03r), 2);
        
        //Fourth moment BGR
        moments[9] = Math.pow((n30b + n12b), 2) + Math.pow((n21b + n03b), 2);
        moments[10] = Math.pow((n30g + n12g), 2) + Math.pow((n21g + n03g), 2);
        moments[11] = Math.pow((n30r + n12r), 2) + Math.pow((n21r + n03r), 2);
        
        //Fifth moment BGR
        moments[12] = (n30b - 3 * n12b) * (n30b + n12b)
                     * (Math.pow((n30b + n12b), 2) - 3 * Math.pow((n21b + n03b), 2))
                     + (3 * n21b - n03b) * (n21b + n03b)
                     * (3 * Math.pow((n30b + n12b), 2) - Math.pow((n21b + n03b), 2));
        moments[13] = (n30g - 3 * n12g) * (n30g + n12g)
                * (Math.pow((n30g + n12g), 2) - 3 * Math.pow((n21g + n03g), 2))
                + (3 * n21g - n03g) * (n21g + n03g)
                * (3 * Math.pow((n30g + n12g), 2) - Math.pow((n21g + n03g), 2));
        moments[14] = (n30r - 3 * n12r) * (n30r + n12r)
                * (Math.pow((n30r + n12r), 2) - 3 * Math.pow((n21r + n03r), 2))
                + (3 * n21r - n03r) * (n21r + n03r)
                * (3 * Math.pow((n30r + n12r), 2) - Math.pow((n21r + n03r), 2));
        
        //Sixth moment BGR
        moments[15] = (n20b - n02b)
                     * (Math.pow((n30b + n12b), 2) - Math.pow((n21b + n03b), 2))
                     + 4 * n11b * (n30b + n12b) * (n21b + n03b);
        moments[16] = (n20g - n02g)
                * (Math.pow((n30g + n12g), 2) - Math.pow((n21g + n03g), 2))
                + 4 * n11g * (n30g + n12g) * (n21g + n03g);
        moments[17] = (n20r - n02r)
                * (Math.pow((n30r + n12r), 2) - Math.pow((n21r + n03r), 2))
                + 4 * n11r * (n30r + n12r) * (n21r + n03r);
        
        //Seventh moment BGR
        moments[18] = (3 * n21b - n30b) * (n30b + n12b)
                     * (Math.pow((n30b + n12b), 2) - 3 * Math.pow((n21b + n03b), 2))
                     + (3 * n12b - n30b) * (n21b + n03b)
                     * (3 * Math.pow((n30b + n12b), 2) - Math.pow((n21b + n03b), 2));
        moments[19] = (3 * n21g - n30g) * (n30g + n12g)
                * (Math.pow((n30g + n12g), 2) - 3 * Math.pow((n21g + n03g), 2))
                + (3 * n12g - n30g) * (n21g + n03g)
                * (3 * Math.pow((n30g + n12g), 2) - Math.pow((n21g + n03g), 2));
        moments[20] = (3 * n21r - n30r) * (n30r + n12r)
                * (Math.pow((n30r + n12r), 2) - 3 * Math.pow((n21r + n03r), 2))
                + (3 * n12r - n30r) * (n21r + n03r)
                * (3 * Math.pow((n30r + n12r), 2) - Math.pow((n21r + n03r), 2));

        return moments;
	}

	public static double normalisedCentralMoment(Mat grey, Mat mask, int p, int q){
		
		double gamma = ((double)(p+q))/2 +1;
		double upq = getCentralMoment(grey, mask, p, q);
		double u00gamma = Math.pow(getCentralMoment(grey, mask, 0, 0), gamma);
//		System.out.println("p="+p+", q="+q+", gamma="+gamma+", upq="+upq+", u00gamma="+u00gamma);
		return upq/u00gamma;
	}

	// with colour channel
	public static double normalisedCentralMoment(Mat img, Mat mask, int p, int q, int channel){
		
		double gamma = ((double)(p+q))/2 +1;
		double upq = getCentralMoment(img, mask, p, q, channel);
		double u00gamma = Math.pow(getCentralMoment(img, mask, 0, 0, channel), gamma);
//		System.out.println("p="+p+", q="+q+", gamma="+gamma+", upq="+upq+", u00gamma="+u00gamma);
		return upq/u00gamma;
	}
	
	// with colour channel
	public static double getCentralMoment(Mat img, Mat mask, int p, int q, int channel){
		
        int width = img.width();
        int height = img.height();
        
        // get centroid of image
//        DoublePoint centroid = getCentroid(fastBitmap);
        double m00 = getRawMoment(img, mask, 0, 0, channel);
        double m10 = getRawMoment(img, mask, 1, 0, channel);
        double m01 = getRawMoment(img, mask, 0, 1, channel);
        double x0 = m10 / m00;
        double y0 = m01 / m00;
// 		centroid = (x0, y0)
        
        double mc = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
                	if(Foreground.isPixelInsideMask(mask, i, j)){
                    	double value = img.get(i, j)[channel];
                		mc += Math.pow((i - x0), p) * Math.pow((j - y0), q) * value;
                	}
 //               	}
                }
        }
        
        return mc;
	}
	
	public static double getCentralMoment(Mat grey, Mat mask, int p, int q){
		

        int width = grey.width();
        int height = grey.height();
        
        // get centroid of image
//        DoublePoint centroid = getCentroid(fastBitmap);
        double m00 = getRawMoment(grey, mask, 0, 0);
        double m10 = getRawMoment(grey, mask, 1, 0);
        double m01 = getRawMoment(grey, mask, 0, 1);
        double x0 = m10 / m00;
        double y0 = m01 / m00;
// 		centroid = (x0, y0)
        
        double mc = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
//                	if(Foreground.isPixelInsideMask(mask, i, j)){
                    	double value;
                    	if (Configs.GSINTENSITY) value = grey.get(i, j)[0];
                    	else {
                    		if (mask.get(i, j)[0]>128) value = 1;
                    		else value = 0;
                    	}
                		mc += Math.pow((i - x0), p) * Math.pow((j - y0), q) * value;
 //               	}
                }
        }
        
        return mc;
	}

	public static double getCentralMoment(Mat grey, int p, int q){
		

        int width = grey.width();
         int height = grey.height();
        
        // get centroid of image
//        DoublePoint centroid = getCentroid(fastBitmap);
        double m00 = getRawMoment(grey, 0, 0);
        double m10 = getRawMoment(grey, 1, 0);
        double m01 = getRawMoment(grey, 0, 1);
        double x0 = m10 / m00;
        double y0 = m01 / m00;
// 		centroid = (x0, y0)
        
        double mc = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
                    mc += Math.pow((i - x0), p) * Math.pow((j - y0), q) * grey.get(i, j)[0];
                }
        }
        
        return mc;
	}
	
	// with colour channel
    public static double getRawMoment(Mat img, Mat mask, int p, int q, int channel) {
        
        int width = img.width();
        int height = img.height();
        
        double m = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
                	double value = img.get(i, j)[channel];
                	// get point value
                	if(Foreground.isPixelInsideMask(mask, i, j))
                		m += Math.pow(i, p) * Math.pow(j, q) * value;
                }
        }
        return m;
    }
    
    public static double getRawMoment(Mat img, Mat mask, int p, int q) {
        
        int width = img.width();
        int height = img.height();
        
        double m = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
                	double value;
                	if (Configs.GSINTENSITY) value = img.get(i, j)[0];
                	else {
                		if (mask.get(i, j)[0]>128) value = 1;
                		else value = 0;
                	}
                	// get point value
//                	if(Foreground.isPixelInsideMask(mask, i, j))
                		m += Math.pow(i, p) * Math.pow(j, q) * value;
                }
        }
        return m;
    }

    public static double getRawMoment(Mat img, int p, int q) {
        
        int width = img.width();
        int height = img.height();
        
        double m = 0;
        for (int i = 0, k = height; i < k; i++) {
                for (int j = 0, l = width; j < l; j++) {
                	// get point value
                    m += Math.pow(i, p) * Math.pow(j, q) * img.get(i, j)[0];
                }
        }
        return m;
    }
}
