package uranusworm.ft;

import java.util.Vector;

import uranusworm.tool.Colour;
import uranusworm.tool.Moments;
import uranusworm.util.Configs;


public class Feature {

	String name;
	double[] vector;
	
	protected Feature(Uwimg img, int code){
		
		if (code == Configs.HUMOMENTS){
			name = "Hu moments";
			vector = huMoments(img);
		}
		
		if (code == Configs.FEATURECOLOURBGR){
			name = "BGR colour histogram";
			vector = rgbColour(img);
		}
	}

	protected static double[] huMoments(Uwimg m){
		
		assert m.greyscale!=null: "greyscale image not initialised yet";
//		
//		Mat gs = ImgConverter.bfd2Mat(m.grey);
//		Mat msk = ImgConverter.bfd2Mat(m.mask);
//		
		
		return Moments.huMoments(m.imgmask);
//		return Moments.huMoments(ImgConverter.bfd2Mat(m.grey), ImgConverter.bfd2Mat(m.mask));

	}
	
	protected static double[] rgbColour(Uwimg m){
		
		assert m.greyscale!=null: "greyscale image not initialised yet";
		
		return Colour.colourHistogram(m);
	}
	
	protected static double[] generateFtVector(Vector<Feature> f){
		
		int sum = 0;
		int[] length = new int[f.size()];
		for (int i = 0; i < f.size(); i ++){
			length[i] = f.elementAt(i).vector.length;
			sum += length[i];
		}
		
		double[] result = new double[sum];
		int destPos = 0;
		for (int i = 0; i < f.size(); i ++){
			System.arraycopy(f.elementAt(i).vector, 0, result, destPos, length[i]);
			destPos += length[i];
		}
		
		return result;
	}
}
