package uranusworm.util;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Visu extends JPanel {
	// visualise points in x-y coordinates
	
	private JFrame frame;
	private Vector<Vector<double[]>> data;
	private String[] word;
	private int w, h;
	
	private Vector<Vector<double[]>> rescaled_data;
	
	public Visu(Vector<Vector<double[]>> d, String[] wd){
		
		w = 750;
		h = 750;
		
		word = wd;
		data = d;

		rescaled_data = rescale(d);
		
		frame = new JFrame();
		frame.setSize(w, h);   // Set frame size
		frame.setLocation(0, 0); // Set location on screen
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Sample points 2D distribution");
		
		// Set the properties of this panel
		this.setLocation(0, 0); // Set at upper left corner of the JFrame
		// this.setSize(width, height); // Don't do this, let the JFrame set its size
		frame.getContentPane().add(this);  // Add this to the JFrame
		
		// All drawing in the panel is done in the overridden paint() function below
		frame.setVisible(true);
	} // end constructor
	

	/** Override the paint() function. */
	public void paint(Graphics g){
		
		int pointcount = 0;
		
		// Create a white background for the entire panel
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, this.getWidth(), this.getHeight());
		g.setColor(Color.BLACK);
		g.drawString("0", 5, 710);
		g.drawString("x", 710, 710);
		g.drawString("y", 5, 15);

		int labx, laby;
		labx = 5;
		laby = 690;
		
		
		for (int i = 0; i < rescaled_data.size(); i ++){
			
			Vector<double[]> point = rescaled_data.elementAt(i);
	
//			Color current = getColour(i);
//			Color current = getRandomColour();		
			Color current = getListColour(i);		
			
			g.setColor(current);
			
			g.drawString(word[i], labx, laby);
			laby = laby-10;
			
			for (int j = 0; j < point.size(); j++){
				int x, y;
				x = (int)((double)w*point.elementAt(j)[0]);
				y = (int)((double)h-(double)h*point.elementAt(j)[1]);
				g.setColor(current);
				g.fillOval(x, y, 5, 5);
				pointcount++;
			}
			
			
		}

		System.out.println();
		System.out.println(pointcount+" points");
//		g.setColor(Color.GREEN);
//		g.fillOval(215, 75, 50, 50);
//		g.setColor(Color.BLUE);
//		g.fillOval(280, 140, 50, 50);
		
//		g.setColor(Color.BLACK);
//		g.drawString("Filled Ovals", 215, 220);
	
		
//		// Draw 3 circles
//		g.setColor(Color.RED);
//		g.drawOval(150, 230, 50, 50);
//		g.setColor(Color.GREEN);
//		g.drawOval(215, 295, 50, 50);
//		g.setColor(Color.BLUE);
//		g.drawOval(280, 355, 50, 50);
//		g.setColor(Color.BLACK);
//		g.drawString("Ovals", 215, 435);
		
	}
	
	private Vector<Vector<double[]>> rescale(Vector<Vector<double[]>> p){
		
		Vector<Vector<double[]>> rs = new Vector<Vector<double[]>>();
		
		// rescale
		double max_x, max_y, min_x, min_y;
		max_x = max_y = -100;
		min_x = min_y = 100;
		for (int j = 0; j < p.size(); j++){
			for (int i = 0; i < p.elementAt(j).size(); i ++){
				double x = p.elementAt(j).elementAt(i)[0];
				double y = p.elementAt(j).elementAt(i)[1];
				if (x>max_x) max_x = x;
				if (y>max_y) max_y = y;
				if (x<min_x) min_x = x;
				if (y<min_y) min_y = y;
			}
		}
		
//		System.out.println("max x = " + max_x);
//		System.out.println("min x = " + min_x);
//		System.out.println("max y = " + max_y);
//		System.out.println("min y = " + min_y);
		
		double spin_x = max_x - min_x;
		double spin_y = max_y - min_y;
		
		for (int j = 0; j < p.size(); j++){
			
			Vector<double[]> temprs = new Vector<double[]>();
			
			for (int i = 0; i < p.elementAt(j).size(); i ++){
			
				double x = p.elementAt(j).elementAt(i)[0];
				double y = p.elementAt(j).elementAt(i)[1];
			
				System.out.println("original coordinates = "+ x +", "+y);
				x = (x-min_x)/spin_x;
				y = (y-min_y)/spin_y;
			
				System.out.println("rescaled coordinates = "+ x +", "+y);
				double[] temp = {x,y};
				temprs.addElement(temp);
			}
			rs.addElement(temprs);
		}
		
		return rs;
	}
//	
//	private Vector<double[]> rescale(Vector<double[]> p){
//		
//		Vector<double[]> rs = new Vector<double[]>();
//		
//		// rescale
//		double max_x, max_y, min_x, min_y;
//		max_x = max_y = -100;
//		min_x = min_y = 100;
//		for (int j = 0; j < p.size(); j++){
//			double x = p.elementAt(j)[0];
//			double y = p.elementAt(j)[1];
//			if (x>max_x) max_x = x;
//			if (y>max_y) max_y = y;
//			if (x<min_x) min_x = x;
//			if (y<min_y) min_y = y;
//		}
//		
////		System.out.println("max x = " + max_x);
////		System.out.println("min x = " + min_x);
////		System.out.println("max y = " + max_y);
////		System.out.println("min y = " + min_y);
//		
//		double spin_x = max_x - min_x;
//		double spin_y = max_y - min_y;
//		
//		for (int j = 0; j < p.size(); j++){
//			
//			double x = p.elementAt(j)[0];
//			double y = p.elementAt(j)[1];
//			
//			System.out.println("original coordinates = "+ x +", "+y);
//			x = (x-min_x)/spin_x;
//			y = (y-min_y)/spin_y;
//			
//			System.out.println("rescaled coordinates = "+ x +", "+y);
//			double[] temp = {x,y};
//			rs.addElement(temp);
//		}
//		
//		return rs;
//	}
	
	private Color getColour(int index){
		// return distinct colours according to number of categories
		
		int num = data.size();	// number of colours
		
		final float hue = ((float)index)*(360f/(float)num);
//		System.out.println("index="+index+"hue="+hue);
		final float saturation = 1.0f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 1.0f; //1.0 for brighter, 0.0 for black
		Color rdColor = Color.getHSBColor(hue, saturation, luminance);
		
		return rdColor;
	}
	
	private Color getRandomColour(){
		
//		Random rand = new Random();
//		float r = rand.nextFloat();
//		float g = rand.nextFloat();
//		float b = rand.nextFloat();
//		Color randomColor = new Color(r, g, b);
//		randomColor.brighter();

		// return a really random color
		Random rd = new Random();
		final float hue = rd.nextFloat();
		final float saturation = 0.95f;//1.0 for brilliant, 0.0 for dull
		final float luminance = 0.8f; //1.0 for brighter, 0.0 for black
//		Color rdColor = Color.getHSBColor(hue, saturation, luminance);
		Color rdColor = new Color(Color.HSBtoRGB(hue, saturation, luminance));
		

		return rdColor;
	}
	
	

	private Color getListColour(int index){

		Vector<Color> colourList = new Vector<Color>();
		
		
//		colourList.addElement(new Color(0xF0, 0xF8, 0xFF)); // Alice blue
//		colourList.addElement(new Color(0xFA, 0xEB, 0xD7)); // Antique White
//		colourList.addElement(new Color(0x00, 0xFF, 0xFF)); // Aqua
//		colourList.addElement(new Color(0x7F, 0xFF, 0xD4)); // Aquamarine
//		colourList.addElement(new Color(0xF0, 0xFF, 0xFF)); // Azure
//		colourList.addElement(new Color(0xF5, 0xF5, 0xDC)); // Beige
//		colourList.addElement(new Color(0xFF, 0xE4, 0xC4)); // Bisque
//		colourList.addElement(new Color(0x00, 0x00, 0x00)); // Black
//		colourList.addElement(new Color(0xFF, 0xEB, 0xCD)); // Blanched Almond
//		colourList.addElement(new Color(0x00, 0x00, 0xFF)); // Blue
//		colourList.addElement(new Color(0x8A, 0x2B, 0xE2)); // Blue Violet
//		colourList.addElement(new Color(0xA5, 0x2A, 0x2A)); // Brown
//		colourList.addElement(new Color(0xDE, 0xB8, 0x87)); // Burly Wood
//		colourList.addElement(new Color(0x5F, 0x9E, 0xA0)); // Cadet Blue
//		colourList.addElement(new Color(0x7F, 0xFF, 0x00)); // Chartreuse
//		colourList.addElement(new Color(0xD2, 0x69, 0x1E)); // Chocolate
//		colourList.addElement(new Color(0xFF, 0x7F, 0x50)); // Coral
//		colourList.addElement(new Color(0x64, 0x95, 0xED)); // Cornflower Blue
//		colourList.addElement(new Color(0xFF, 0xF8, 0xDC)); // Cornsilk
		colourList.addElement(new Color(0xDC, 0x14, 0x3C)); // Crimson
//		colourList.addElement(new Color(0x00, 0xFF, 0xFF)); // Cyan
		colourList.addElement(new Color(0x00, 0x00, 0x8B)); // Dark Blue
//		colourList.addElement(new Color(0x00, 0x8B, 0x8B)); // Dark Cyan
//		colourList.addElement(new Color(0xB8, 0x86, 0x0B)); // Dark Golden Rod
//		colourList.addElement(new Color(0xA9, 0xA9, 0xA9)); // Dark Grey
		colourList.addElement(new Color(0x00, 0x64, 0x00)); // Dark Green
//		colourList.addElement(new Color(0xBD, 0xB7, 0x6B)); // Dark Khaki
//		colourList.addElement(new Color(0x8B, 0x00, 0x8B)); // Dark Magenta
//		colourList.addElement(new Color(0x55, 0x6B, 0x2F)); // Dark Olive Green
		colourList.addElement(new Color(0xFF, 0x8C, 0x00)); // Dark Orange
		colourList.addElement(new Color(0x99, 0x32, 0xCC)); // Dark Orchid
		colourList.addElement(new Color(0x8B, 0x00, 0x00)); // Dark Red
//		colourList.addElement(new Color(0xE9, 0x96, 0x7A)); // Dark Salmon
//		colourList.addElement(new Color(0x8F, 0xBC, 0x8F)); // Dark Sea Green
//		colourList.addElement(new Color(0x48, 0x3D, 0x8B)); // Dark Slate Blue
//		colourList.addElement(new Color(0x2F, 0x4F, 0x4F)); // Dark Slate Grey
		colourList.addElement(new Color(0x00, 0xCE, 0xD1)); // Dark Turquoise
//		colourList.addElement(new Color(0x94, 0x00, 0xD3)); // Dark Violet
		colourList.addElement(new Color(0xFF, 0x14, 0x93)); // Deep Pink
		colourList.addElement(new Color(0x00, 0xBF, 0xFF)); // Deep Sky Blue
//		colourList.addElement(new Color(0x69, 0x69, 0x69)); // Dim Grey
		colourList.addElement(new Color(0x1E, 0x90, 0xFF)); // Dodger Blue
		colourList.addElement(new Color(0xB2, 0x22, 0x22)); // Fire Brick
//		colourList.addElement(new Color(0xFF, 0xFA, 0xF0)); // Floral White
		colourList.addElement(new Color(0x22, 0x8B, 0x22)); // Forest Green
		colourList.addElement(new Color(0xFF, 0x00, 0xFF)); // Fuchsia
//		colourList.addElement(new Color(0xDC, 0xDC, 0xDC)); // Gainsboro
//		colourList.addElement(new Color(0xF8, 0xF8, 0xFF)); // GhostWhite
		colourList.addElement(new Color(0xFF, 0xD7, 0x00)); // Gold
		colourList.addElement(new Color(0xDA, 0xA5, 0x20)); // Golden Rod
		colourList.addElement(new Color(0x80, 0x80, 0x80)); // Grey
		colourList.addElement(new Color(0x00, 0x80, 0x00)); // Green
		colourList.addElement(new Color(0xAD, 0xFF, 0x2F)); // Green Yellow
//		colourList.addElement(new Color(0xF0, 0xFF, 0xF0)); // Honey Dew
		colourList.addElement(new Color(0xFF, 0x69, 0xB4)); // Hot Pink
		colourList.addElement(new Color(0xCD, 0x5C, 0x5C)); // Indian Red
		colourList.addElement(new Color(0x4B, 0x00, 0x82)); // Indigo
//		colourList.addElement(new Color(0xFF, 0xFF, 0xF0)); // Ivory
		colourList.addElement(new Color(0xF0, 0xE6, 0x8C)); // Khaki
		colourList.addElement(new Color(0xE6, 0xE6, 0xFA)); // Lavender
//		colourList.addElement(new Color(0xFF, 0xF0, 0xF5)); // Lavender Blush
		colourList.addElement(new Color(0x7C, 0xFC, 0x00)); // Lawn Green
//		colourList.addElement(new Color(0xFF, 0xFA, 0xCD)); // Lemon Chiffon
		colourList.addElement(new Color(0xAD, 0xD8, 0xE6)); // Light Blue
		colourList.addElement(new Color(0xF0, 0x80, 0x80)); // Light Coral
//		colourList.addElement(new Color(0xE0, 0xFF, 0xFF)); // Light Cyan
//		colourList.addElement(new Color(0xFA, 0xFA, 0xD2)); // Light Golden Rod Yellow
//		colourList.addElement(new Color(0xD3, 0xD3, 0xD3)); // Light Grey
		colourList.addElement(new Color(0x90, 0xEE, 0x90)); // Light Green
		colourList.addElement(new Color(0xFF, 0xB6, 0xC1)); // Light Pink
		colourList.addElement(new Color(0xFF, 0xA0, 0x7A)); // Light Salmon
		colourList.addElement(new Color(0x20, 0xB2, 0xAA)); // Light Sea Green
		colourList.addElement(new Color(0x87, 0xCE, 0xFA)); // Light Sky Blue
		colourList.addElement(new Color(0x77, 0x88, 0x99)); // Light Slate Grey
		colourList.addElement(new Color(0xB0, 0xC4, 0xDE)); // Light Steel Blue
//		colourList.addElement(new Color(0xFF, 0xFF, 0xE0)); // Light Yellow
		colourList.addElement(new Color(0x00, 0xFF, 0x00)); // Lime
		colourList.addElement(new Color(0x32, 0xCD, 0x32)); // Lime Green
//		colourList.addElement(new Color(0xFA, 0xF0, 0xE6)); // Linen
		colourList.addElement(new Color(0xFF, 0x00, 0xFF)); // Magenta
		colourList.addElement(new Color(0x80, 0x00, 0x00)); // Maroon
		colourList.addElement(new Color(0x66, 0xCD, 0xAA)); // Medium Aqua Marine
		colourList.addElement(new Color(0x00, 0x00, 0xCD)); // Medium Blue
		colourList.addElement(new Color(0xBA, 0x55, 0xD3)); // Medium Orchid
		colourList.addElement(new Color(0x93, 0x70, 0xDB)); // Medium Purple
		colourList.addElement(new Color(0x3C, 0xB3, 0x71)); // Medium Sea Green
		colourList.addElement(new Color(0x7B, 0x68, 0xEE)); // Medium Slate Blue
		colourList.addElement(new Color(0x00, 0xFA, 0x9A)); // Medium Spring Green
		colourList.addElement(new Color(0x48, 0xD1, 0xCC)); // Medium Turquoise
		colourList.addElement(new Color(0xC7, 0x15, 0x85)); // Medium Violet Red
		colourList.addElement(new Color(0x19, 0x19, 0x70)); // Midnight Blue
//		colourList.addElement(new Color(0xF5, 0xFF, 0xFA)); // Mint Cream
//		colourList.addElement(new Color(0xFF, 0xE4, 0xE1)); // Misty Rose
//		colourList.addElement(new Color(0xFF, 0xE4, 0xB5)); // Moccasin
		colourList.addElement(new Color(0xFF, 0xDE, 0xAD)); // Navajo White
		colourList.addElement(new Color(0x00, 0x00, 0x80)); // Navy
//		colourList.addElement(new Color(0xFD, 0xF5, 0xE6)); // Old Lace
		colourList.addElement(new Color(0x80, 0x80, 0x00)); // Olive
		colourList.addElement(new Color(0x6B, 0x8E, 0x23)); // Olive Drab
		colourList.addElement(new Color(0xFF, 0xA5, 0x00)); // Orange
		colourList.addElement(new Color(0xFF, 0x45, 0x00)); // Orange Red
		colourList.addElement(new Color(0xDA, 0x70, 0xD6)); // Orchid
//		colourList.addElement(new Color(0xEE, 0xE8, 0xAA)); // Pale Golden Rod
		colourList.addElement(new Color(0x98, 0xFB, 0x98)); // Pale Green
		colourList.addElement(new Color(0xAF, 0xEE, 0xEE)); // Pale Turquoise
		colourList.addElement(new Color(0xDB, 0x70, 0x93)); // Pale Violet Red
		colourList.addElement(new Color(0xFF, 0xEF, 0xD5)); // Papaya Whip
		colourList.addElement(new Color(0xFF, 0xDA, 0xB9)); // Peach Puff
		colourList.addElement(new Color(0xCD, 0x85, 0x3F)); // Peru
		colourList.addElement(new Color(0xFF, 0xC0, 0xCB)); // Pink
		colourList.addElement(new Color(0xDD, 0xA0, 0xDD)); // Plum
		colourList.addElement(new Color(0xB0, 0xE0, 0xE6)); // Powder Blue
		colourList.addElement(new Color(0x80, 0x00, 0x80)); // Purple
		colourList.addElement(new Color(0xFF, 0x00, 0x00)); // Red
		colourList.addElement(new Color(0xBC, 0x8F, 0x8F)); // Rosy Brown
		colourList.addElement(new Color(0x41, 0x69, 0xE1)); // Royal Blue
		colourList.addElement(new Color(0x8B, 0x45, 0x13)); // Saddle Brown
		colourList.addElement(new Color(0xFA, 0x80, 0x72)); // Salmon
		colourList.addElement(new Color(0xF4, 0xA4, 0x60)); // Sandy Brown
		colourList.addElement(new Color(0x2E, 0x8B, 0x57)); // Sea Green
//		colourList.addElement(new Color(0xFF, 0xF5, 0xEE)); // Sea Shell
		colourList.addElement(new Color(0xA0, 0x52, 0x2D)); // Sienna
		colourList.addElement(new Color(0xC0, 0xC0, 0xC0)); // Silver
		colourList.addElement(new Color(0x87, 0xCE, 0xEB)); // Sky Blue
		colourList.addElement(new Color(0x6A, 0x5A, 0xCD)); // Slate Blue
		colourList.addElement(new Color(0x70, 0x80, 0x90)); // Slate Grey
//		colourList.addElement(new Color(0xFF, 0xFA, 0xFA)); // Snow
		colourList.addElement(new Color(0x00, 0xFF, 0x7F)); // Spring Green
		colourList.addElement(new Color(0x46, 0x82, 0xB4)); // Steel Blue
		colourList.addElement(new Color(0xD2, 0xB4, 0x8C)); // Tan
		colourList.addElement(new Color(0x00, 0x80, 0x80)); // Teal
		colourList.addElement(new Color(0xD8, 0xBF, 0xD8)); // Thistle
		colourList.addElement(new Color(0xFF, 0x63, 0x47)); // Tomato
		colourList.addElement(new Color(0x40, 0xE0, 0xD0)); // Turquoise
		colourList.addElement(new Color(0xEE, 0x82, 0xEE)); // Violet
//		colourList.addElement(new Color(0xF5, 0xDE, 0xB3)); // Wheat
//		colourList.addElement(new Color(0xFF, 0xFF, 0xFF)); // White
//		colourList.addElement(new Color(0xF5, 0xF5, 0xF5)); // White Smoke
		colourList.addElement(new Color(0xFF, 0xFF, 0x00)); // Yellow
		colourList.addElement(new Color(0x9A, 0xCD, 0x32)); // Yellow Green
		
		
		return colourList.elementAt(index);
	}
}
