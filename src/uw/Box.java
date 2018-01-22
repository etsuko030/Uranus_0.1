package uw;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

import com.mathworks.engine.EngineException;
import com.mathworks.engine.MatlabEngine;

import data.Classification;
import data.DataSave;
import data.DataSet;
import data.DataSet.ProjectedSet;
import data.Model;
import uranusworm.ft.*;
import uranusworm.seg.Foreground;
import uranusworm.tool.ImageMani;
import uranusworm.util.Calculate;
import uranusworm.util.ImgReader;
import uranusworm.util.PathRetriever;
import uranusworm.util.Scaler;
import uranusworm.util.Visu;

public class Box {

	public static void generateAndSaveData(String filename) throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, InterruptedException{
	//	generate data (training/test)
	//	and save to file
		
		PathRetriever pn = new PathRetriever("choose image folder");
		ImgReader ir = new ImgReader(pn);
		
		int wormNum = ir.keyword.length;
		Worm[] word = new Worm[wormNum];
		Vector<Uwimg[]> images = new Vector<Uwimg[]>();
		
		for (int a = 0; a < wormNum; a ++){
			String kw = ir.keyword[a];
			Uwimg[] temp = ir.getUwimg(a);
			for (int b = 0; b < temp.length; b ++){
				temp[b].setWord(kw);
			}
			word[a] = new Worm(kw, temp);
			images.addElement(temp);
		}

		DataSet original_data = new DataSet(images);
		ProjectedSet reduced = original_data.dimensionReduction();
		
		// test writing to file
		DataSave.saveTxt(pn.path, original_data);
//		DataSave.saveTxt(pn.path, reduced);
//		DataSave.saveTxt(pn.path, original_data.to2D());
		
		// create models
//		Model[] classes = new Model[ir.keyword.length];
//		for (int i = 0; i < ir.keyword.length; i ++){ 
////			classes[i] = new Model(original_data.collectPointsLabelled(ir.keyword[i]));
//			classes[i] = new Model(reduced.collectPointsLabelled(ir.keyword[i]));
//			classes[i].setClassLabel(ir.keyword[i]);
//		}
		
		// classification
//		Classification notest = new Classification(original_data.points, classes);
//		Classification notest = new Classification(reduced.points, classes);
//		notest.doClassification();
//		notest.printConfusion();
//		
//		DataSet now = new DataSet(word);
//		// no separate test set
//		now.reduceDimension();
//		now.classification(now);
//		
//		new Visu(now.reduced_data, now.keyword);
	}
	
	public static DataSet loadFeatureVector(String filename){
		
		return null;
	}
	
	public static void segmentation() throws ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IOException, InterruptedException{
	//	get images from folder(s) 
	//	save masks to folder(s)

		PathRetriever pn = new PathRetriever("choose source image folder");
		PathRetriever pt = new PathRetriever("choose segmentation result folder");
		String savingpath = pt.path;
		
		Mat[] original = ImgReader.getImages(pn.path);
		int filetotal = original.length;
		int filecount = 0;
//		Mat[] mask = new Mat[filetotal];

		// COULD add something to restrict file size
		// if not suitable resize an image before segmentation
		// and save all images resized
		for (int i = 0; i < filetotal; i ++){
			
			Mat thisimg = Scaler.rescale(original[i], 300);
//			Mat thisimg = original[i];
			Foreground.salCutMask(thisimg);
//			Mat mask = Foreground.simpleTshMask(thisimg);
			Mat mask = ImageMani.imread("output.png");
			Mat reg = ImageMani.imread("reg.png");
//			boolean flag = Uwimg.isGoodMask(mask[i]);
//			System.out.println("Image no."+i+" is good = "+flag);		
//			flag = true;
			
			String nameindex = String.valueOf(filecount+1);
			if (filecount+1<10) nameindex = "00"+nameindex;
			else if (filecount+1>=10 && filecount+1<100) nameindex = "0"+nameindex;
			
			String regionname = savingpath + "\\" + nameindex + "-reg.jpg";
			String maskname = savingpath + "\\" + nameindex + "-m.jpg";
			String imgname = savingpath + "\\" + nameindex + ".jpg";
//			if (flag){
				Imgcodecs.imwrite(imgname, thisimg);
				Imgcodecs.imwrite(maskname, mask);
				Imgcodecs.imwrite(regionname, reg);
				filecount++;
//			} else continue;
		}
		System.out.println(original.length+" images in source folder, "+ filecount +" images segmented, "+(filetotal-filecount)+" images thrown away.");
	}
	
	
	public static void batchsegmentation() throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException{
	// 12052017: import from data set of multiple folders
	// and segment
			
		PathRetriever pn = new PathRetriever("choose source image folder");
		PathRetriever pt = new PathRetriever("choose segmentation result folder");
		String savingpath = pt.path;	// saving root path
		String[] folderpath = pn.fdName;
		System.out.println("Copying folders:");
		for (int i = 0; i < folderpath.length; i++){
			folderpath[i] = savingpath + "\\" + folderpath[i];
			new File(folderpath[i]).mkdir();
			System.out.println(folderpath[i]);
		}
		
		for (int f = 0; f < pn.childPath.length; f ++){
			
			Mat[] original = ImgReader.getImages(pn.childPath[f]);
			int filetotal = original.length;
			int filecount = 0;
//			Mat[] mask = new Mat[filetotal];

			// COULD add something to restrict file size
			// if not suitable resize an image before segmentation
			// and save all images resized
			for (int i = 0; i < filetotal; i ++){
					
				Mat thisimg = Scaler.rescale(original[i], 300);
//				Mat thisimg = original[i];
				Foreground.salCutMask(thisimg);
//				Mat mask = Foreground.simpleTshMask(thisimg);
				Mat mask = ImageMani.imread("output.png");
				Mat reg = ImageMani.imread("reg.png");
				
//				boolean flag = Uwimg.isGoodMask(mask[i]);
//				System.out.println("Image no."+i+" is good = "+flag);		
//				flag = true;
					
				String nameindex = String.valueOf(filecount+1);
				if (filecount+1<10) nameindex = "00"+nameindex;
				else if (filecount+1>=10 && filecount+1<100) nameindex = "0"+nameindex;
				
				
				String regionname = folderpath[f] + "\\" + nameindex + "-reg.png";
				String maskname = folderpath[f] + "\\" + nameindex + "-m.png";
				String imgname = folderpath[f] + "\\" + nameindex + ".png";
//				if (flag){
					Imgcodecs.imwrite(imgname, thisimg); //System.out.println("write safe");
					Imgcodecs.imwrite(maskname, mask);
					Imgcodecs.imwrite(regionname, reg);
					filecount++;
//				} else continue;
			}
			System.out.println(original.length+" images in folder "+ pn.fdName[f] + ", "+ filecount +" images segmented, "+(filetotal-filecount)+" images thrown away.");
		}
		
	}
	
	public static void batchsegEnforced() throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IllegalArgumentException, IllegalStateException, RejectedExecutionException, ExecutionException{
	// keep original outputs and modify to get 0-1 valued mask
			
		
		MatlabEngine engine = MatlabEngine.startMatlab();
		System.out.println("MATLAB engine on");
		
		
		
		PathRetriever pn = new PathRetriever("choose source image folder");
		PathRetriever pt = new PathRetriever("choose segmentation result folder");
		String savingpath = pt.path;	// saving root path
		String[] folderpath = pn.fdName;
		System.out.println("Copying folders:");
		for (int i = 0; i < folderpath.length; i++){
			folderpath[i] = savingpath + "\\" + folderpath[i];
			new File(folderpath[i]).mkdir();
			System.out.println(folderpath[i]);
		}
		
		for (int f = 0; f < pn.childPath.length; f ++){
			
			Mat[] original = ImgReader.getImages(pn.childPath[f]);
			int filetotal = original.length;
			int filecount = 0;
//			Mat[] mask = new Mat[filetotal];

			// COULD add something to restrict file size
			// if not suitable resize an image before segmentation
			// and save all images resized
			for (int i = 0; i < filetotal; i ++){
					
				Mat thisimg = Scaler.rescale(original[i], 300);
//				Mat thisimg = original[i];
			
				Foreground.salCutMask(thisimg);
				
//				Mat mask = Foreground.simpleTshMask(thisimg);
				Mat mask = ImageMani.imread("output.png");
				// the original outputs of SalCut
			
				Mat reg = ImageMani.imread("reg.png");
				
				Mat out = ImageMani.imread("output.png");	// the mask
				
				//	using MATLAB to binarise mask (?)
				mask = engine.feval("binarymask", "output.png");

//				mask = Foreground.binariseMask(mask);
				
				
//				boolean flag = Uwimg.isGoodMask(mask[i]);
//				System.out.println("Image no."+i+" is good = "+flag);		
//				flag = true;
					
				String nameindex = String.valueOf(filecount+1);
				if (filecount+1<10) nameindex = "00"+nameindex;
				else if (filecount+1>=10 && filecount+1<100) nameindex = "0"+nameindex;
					
				String regionname = folderpath[f] + "\\" + nameindex + "-reg.png";
				String outputname = folderpath[f] + "\\" + nameindex + "-out.png";
				String maskname = folderpath[f] + "\\" + nameindex + "-m.jpg";
				String imgname = folderpath[f] + "\\" + nameindex + ".jpg";
				
//				if (flag){
					Imgcodecs.imwrite(imgname, thisimg); //System.out.println("write safe");
					Imgcodecs.imwrite(maskname, mask);
					Imgcodecs.imwrite(outputname, out);
					Imgcodecs.imwrite(regionname, reg);
					filecount++;
//				} else continue;
			}
			System.out.println(original.length+" images in folder "+ pn.fdName[f] + ", "+ filecount +" images segmented, "+(filetotal-filecount)+" images thrown away.");
		}
		
		

		
		engine.close();
		System.out.println("MATLAB engine off");
	}
	
	public static void countObjects() throws EngineException, IllegalArgumentException, IllegalStateException, InterruptedException{
	// load images with mask and save masks with object annotation to another folder
	//	multiple objects divided to single object masks
		
		MatlabEngine engine = MatlabEngine.startMatlab();
		System.out.println("MATLAB engine on");
		
		
		
		
		engine.close();
		System.out.println("MATLAB engine off");
	}
}
