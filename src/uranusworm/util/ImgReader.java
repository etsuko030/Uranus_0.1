package uranusworm.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;





import uranusworm.ft.Uwimg;
import uranusworm.util.Configs;
import uranusworm.util.PathRetriever;


public class ImgReader {
	// read location and folder name of
	// images from local folders
	
	// add file names / file address
	
	
	public String parentpath;
	public String[] imgPath;
	public String[] keyword;
	
	public ImgReader (PathRetriever r){
		parentpath = r.path;
		imgPath = r.childPath;
		keyword = r.fdName;
	}
	
	public Uwimg[] getUwimg(int index) throws IOException, InterruptedException{
		// index correspondent to that of keyword
		
		if (!Configs.MASKPRESENT) return getImg(imgPath[index]);
		// if the configs say that folders include masks
		else return getImgMask(imgPath[index]);
	}
	
	public Mat[] getImgFromFile(int index){
		// index correspondent to that of keyword
		if (!Configs.MASKPRESENT) return getImages(imgPath[index]);
		// if the configs say that folders include masks
		else return getImagesMask(imgPath[index]);
	}
//	
//	public Icecream[] getIceFromFile(int index) throws IOException, InterruptedException{
//		
//		Icecream[] ice;
//		
//		if(!Configs.MASKPRESENT){	// use built-in segmentation
//			Icecream[] file = getIce(imgPath[index]);
////			Icecream[] temp = new Icecream[file.length];
////			System.out.println("Reading " +file.length+" images with this keyword");
////			for (int b = 0; b < file.length; b ++){
////				System.out.println("image no. " +b);
////				temp[b] = new Icecream(file[b]);
////			}
//			ice = file;
//		}
//		
//		else if(Configs.MASKPRESENT){	// use pre-segmented images
//			Icecream[] file = getIceMask(imgPath[index]);
////			Icecream[] temp = new Icecream[file.length/2];
////			System.out.println("Reading " +(file.length/2)+" images (+ same number of masks) with this keyword");
////			for (int b = 0; b < file.length/2; b ++){
////				System.out.println("image no. " +b);
////				temp[b] = new Icecream(file[2*b], file[2*b+1]);
////			}
//			ice = file;
//		}
//		
//		else {
//			System.err.println("Error in Configs - will use default settings");
//			System.err.println("Applying built-in segmentation programme ...");
//			Icecream[] file = getIce(imgPath[index]);
////			Icecream[] temp = new Icecream[file.length];
////			System.out.println("Reading " +file.length+" images with this keyword");
////			for (int b = 0; b < file.length; b ++){
////				System.out.println("image no. " +b);
////				temp[b] = new Icecream(file[b]);
////			}
//			ice = file;
//		}
//		
//		
//		return ice;
//	}
/*
 *  READ IMAGES FROM LOCAL FILE
 */
	
	// read only images
	public static Uwimg[] getImg (String path) throws IOException, InterruptedException{
	// read all applicable images from a given folder path
	// also goes into child directories to get images
	
		Vector<File> f = getAllFile (path);
	    int count = 0;
	    
//	    System.out.println(f.size() + " files get from selected path");
	    
	    for (int i = 0; i < f.size(); i ++){   // number of images
	    	if(isImage(f.elementAt(i))) count ++;
	    }
	    
	    System.out.println(count + " images loaded");
	    
    	File[] images = new File[count];
    	int j = 0;
	    for (int i = 0; i < f.size(); i ++) {
	    	if(isImage(f.elementAt(i))){ 
	    		images[j] = f.elementAt(i);
	    		j ++;
	    	}
	    }

		Uwimg[] img = new Uwimg[images.length];
		for (int i = 0; i < img.length; i ++){
//			BufferedImage temp = ImageIO.read(images[i]);
			Mat temp = Imgcodecs.imread (images[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
			String loc = images[i].getAbsolutePath();
			img[i] = new Uwimg(temp, loc);
			System.out.println("Image no. "+i+": "+loc);
		}
		return img;
	}
	
	// get images AND masks
	public static Uwimg[] getImgMask (String path) throws IOException, InterruptedException{
	// read all applicable images from a given folder path
	// and find correspondent masks according to file names (see Box - segmentation)
	// in the array each mask is allocated right following its original image 
	
		Vector<File> f = getAllFile (path);
	    int count = 0;
	    
//	    System.out.println(f.size() + " files get from selected path");
	    
	    for (int i = 0; i < f.size(); i ++){   // number of images
	    	if(isImage(f.elementAt(i))) count ++;
	    }
	    System.out.println(count + " images found from folder");
	    
    	File[] images = new File[count];
    	int num = 0;	
	    for (int i = 0; i < f.size(); i ++) {
	    	if(isImage(f.elementAt(i))){
	    		// if this image is the original version 
	    		if (!isMask(f.elementAt(i))){
	    			images[num*2] = f.elementAt(i);
		    		images[num*2+1] = findMaskByName(f.elementAt(i));
		    		// put mask after image in the array
		    		num ++;
	    		}
	    		else continue;
	    	}
	    }
	    System.out.println(num + " images with correspondent masks");
	    
//		BufferedImage[] pack = new BufferedImage[num*2];
//		for (int i = 0; i < pack.length; i ++)
//			pack[i] = ImageIO.read(images[i]);
//		

		Mat[] pack = new Mat[num*2];
		
		for (int i = 0; i < pack.length; i ++)
			pack[i] = Imgcodecs.imread (images[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		
		Uwimg[] img = new Uwimg[num];
		for (int i = 0; i < img.length; i ++){
			String loc = images[2*i].getAbsolutePath();
			img[i] = new Uwimg(pack[2*i], pack[2*i+1], loc);
			System.out.println("Image no. "+i+": "+loc);
		}

		return img;
	}
	
	// read only images
	public static Mat[] getImages (String path){
	// read all applicable images from a given folder path
	// also goes into child directories to get images
	
		Vector<File> f = getAllFile (path);
	    int count = 0;
	    
//	    System.out.println(f.size() + " files get from selected path");
	    
	    for (int i = 0; i < f.size(); i ++){   // number of images
	    	if(isImage(f.elementAt(i))) count ++;
	    }
	    
	    System.out.println(count + " images loaded");
	    
    	File[] images = new File[count];
    	int j = 0;
	    for (int i = 0; i < f.size(); i ++) {
	    	if(isImage(f.elementAt(i))){ 
	    		images[j] = f.elementAt(i);
	    		j ++;
	    	}
	    }
	   
	    
		Mat[] pack = new Mat[images.length];
		
		for (int i = 0; i < pack.length; i ++)
			pack[i] = Imgcodecs.imread (images[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		
		return pack;
	}
	

	// get images AND masks
	public static Mat[] getImagesMask (String path){
	// read all applicable images from a given folder path
	// and find correspondent masks according to file names (see Box - segmentation)
	// in the array each mask is allocated right following its original image 
	
		Vector<File> f = getAllFile (path);
	    int count = 0;
	    
//	    System.out.println(f.size() + " files get from selected path");
	    
	    for (int i = 0; i < f.size(); i ++){   // number of images
	    	if(isImage(f.elementAt(i))) count ++;
	    }
	    
	    System.out.println(count + " images found from folder");
	    
	    
    	File[] images = new File[count];
    	int num = 0;	
	    for (int i = 0; i < f.size(); i ++) {
	    	if(isImage(f.elementAt(i))){
	    		// if this image is the original version 
	    		if (!isMask(f.elementAt(i))){
	    			images[num*2] = f.elementAt(i);
		    		images[num*2+1] = findMaskByName(f.elementAt(i));
		    		// put mask after image in the array
		    		num ++;
	    		}
	    		else continue;
	    	}
	    }

	    System.out.println(num + " images with correspondent masks");
	    
		Mat[] pack = new Mat[num*2];
		
		for (int i = 0; i < pack.length; i ++)
			pack[i] = Imgcodecs.imread (images[i].getAbsolutePath(), Imgcodecs.CV_LOAD_IMAGE_ANYCOLOR);
		
		return pack;
	}
	
	
	public static boolean isImage(File f){
		// judge if a file is an image by restricting file name extension
		
		// COULD ADD SOME CODE e.g. for judging how many channels one has
		
		
		if (f.getName().toLowerCase().endsWith("jpg"))
			return true;
		else if (f.getName().toLowerCase().endsWith("png"))
			return true;
		else return false;
		
	}

	public static boolean isMask(File f){
		// judge if a file is a mask by restricting file name
		// (GIVEN that masks are obtained from Box - segmentation)
		
		// COULD ADD: judge by reading pixel values
		// (ususally black and white)
		// (or by channels?)
		
		if (f.getName().toLowerCase().endsWith("-m.jpg"))
			return true;
		else return false;
		
	}
	
	public static File findMaskByName(File image){
		String imagename = image.getAbsolutePath();
//		System.out.println("image name: "+imagename);

//		int length = imagename.length();
//		CharSequence seq = imagename.subSequence(length-4, length); // stops before index "length"
//
//		String ending = "-m.jpg";
//		CharSequence newseq = ending.subSequence(0, 6);
//		String newname = imagename.replace(seq, newseq);
//		
		String newname = imagename.replaceFirst(".jpg", "-m.jpg");

//		System.out.println("replaced image name: "+newname);
		File mask = new File(newname);
		
		return mask;
	}
	
	public static Vector<File> getAllFile (String path){
		// get all file from a directory
		
		File dir = new File(path);
	    File[] files = dir.listFiles();
	    
		Vector<File> fd = new Vector<File>();  // folder list
		Vector<File> f = new Vector<File>();   // file list
		
		int fileN = 0, fdN = 0; // file number and folder number
		
		for (int i = 0; i < files.length; i ++) {
			
			if(files[i].isDirectory()){
				fd.addElement(files[i]);
				fdN ++;
//				System.out.println("dir num"+ fdN + ":" + files[i].getAbsolutePath());
			} 
			else { 
				f.addElement(files[i]);
				fileN ++;
//		    	System.out.println("file num"+ fileN + ":" + files[i].getAbsolutePath());
			}   
			
		}
		
		File t = null;
		
		while (!fd.isEmpty()) {
			
			t = fd.remove(0);  // remove head, give head to t
			
			if (t.isDirectory()) {
				
			    files = t.listFiles();
			    if (files == null) continue;
			    
			    for (int i = 0; i < files.length; i++) {
			    	
			    	if (files[i].isDirectory()) { 
			    		fd.addElement(files[i]);
			    		fdN ++;
//			    		System.out.println("dir num"+ fdN + ":" + files[i].getAbsolutePath());
			    	}
			    	else {
			    		f.addElement(files[i]);
			    		fileN ++;
			    	}
//			    	System.out.println("file num"+ fileN + ":" + files[i].getAbsolutePath());
			    	
			    }
			    
			}
			
			else {
			    fileN ++;
//			    System.out.println("file num" + fileN + "]:" + t.getAbsolutePath());
			}
			
		}
		
		return f;
	}
	
}
