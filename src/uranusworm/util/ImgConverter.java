package uranusworm.util;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

public class ImgConverter {
	// converter between OpenCV Mat and Java BufferedImage
	
	public static BufferedImage mat2Bfd(Mat m){
		
		int type = BufferedImage.TYPE_BYTE_GRAY;
		if ( m.channels() > 1 ) {
		    type = BufferedImage.TYPE_3BYTE_BGR;
		}
		
		int bufferSize = m.channels() * m.cols() * m.rows();
		byte [] b = new byte [bufferSize];
		m.get ( 0, 0, b ); // get all the pixels
		BufferedImage img = new BufferedImage(m.cols(),m.rows(), type);
		
		final byte[] targetPixels = ( (DataBufferByte) img.getRaster().getDataBuffer() ).getData();
		System.arraycopy( b, 0, targetPixels, 0, b.length);  
		
		return img;
    } 
	
	public static Mat bfd2Mat(BufferedImage img){
		//  CANNOT DEAL WITH PNG IMAGES
		
		int height = img.getHeight();
		int width = img.getWidth();
		
		byte[] pixels = ((DataBufferByte)img.getRaster().getDataBuffer()).getData();
		Mat m = new Mat(height, width, CvType.CV_8UC3);
		m.put(0, 0, pixels);
		
		return m;
	} 
	
	public static BufferedImage[] batch_mat2Bfd(Mat[] m){
		BufferedImage[] img = new BufferedImage[m.length];
		for (int i = 0; i < m.length; i ++)
			img[i] = mat2Bfd(m[i]);
		return img;
	}
	
	public static Mat[] batch_bfd2Mat(BufferedImage[] img){
		Mat[] m = new Mat[img.length];
		for (int i = 0; i < m.length; i ++)
			m[i] = bfd2Mat(img[i]);
		return m;
	}
}
