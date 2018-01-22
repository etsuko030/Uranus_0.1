package uranusworm.ft;

import java.util.Vector;

import org.opencv.core.Mat;
import org.opencv.core.Rect;

import uranusworm.util.Configs;

public class Cell {
	// Separated cell from image
	// record of position and coordinates of the cell (patch of image)
	
	int positionRow, positionCol;
	int sizeX, sizeY, paceX, paceY;
	int cellRows, cellCols;	// number of row and column of cells in total of the correspondent image
	
	// correspondent to OpenCV Rect(x, y, width, height)
	// (x, y) is the coordinates of the left-top point
	public int x, y, width, height;

	private Cell(){
		
	}
	
	protected Cell(Uwimg img, int row, int col){
		
//		assert row>=0 && row < img.cellRows: "0 <= row < total not satisfied. row = "+row+", total rows = "+img.cellRows;
//		assert col>=0 && col < img.cellCols: "0 <= col < total not satisfied. col = "+col+", total cols = "+img.cellCols;
		
		positionRow = row;
		positionCol = col;
		sizeX = Configs.CELLSIZEX;
		sizeY = Configs.CELLSIZEY;
		paceX = Configs.MOVINGPACEX;
		paceY = Configs.MOVINGPACEY;

		generateCellCoordinates();
		
	}
	
	private void generateCellCoordinates(){
		
		width = sizeX;
		height = sizeY;
		x = positionCol*paceX;
		y = positionRow*paceY;
		
	}
	
	protected static Vector<Cell[]> divideImg(Uwimg m){
		
		int sx = Configs.CELLSIZEX;
		int sy = Configs.CELLSIZEX;
		int px = Configs.MOVINGPACEX;
		int py = Configs.MOVINGPACEY;
		int h = m.img.rows();
		int w = m.img.cols();
		
		assert sx>0 && sx <= (m.img.cols()/2): "cell size x = "+sx;
		assert sy>0 && sy <= (m.img.rows()/2): "cell size y = "+sy;
		assert px>0 && px <= sx: "moving pace x = "+px;
		assert py>0 && py <= sy: "moving pace y = "+py;
		
		// number of cells on x-y directions
		int cx, cy;
		cx = (w-sx)/px +1;	// columns
		cy = (h-sy)/py +1;	// rows


		System.out.println("image size = " + m.img.rows()+ "x"+m.img.cols());
		System.out.println("cell cols x rows = "+cx +"x"+cy);
//		assert cy>0;
//		assert cx>0;
		
		Vector<Cell[]> patches = new Vector<Cell[]>();
		
		for (int y = 0; y < cy; y ++){
			Cell[] pRow = new Cell[cx];
			for (int x = 0; x < cx; x ++){
				pRow[x] = new Cell(m, y, x);
			}
			patches.addElement(pRow);
		}
		assert patches!=null;
		assert cy == patches.size();
		assert cx == patches.elementAt(0).length;
		
		return patches;
	}
	
	public Mat getPatchFromOriginal(Mat original){
		
        Rect patch = new Rect (x, y, width, height);
        
        return new Mat (original, patch);
	}
	
	public static Vector<Vector<Mat>> getPatches(Uwimg m){
		
		Vector<Vector<Mat>> cell = new Vector<Vector<Mat>>();
		
		for(int i = 0; i < m.cellRows; i ++){
			Vector<Mat> temp = new Vector<Mat>();
			for (int j = 0; j < m.cellCols; j ++){
				temp.addElement(m.patches.elementAt(i)[j].getPatchFromOriginal(m.img));
			}
			cell.addElement(temp);
		}
		
		return cell;
	}
	
	public static Vector<Vector<Mat>> getMaskFilteredPatches(Uwimg m){
		
		Vector<Vector<Mat>> cell = new Vector<Vector<Mat>>();
		
		for(int i = 0; i < m.cellRows; i ++){
			Vector<Mat> temp = new Vector<Mat>();
			for (int j = 0; j < m.cellCols; j ++){ // revise
				if (isCellInMask(m.patches.elementAt(i)[j], m))
					temp.addElement(m.patches.elementAt(i)[j].getPatchFromOriginal(m.img));
			}
			cell.addElement(temp);
		}
		
		return cell;
	}
	
	public static Mat getCellImg(Cell c, Uwimg m){
		
		Cell temp = m.patches.elementAt(c.positionRow)[c.positionCol];
		
		return temp.getPatchFromOriginal(m.img);
	}
	
	public static Mat getCellMaskImg(Cell c, Uwimg m){
		
		Cell temp = m.patches.elementAt(c.positionRow)[c.positionCol];
		
		return temp.getPatchFromOriginal(m.imgmask);
	}
	
	public static boolean isCellInMask(Cell c, Uwimg m){
		
		Mat patchMask = getCellMaskImg(c, m);
		assert patchMask.type() == m.imgmask.type();
		
		boolean flag = false;
		for (int i = 0; i  <patchMask.rows(); i++)
			for (int j = 0; j < patchMask.rows(); j++){
				double[] value = patchMask.get(i, j);
//				System.out.println("value="+value[0]);
				if (value[0]>128) {
//				if (value[0]==255) {	// ?!
					flag = true;
					break;
				}
			}
		
		return flag;
	}
	
}
