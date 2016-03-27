package SudokuSim;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class SudokuManager {

	@SuppressWarnings("resource")
	public ArrayList<Integer> ReadSudoku (String source) throws FileNotFoundException{
		ArrayList<Integer> TabtoReturn = new ArrayList<Integer>();

		String sCurrentLine;
		BufferedReader br;
		try {

			br = new BufferedReader(new FileReader(source));

			while ((sCurrentLine = br.readLine()) != null) {
				Integer i = 0;
				String [] Tableau = sCurrentLine.split(" ");
				while(i < 9){
					TabtoReturn.add(Integer.parseInt(Tableau[i]));
					i+=1;
				}
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Entree reader"+ e);
		}
		return TabtoReturn;
	}


	public ArrayList<Cell> getligne(Integer i, ArrayList<Cell> Sudoku){
		ArrayList<Cell> resultat = new ArrayList<Cell>();
		Integer j=0;
		while (j<9){
			resultat.add(Sudoku.get(i*9+j));
			j+=1;
		}

		return resultat;
	}

	public ArrayList<Cell> getcolonne(Integer i, ArrayList<Cell> Sudoku){
		ArrayList<Cell> resultat = new ArrayList<Cell>();
		Integer j=0;
		while (j<9){
			resultat.add(Sudoku.get(j*9+i)); 
			j+=1;
		}

		return resultat;
	}
	public ArrayList<Cell> getcarre_s(Integer xd, Integer yd, ArrayList<Cell> Sudoku){
		int x = xd;
		int y = yd;
		ArrayList<Cell> resultat = new ArrayList<Cell>();
		
		while (x<(xd+3)){
			y = yd;
			while (y<(yd+3)){
				resultat.add(Sudoku.get(x*9+y));
				y+=1;
			}
			x+=1;
		}
		return resultat;
	}
	
	public ArrayList<Cell> getcarre(Integer i, ArrayList<Cell> Sudoku){
		ArrayList<Cell> resultat = new ArrayList<Cell>();
		int xd,yd;
		
		switch (i)
		{
		case 1:
			xd=yd=0;
			resultat = getcarre_s(xd,yd,Sudoku);break;
		case 2:
			xd=0;yd=3;
			resultat = getcarre_s(xd,yd,Sudoku); break;
		case 3:
			xd=0;yd=6;
			resultat = getcarre_s(xd,yd,Sudoku);break;        
		case 4:
			xd=3;yd=0;
			resultat = getcarre_s(xd,yd,Sudoku);break;
		case 5:
			xd=3;yd=3;
			resultat = getcarre_s(xd,yd,Sudoku);break;      
		case 6:
			xd=3;yd=6;
			resultat = getcarre_s(xd,yd,Sudoku); break;
		case 7:
			xd=6;yd=0;
			resultat = getcarre_s(xd,yd,Sudoku);break;        
		case 8:
			xd=6;yd=3;
			resultat = getcarre_s(xd,yd,Sudoku);break;
		case 9:
			xd=6;yd=6;
			resultat = getcarre_s(xd,yd,Sudoku);break;
		}
		
		return resultat;
	}
	
	public ArrayList<Cell> modifySudokuFromCells(String type, int num, ArrayList<Cell> Sudoku, ArrayList<Cell> Cells){
//		switch(type){
//		case "Ligne": Sudoku = setligne(num, Sudoku, Cells);
//			break;
//		case "Colonne": Sudoku = setcolonne(num, Sudoku, Cells);
//			break;
//		case "Carre": Sudoku = setcarre(num, Sudoku, Cells);
//			break;
//		}
		return Sudoku;
		
	}
	
	public boolean SudokuIsFinished(ArrayList<Cell> Sudoku){
		// A FAIRE 
		return true;
	}
	public void AfficheSudoku(ArrayList<Cell> Sudoku){
		// A FAIRE 
	}
}


