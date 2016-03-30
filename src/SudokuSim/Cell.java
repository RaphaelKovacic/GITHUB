package SudokuSim;

import java.util.ArrayList;

public class Cell {
	private int value;
	private ArrayList<Integer> l_possible = new ArrayList<Integer>();

	public Cell(int valeur){
		this.value = valeur;
		if (value == 0){
			this.l_possible.add(1);
			this.l_possible.add(2);
			this.l_possible.add(3);
			this.l_possible.add(4);
			this.l_possible.add(5);
			this.l_possible.add(6);
			this.l_possible.add(7);
			this.l_possible.add(8);
			this.l_possible.add(9);
		}
	}
	public Cell(){

	}

	public int getValue(){
		return this.value;
	}

	public ArrayList<Integer> getLPossibles(){
		return this.l_possible;
	}

	public void print(){
		System.out.println("{"+this.value+"} : ("+this.l_possible.toString()+")");
	}
	public void setValue(int val){
		this.value = val;
	}
	public void setLPossibles(ArrayList<Integer> LP){

		this.l_possible.clear();

		Integer i = 0;

		while(i < LP.size()){

			this.l_possible.add(LP.get(i));
			i+=1;
		}
	}
}
