package agents;


import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import Class_For_JSON.OperationResult;
import SudokuSim.Cell;
import SudokuSim.SudokuManager;

import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
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
		addBehaviour(new WaitBehaviour());

	}

	class WaitBehaviour extends CyclicBehaviour{

		@Override
		public void action() {
			// On attend la réception de message de type REQUEST
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = myAgent.receive(mt);
			if (message != null){
				// A la reception, on lance un OneShotBehaviour qui s'occupe de traiter le message et de répondre
				myAgent.addBehaviour(new MajBehaviour(message));
			}else{
				block();
			}
		}
	}

	class MajBehaviour extends OneShotBehaviour{
		private String mess;
		private ACLMessage message;
		private ArrayList<Cell> SudokuSimul;

		// Constructor
		public MajBehaviour(ACLMessage message2) {
			this.message = message2;
			this.mess = message.getContent();
			this.SudokuSimul =  new ArrayList<Cell>();
		}

		// Task to do
		public void action() {
			// On deserialise le message
			ObjectMapper mapper = new ObjectMapper();
			try {
				OperationResult ort = mapper.readValue(mess,OperationResult.class);
				SudokuSimul = ort.getValue();
			}
			catch(Exception ex) {
				System.out.println("EXCEPTION" + ex.getMessage());
			}
			
			// On met a jour le Sudoku.
			Sudoku = SudokuSimul;
			
			// Si le Sudoku est terminé
			if (manager.SudokuIsFinished(Sudoku)){
				// On affiche le résultat
				manager.AfficheSudoku(Sudoku);

			}
		}

	}
}