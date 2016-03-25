package agents;

import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import SudokuSim.Cell;
import SudokuSim.SudokuManager;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.Agent;

public class EnvironmentalAgent extends Agent{
	ArrayList<Integer> SudokuInt = new ArrayList<Integer>();
	ArrayList<Cell> Sudoku = new ArrayList<Cell>();
	SudokuManager manager = new SudokuManager();
	protected void setup() 
	{ 
		//On récupère le sudoku du fichier
		System.out.println("Agent Simulation. ");
		SudokuInt = (ArrayList<Integer>) this.getArguments()[0];
		
		//On le convertit comme une grille de cells (val +listes des possibles)
	    for(int i = 0; i < SudokuInt.size(); i++)
	    {
	    	Cell element = new Cell(SudokuInt.get(i));
	    	Sudoku.add(i, element);
	    }
	    
		DFAgentDescription dafd = new DFAgentDescription();
		dafd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Sudoku");
		sd.setName("EnvironmentalAgent");
		dafd.addServices(sd);
		try {
			DFService.register(this, dafd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}


		System.out.println("Agent Environmental. ");
		System.out.println("My name is "+ this.getLocalName()); 	          
		//addBehaviour(new EBehaviour());

	}


}

class EBehaviour extends CyclicBehaviour{

	jade.core.Agent thisagent;

	public EBehaviour() {
		// TODO Auto-generated constructor stub

	}

	@Override
	public void action() {
		
		
		
		MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
		// TODO Auto-generated method stub
		ACLMessage message = thisagent.receive(mt);
		System.out.println("Message recu. ");

		
		
		
		if (message != null){

			
			
			
			
		}else
			block();
	}



}