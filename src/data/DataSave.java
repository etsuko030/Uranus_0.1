package data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;

public class DataSave {

	public static void saveTxt(String path, DataSet data) throws IOException{
		File save = createEmptyFile(path, "feature vectors.txt");
		writeToFile(data.points, save);
	}
	
	public static void writeToFile(Vector<DataPoint> data, File save) throws IOException{
	// write data to created file
		
		if (data!=null){
			FileWriter fw = new FileWriter(save.getAbsoluteFile());
			BufferedWriter wr = new BufferedWriter(fw);
			for (int i = 0; i < data.size(); i ++){
				double[] line = data.elementAt(i).value;
				for (int j = 0; j < line.length; j ++){
					wr.write(String.valueOf(line[j]));
					if (j != line.length-1)
						wr.write(", ");
				}
//				wr.write(", "); wr.write(data.elementAt(i).label); wr.write(", ");
				if (i != data.size()-1) wr.write("\r\n");
			}
			wr.write("\r\n");wr.write("\r\n");
			for (int i = 0; i < data.size(); i ++){
				wr.write(data.elementAt(i).label);wr.write("\r\n");
			}
			wr.close();
			System.out.println("Data saved.");
		} else {
			System.err.println("Data not found.");
			System.err.println("Vocabulary is not written to file.");
		}
		
	}
	
	public static File createEmptyFile(String path, String name){

		File save = null;
		
		if(path!=null){
		// create a file to given path
			File f = new File(path);
			boolean flag = false;
		
			try {
				f = new File(path+"\\"+name);
				// tries to create new file in the system
				flag = f.createNewFile();
				if (flag){
					save = f;
				}
//				// deletes file from the system
//				f.delete();
//				System.out.println("delete() method is invoked");
			}
			catch(Exception e){
				e.printStackTrace();
			}
		} else {
			System.err.println("Path is null: File not created.");
		}
		
		return save;
	}
	
}
