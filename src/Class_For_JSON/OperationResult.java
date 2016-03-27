package Class_For_JSON;

import java.util.ArrayList;

import SudokuSim.Cell;

public class OperationResult {
	ArrayList<Cell> Sudoku = new ArrayList<Cell>();
	
	public OperationResult(ArrayList<Cell> S){
		this.Sudoku = S;
	}
	
	public OperationResult() {
		
	}
	
	public ArrayList<Cell> getValue(){
		return this.Sudoku;
	}
}