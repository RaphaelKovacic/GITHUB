package SudokuSim;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import sim.engine.SimState;
import sim.engine.Stoppable;
import sim.field.grid.IntGrid2D;
import sim.field.grid.ObjectGrid2D;
import sim.field.grid.SparseGrid2D;
import sim.util.Int2D;
import sim.util.TableLoader;
public class Beings extends SimState {
	public static int GRID_SIZE = 9; 
	public static int NUM_A = 4; 
	public static int NUM_B = 4; 
	public static int NB_DIRECTIONS = 8;
	public IntGrid2D yard = new IntGrid2D(GRID_SIZE,GRID_SIZE);
	public Beings(long seed) {
		super(seed);
	}
	public void start() {
		System.out.println("Simulation started");
		super.start();
		InputStream inputstream;
		try {
			inputstream = new FileInputStream("src/sudoku4.txt");
			double[][] tab =  sim.util.TableLoader.loadTextFile(inputstream);
			int [][] tab1 = sim.util.TableLoader.convertToIntArray(tab);
		    yard.setTo(tab1);

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//	    addAgentsA();
//	    addAgentsB();
  }
}