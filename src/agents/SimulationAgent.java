package agents;

import java.io.StringWriter;
import java.util.ArrayList;

import SudokuSim.Cell;
import SudokuSim.SudokuManager;

import com.fasterxml.jackson.databind.ObjectMapper;

import Class_For_JSON.OperationDemand;
import Class_For_JSON.OperationResult;
import jade.core.AID;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.core.behaviours.SimpleBehaviour;
import jade.core.behaviours.TickerBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.wrapper.StaleProxyException;
import jade.core.Agent;
public class SimulationAgent extends Agent{
	ArrayList<Integer> SudokuInt = new ArrayList<Integer>();
	ArrayList<Cell> Sudoku = new ArrayList<Cell>();
	Cell[][] Sudoku_v2 = new Cell[9][9];
	SudokuManager manager = new SudokuManager();

	public boolean SudokuFinished;
	int nb_subs_recu;
	int id_conv;
	protected void setup() 
	{
		//On rï¿½cupï¿½re le sudoku du fichier
		SudokuInt = (ArrayList<Integer>) this.getArguments()[0];

		//On le convertit comme une grille de cells (val +listes des possibles)
		for(int i = 0; i < SudokuInt.size(); i++)
		{
			Cell element = new Cell(SudokuInt.get(i));
			Sudoku.add(i, element);
		}

		//manager.AfficheSudoku(Sudoku);

		SudokuFinished = false;
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
		addBehaviour(new TickerBehaviour(this, 5000){
			protected void onTick() {
				if (SudokuFinished == false){

					String type;
					String NameOfAna;
					AID receiver;
					int num;
					System.out.println("Agent "+myAgent.getLocalName()+": tick="+getTickCount());
					System.out.println("nb_subs_recu :"+ nb_subs_recu);
					
					// Si on a recu les 27 subrscribe, on parrallï¿½lise les traitements.
					if (nb_subs_recu == 27){
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
							nb_subs_recu --;
							sendMess_ToAna(myAgent,receiver,type,num);
							id_conv++;
						}
						addBehaviour(new TraiteRepAnalyse());
					}
					// Sinon on demande au DF de distribuer les messsages alï¿½atoirement à des agents Analyse...
					else{
						for (int i = 0 ; i < 27 ; i++){
							receiver = getAnaReceiver();
							num = i%9;
							if (i <9)
								type = "Ligne";
							else 
								if(i < 18)
									type = "Colonne";
								else
									type = "Carre";
							if (receiver != null) {
								sendMess_ToAna(myAgent,receiver,type,num);
							}else{
								System.out.println("Aucun agent mult trouve...");
							}
							id_conv++;
						}
						addBehaviour(new TraiteRepAnalyse());
						

					}
				// Sinon si le Sudoku est terminé on Kill la simulation.
				}else{
					doDelete();
				}
			}
		});
		//Behaviour qui s'occupe de rï¿½ceptionner les 'Subscribe' des agents d'analyses.
		addBehaviour(new WaitBehaviour());

		//Behaviour qui s'occupe de rï¿½ceptionner les 'Inform' de l'agent d Environnemnt.
		addBehaviour(new WaitEnvBehaviour());


	} // fin Setup
	
	// Fonction permettant de trouver un agent mult (choisit aléatoirement)
	public AID getAnaReceiver() {
		AID rec = null;
		DFAgentDescription template = new DFAgentDescription();
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Sudoku");
		sd.setName("Analysis");
		template.addServices(sd);
		try {
			DFAgentDescription[] result = DFService.search(this, template);
			if (result.length > 0){
				int i;
				i = (int)(Math.random() * (result.length));
				rec = result[i].getName();
			}
		} catch(FIPAException fe) {

		}
		return rec;
	}

	class WaitBehaviour extends CyclicBehaviour{

		@Override
		public void action() {
			// On attend la rï¿½ception de message de type Subscribe
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.SUBSCRIBE);
			ACLMessage message = myAgent.receive(mt);
			if (message != null){
				nb_subs_recu++;
			}else{
				block();
			}
		}
	}

	class WaitEnvBehaviour extends CyclicBehaviour{

		@Override
		public void action() {
			// On attend la rï¿½ception de message de type Inform
			MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM),
					MessageTemplate.MatchSender(myAgent.getAID("Envi")));
			ACLMessage message = myAgent.receive(mt);
			if (message != null){
				SudokuFinished = true;
			}else{
				block();
			}
		}
	}

	class TraiteRepAnalyse extends SimpleBehaviour{
		//Propriï¿½tï¿½s
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

			if (message != null){
				// On rï¿½cupï¿½re le message et on met ï¿½ jour notre copie de sudoku.
				String mess = message.getContent();
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

				// On met a jour notre table "copy";
				manager.modifySudokuFromCells(type, num, Sudoku, cells);


				nb_rep++;
				//System.out.println("nb_rep :"+ nb_rep);
				// Si jamais on a reï¿½u les 27 messages :
				if (nb_rep == 27){
					nb_rep = 0;
					endOf = true;
					// On envoie notre Sudoku "Copy" ï¿½ Env pour qu'il fasse la Maj du "Vrai" Sudoku.
					myAgent.addBehaviour(new TraiteMajBehaviour());
				}

			}else{
				// Sinon on attend l'arrivï¿½ d'un message
				block();		
			}
		}
	}

	class TraiteMajBehaviour extends OneShotBehaviour{

		//Constructeur
		public TraiteMajBehaviour() {

		}

		public void action() {
			// envoie du message de Maj.
			ACLMessage message1 = new ACLMessage(ACLMessage.REQUEST);
			message1.addReceiver(myAgent.getAID("Envi"));

			ObjectMapper mapper1 = new ObjectMapper();
			StringWriter sw = new StringWriter();

			OperationResult or = new OperationResult(Sudoku);
			try {
				mapper1.writeValue(sw, or);
				String s1 = sw.toString();
				message1.setContent(s1);
				myAgent.send(message1);
			}
			catch(Exception ex) {
				System.out.println(ex.getMessage());
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
		case "Carre": CellsToSend = manager.getcarre(num, Sudoku);
		break;
		}
		OperationDemand or = new OperationDemand(type,num,CellsToSend);
		try {
			mapper1.writeValue(sw, or);
			String s1 = sw.toString();
			message1.setContent(s1);
			myAgent.send(message1);
			//System.out.println("Sim a envoye (a "+ receiver.getLocalName() + ") :" + s1);
		}
		catch(Exception ex) {
			System.out.println(ex.getMessage());
		}
	}
}