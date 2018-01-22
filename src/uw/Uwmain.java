package uw;

import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.RejectedExecutionException;

import javax.swing.UnsupportedLookAndFeelException;

import org.opencv.core.Core;

public class Uwmain {


	public static void main (String[] args) throws IOException, InterruptedException, ClassNotFoundException, InstantiationException, IllegalAccessException, UnsupportedLookAndFeelException, IllegalArgumentException, IllegalStateException, RejectedExecutionException, ExecutionException{
		
		long time = System.currentTimeMillis();
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
		System.out.println("Now running...");
//		Test.maskGeneration("10.jpg");
		
//		Test.humoments("recmask.jpg");
//		Test.visualmodule();
//		Box.generateAndSaveData("data");
		
//		Test.savemask();
//		Test.maskbinarise();
//		Box.segmentation();
		Box.batchsegmentation();
//		Box.batchsegEnforced();
//		Box.countObjects();		
		
		System.out.print("Time = " + (System.currentTimeMillis()-time) + "ms");
		
	}
}
