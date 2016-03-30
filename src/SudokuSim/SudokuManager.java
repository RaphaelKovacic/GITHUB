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
		System.out.println("ARRRIVEE AU MODIFY SUDOKU FROM CELLS ---- TEST");

		switch(type){
		case "Ligne": setLigne(num, Sudoku, Cells);
		break;
		case "Colonne": setColonne(num, Sudoku, Cells);
		break;
		case "Carre": setCarre(num, Sudoku, Cells);
		break;
		}
		return Sudoku;

	}

	public void setLigne(int num, ArrayList<Cell> Sudoku,ArrayList<Cell> Cells){
		ArrayList<Cell> Temp = new ArrayList<Cell>();
		Temp = getligne(num,Sudoku);
		Integer i = 0;
		Cell TempC = new Cell();
		Cell TempT = new Cell();

		while (i<9){

			TempT = Temp.get(i);
			TempC = Cells.get(i);
			if (TempT.getValue() == 0){

				
				if (TempC.getValue() != 0){

					//La valeur a été changée, on la remplace directement

					TempT.setValue(TempC.getValue());

				}else{

					//On compare maintenant les listes de possibles

					ArrayList<Integer> NouvelleListePossible = compCell(TempT,TempC);
					

					if(!(NouvelleListePossible == null)){

						if(NouvelleListePossible.size() == 1 ){

							// Plus qu'un élément, on peut mettre une valeur ?
							setCellValue(Sudoku,num*9+i,NouvelleListePossible.get(0));

							setCellListePossible(Sudoku,num*9+i,NouvelleListePossible);


						}else {

							// On change la liste de possibles
							setCellListePossible(Sudoku,num*9+i,NouvelleListePossible);


						}

					}


				}
			}else{

				//TODO La valeur a déjà été changée auparavant, est ce qu'on fait quelque chose ?

			}
			i+=1;
		}

	}

	public void setColonne(int num, ArrayList<Cell> Sudoku,ArrayList<Cell> Cells){
		ArrayList<Cell> Temp = new ArrayList<Cell>();
		Temp = getligne(num,Sudoku);
		Integer i = 0;
		Cell TempC = new Cell();
		Cell TempT = new Cell();
		while (i<9){
			TempT = Temp.get(i);
			TempC = Cells.get(i);
			if (TempT.getValue() == 0){
				if (TempC.getValue() != 0){

					//La valeur a été changée, on la remplace directement

					TempT.setValue(TempC.getValue());

				}else{

					//On compare maintenant les listes de possibles

					ArrayList<Integer> NouvelleListePossible = compCell(TempT,TempC);
					if(!(NouvelleListePossible == null)){
						if(NouvelleListePossible.size() == 1 ){
							// Plus qu'un élément, on peut mettre une valeur ?
							setCellValue(Sudoku,i*9+num,NouvelleListePossible.get(0));
							setCellListePossible(Sudoku,i*9+num,NouvelleListePossible);

						}else {
							// On change la liste de possibles
							setCellListePossible(Sudoku,i*9+num,NouvelleListePossible);


						}

					}


				}
			}else{

				//TODO La valeur a déjà été changée auparavant, est ce qu'on fait quelque chose ?

			}
			i+=1;
		}

	}


	public void setCarre(int num, ArrayList<Cell> Sudoku,ArrayList<Cell> Cells){
		ArrayList<Cell> Temp = new ArrayList<Cell>();
		Temp = getligne(num,Sudoku);
		Integer i = 0;
		Cell TempC = new Cell();
		Cell TempT = new Cell();
		while (i<9){
			TempT = Temp.get(i);
			TempC = Cells.get(i);
			if (TempT.getValue() == 0){
				if (TempC.getValue() != 0){

					//La valeur a été changée, on la remplace directement

					TempT.setValue(TempC.getValue());

				}else{

					//On compare maintenant les listes de possibles

					ArrayList<Integer> NouvelleListePossible = compCell(TempT,TempC);
					if(!(NouvelleListePossible == null)){
						if(NouvelleListePossible.size() == 1 ){
							// Plus qu'un élément, on peut mettre une valeur ?
							setCellValue(Sudoku,i*9+num,NouvelleListePossible.get(0));
							setCellListePossible(Sudoku,i*9+num,NouvelleListePossible);

						}else {
							// On change la liste de possibles
							setCellListePossible(Sudoku,i*9+num,NouvelleListePossible);


						}

					}


				}
			}else{

				//TODO La valeur a déjà été changée auparavant, est ce qu'on fait quelque chose ?

			}
			i+=1;
		}

	}

	public void setCellValue(ArrayList<Cell> Sudoku,Integer num, Integer value){
		System.out.println("setCellValue" + num);

		Sudoku.get(num).setValue(value);

	}


	public void setCellValueCarre(ArrayList<Cell> Sudoku,Integer num,Integer i, Integer value){
		
		Integer xd = 0;
		Integer x = 0;
		Integer y = 0;
		Integer yd= 0;



		switch (num)
		{
		case 1:
			xd=x=y=yd=0;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		case 2:
			xd=x=0;yd=y=3;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		case 3:
			xd=x=0;yd=y=6;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;        
		case 4:
			xd=x=3;yd=y=0;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		case 5:
			xd=x=3;yd=y=3;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;      
		case 6:
			xd=x=3;yd=y=6;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		case 7:
			xd=x=6;yd=y=0;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;        
		case 8:
			xd=x=6;yd=y=3;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i--;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		case 9:
			xd=x=6;yd=y=6;
			while (x<(xd+3)){
				y = yd;
				while (y<(yd+3)){
					i-= 1;
					if (i == 0) {

						setCellValue(Sudoku,x*9+y, value);

					}
					y+=1;
				}
				x+=1;
			};
			break;
		}		
	}


	public void setCellListePossible(ArrayList<Cell> Sudoku,Integer num, ArrayList<Integer> value){
		System.out.println("setCellListePossible" + num);

		Sudoku.get(num).setLPossibles(value);

	}


	public ArrayList<Integer> compCell(Cell Cell1, Cell Cell2){

		ArrayList<Integer> LPossiblesResultat = new ArrayList<Integer>();
		ArrayList<Integer> L1Possibles = Cell1.getLPossibles();
		ArrayList<Integer> L2Possibles = Cell2.getLPossibles();
		Integer i = 0;

		if (L1Possibles.size() >= L2Possibles.size()){


			while(i < L1Possibles.size()){

				if (L2Possibles.contains(L1Possibles.get(i))){

					LPossiblesResultat.add(L1Possibles.get(i));

				}

				i+=1;
			}

		}
		if (!LPossiblesResultat.isEmpty()){

			return LPossiblesResultat;
		}
		else {

			return null;
		}
	}

	public boolean SudokuIsFinished(ArrayList<Cell> Sudoku){
		// A FAIRE 
		return true;
	}
	public void AfficheSudoku(ArrayList<Cell> Sudoku){

		Integer i=0;

		while (i< Sudoku.size()){
			if(i%9 == 0){
				System.out.println("\n");
			}

			System.out.print(Sudoku.get(i).getValue() + "|");
			i+=1;
		}
		System.out.println("\n");

	}
}


