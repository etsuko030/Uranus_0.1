package uranusworm.util;

import java.io.File;
import java.util.Vector;

import javax.swing.JFileChooser;  
import javax.swing.UIManager; 
import javax.swing.UnsupportedLookAndFeelException;

public class PathRetriever {
	
	public String path;
	
	public String[] childPath;
	public String[] fdName;		// store class names (child folder names)
	
	
	public PathRetriever(String title) throws ClassNotFoundException, 
	InstantiationException, IllegalAccessException,
	UnsupportedLookAndFeelException {
		
		chooseDir (title);

		if (path!=null) getChildPathName();
		else {
			childPath = null;
			fdName = null;
		}

	}
	
	
	public void chooseDir (String title) throws ClassNotFoundException, 
	InstantiationException, IllegalAccessException,
	UnsupportedLookAndFeelException {
		
		UIManager.setLookAndFeel (UIManager.getSystemLookAndFeelClassName());  
        JFileChooser chooseDir = new JFileChooser ();  
        
        chooseDir.setMultiSelectionEnabled(true);
        
        chooseDir.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);  
        chooseDir.setDialogTitle(title);  
        
        if ( JFileChooser.APPROVE_OPTION == chooseDir.showOpenDialog(null) ) { 
            path = chooseDir.getSelectedFile().getAbsolutePath();
        }  else path = null;
            
	}


	public void getChildPathName (){
		// get first level child paths (the class paths)
		
		File dir = new File(path);
	    File[] files = dir.listFiles();
	    
	    
	    Vector<File> fd = new Vector<File>();  // folder list
		
		for (int i = 0; i < files.length; i ++) {
			if(files[i].isDirectory())
				fd.addElement(files[i]);
		}
		
		
		childPath = new String[fd.size()];
		fdName = new String[fd.size()];
		
		for (int i = 0; i < fd.size(); i ++) {
			childPath[i] = fd.elementAt(i).getAbsolutePath();
			fdName[i] = fd.elementAt(i).getName();
		}

	}

	
	public static boolean isCharArrayAllEqual(char[] cha){
		
		int count = 0;
		
		for (int i = 0; i < cha.length - 1; i ++){
			if (cha[i] == cha[i+1]) {
				count ++;
				continue;
			}
			else break;
		}
		
		if (count == cha.length - 1) return true;
		else return false;
	}
	
	
	public void printPaths(){
		
		System.out.println("The folder you have chosen is: " + path);
		System.out.println("There are " + childPath.length + " child folders:");
		for (int i = 0; i < childPath.length; i ++){
			System.out.println("Folder name: " + fdName[i]);
			System.out.println("Path: " + childPath[i]);
		}
		
		
	}
	
}  