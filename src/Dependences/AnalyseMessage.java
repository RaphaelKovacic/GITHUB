package Dependences;

import java.util.ArrayList;

public class AnalyseMessage {
	String objet;
	ArrayList<Integer> []values;
	
	public AnalyseMessage(ArrayList<Integer> []values, String objet) {
		// TODO Auto-generated constructor stub
		this.objet = objet;
		this.values = values;
		
	}

	public ArrayList<Integer> []getValue(){
		return this.values;
	}
	
}

