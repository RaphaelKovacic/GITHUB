package agents;

import java.io.StringWriter;
import java.util.ArrayList;

import Dependences.AnalyseMessage;
import SudokuSim.Cell;
import SudokuSim.SudokuManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import Class_For_JSON.OperationDemand;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.core.Agent;
public class SimulationAgent extends Agent{
	ArrayList<Integer> SudokuInt = new ArrayList<Integer>();
	ArrayList<Cell> Sudoku = new ArrayList<Cell>();
	Cell[][] Sudoku_v2 = new Cell[9][9];
	SudokuManager manager = new SudokuManager();
	int nb_subs_recu;
	int id_conv;
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
		
//		int k,j;
//		k=j=0;
//		for(int i = 0; i < SudokuInt.size(); i++)
//		{
//			Cell element = new Cell(SudokuInt.get(i));
//			j = i%9;
//			Sudoku_v2[k][j] = element;
//			if (i != 0 && i%9 == 0)
//				k++;
//		}

		nb_subs_recu = 0;
		id_conv = 0;
		
		DFAgentDescription dafd = new DFAgentDescription();
		dafd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Sudoku");
		sd.setName("SimulationAgent");
		dafd.addServices(sd);
		try {
			DFService.register(this, dafd);
		}
		catch (FIPAException fe) {
			System.out.println(fe);
			fe.printStackTrace();
		}


		System.out.println("Agent Simulation. ");

		// Behaviour qui toutes les 10secondes envoies une batterie de messages aux agents analyse
		addBehaviour(new TickerBehaviour(this, 10000){
			protected void onTick() {
				String type;
				String NameOfAna;
				AID receiver;
				int num;
				System.out.println("Agent "+myAgent.getLocalName()+": tick="+getTickCount());
				for (int i = 0 ; i < 27 ; i++){
					NameOfAna = "Analyse"+i;
					receiver = myAgent.getAID(NameOfAna);
					num = i%9;
					if (i <9)
						type = "Ligne";
					else 
						if(i < 18)
							type = "Colonne";
						else
							type = "Carre";
					sendMess_ToAna(myAgent,receiver,type,num);
					id_conv++;
				}
				addBehaviour(new TraiteRepAnalyse());
			}
		});


	}
	
	class TraiteRepAnalyse extends SimpleBehaviour{
		//Propriétés
		private boolean endOf;
		private int nb_rep ;
		private ArrayList<Cell> cells;
		private String type;
		private int num;
		
		//Constructeur
		public TraiteRepAnalyse() {
			this.endOf = false;
			this.nb_rep = 0;
			this.cells =  new ArrayList<Cell>();
			this.type = "";
			this.num = -1;
		}

		// Implementation de la fonction done()
		public boolean done() {
			if (endOf == true)
				return true; 
			else 
				return false;
		}


		@Override
		public void action() {
			// L'agent attend de recevoir des messages de type "INFORM"
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.INFORM);
			ACLMessage message = myAgent.receive(mt);
			String mess = message.getContent();

			if (message != null){
				// On récupère le message et on met à jour notre copie de sudoku.
				
				// On deserialise le message
				ObjectMapper mapper = new ObjectMapper();
				try {
					OperationDemand ort = mapper.readValue(mess, OperationDemand.class);
					cells = ort.getTab();
					type = ort.getType();
					num = ort.getNum();
				}
				catch(Exception ex) {
					System.out.println("EXCEPTION" + ex.getMessage());
				}
				
				// On met a jour notre table;
				manager.modifySudokuFromCells(type, num, Sudoku, cells);


				nb_rep++;
				if (nb_rep == 27)
					endOf = true;
			}else{
				// Sinon on attend l'arrivé d'un message
				block();		
			}
		}
	}

	public void sendMess_ToAna(Agent myAgent,AID receiver,String type,int num){
		ACLMessage message1 = new ACLMessage(ACLMessage.REQUEST);
		message1.addReceiver(receiver);
		message1.setConversationId(String.valueOf(id_conv));

		ObjectMapper mapper1 = new ObjectMapper();
		StringWriter sw = new StringWriter();
		ArrayList<Cell> CellsToSend = new ArrayList<Cell>();
		switch(type){
		case "Ligne": CellsToSend = manager.getligne(num, Sudoku);
			break;
		case "Colonne": CellsToSend = manager.getcolonne(num, Sudoku);
			break;
		case "Carre": CellsToSend = manager.getcarre(num+1, Sudoku);
			break;
		}
		OperationDemand or = new OperationDemand(type,num,CellsToSend);
		try {
			mapper1.writeValue(sw, or);
			String s1 = sw.toString();
			message1.setContent(s1);
			myAgent.send(message1);
			System.out.println("Sim a envoye (a "+ receiver.getLocalName() + ") :" + s1);
		}
		catch(Exception ex) {
			System.out.println("Probleme serialisation lorsque Fact a envoye un mess...");
		}
	}
}