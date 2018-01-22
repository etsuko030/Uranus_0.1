package uranusworm.util;

import java.awt.FlowLayout;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Mat;

public class ImageViewer extends JFrame {
	
	// this class loads an image (Mat or Image) in a new window
	
	public ImageViewer (Mat m, String title) {   
		displayImage ( ImgConverter.mat2Bfd(m), title);
	}
	
	public ImageViewer (BufferedImage img, String title){
		displayImage (img, title);
	}
	
    public void displayImage(Image img, String title) {

    	int width = img.getWidth(null);
    	int height = img.getHeight(null);
    	
    	ImageIcon icon = new ImageIcon (img);
    	JFrame frame = new JFrame();
    	
    	frame.setLayout (new FlowLayout());        
    	frame.setSize (width+20, height+40);
    	
    	JLabel lbl = new JLabel();
    	lbl.setIcon (icon);
    	frame.add (lbl);
    	frame.setTitle(title);
    	
    	frame.setResizable(true);
    	frame.setVisible (true);
    	frame.setDefaultCloseOperation (JFrame.DISPOSE_ON_CLOSE);
    }
}
