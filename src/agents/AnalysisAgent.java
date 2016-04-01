package agents;

import java.io.StringWriter;
import java.util.ArrayList;

import com.fasterxml.jackson.databind.ObjectMapper;

import Class_For_JSON.OperationDemand;
import SudokuSim.Cell;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.DFService;
import jade.domain.FIPAException;
import jade.domain.FIPAAgentManagement.DFAgentDescription;
import jade.domain.FIPAAgentManagement.ServiceDescription;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class AnalysisAgent extends Agent{
	boolean isOccuped;
	protected void setup() 
	{ 

		DFAgentDescription dafd = new DFAgentDescription();
		dafd.setName(getAID());
		ServiceDescription sd = new ServiceDescription();
		sd.setType("Sudoku");
		sd.setName("Analysis");
		dafd.addServices(sd);
		try {
			DFService.register(this, dafd);
		}
		catch (FIPAException fe) {
			fe.printStackTrace();
		}
		isOccuped = false;

		//		System.out.println("Agent Analysis. ");
		//		System.out.println("My name is "+ this.getLocalName()); 	          
		addBehaviour(new WaitBehaviour());
		addBehaviour(new SubBehaviour());
	}

	class SubBehaviour extends OneShotBehaviour{
		public void action(){
			// Si l'agent n'est pas occupé il faut envoyé un message Subscribe à l'agent de Simu
			if(isOccuped == false){
				ACLMessage message1 = new ACLMessage(ACLMessage.SUBSCRIBE);
				message1.addReceiver(myAgent.getAID("Simu"));
				message1.setContent("Je suis prêt à travailler");
				myAgent.send(message1);
				
				// L'agent passe en occupé;
				isOccuped = true;
			}
		}
	}



	class WaitBehaviour extends CyclicBehaviour{

		@Override
		public void action() {

			// On attend la réception de message de type REQUEST
			MessageTemplate mt = MessageTemplate.MatchPerformative(ACLMessage.REQUEST);
			ACLMessage message = myAgent.receive(mt);
			if (message != null){
				// A la reception, on lance un OneShotBehaviour qui s'occupe de traiter le message et de répondre
				myAgent.addBehaviour(new TraiteCellsBehaviour(message));
			}else{
				block();
			}
		}
	}

	class TraiteCellsBehaviour extends OneShotBehaviour{
		private String mess;
		private ACLMessage message;
		private ArrayList<Cell> cells;
		private String type;
		private int num;

		// Constructor
		public TraiteCellsBehaviour(ACLMessage message2) {
			this.message = message2;
			this.mess = message.getContent();
			this.cells =  new ArrayList<Cell>();
			this.type = "";
			this.num = -1;
		}

		// Task to do
		public void action() {

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
			// On execute les différents algo... (modifie cells en conséquence)
			ArrayList<Integer> LPossiblesTot = new ArrayList<Integer>();
			boolean cells_modif = true;
			while (cells_modif == true){
				cells_modif = false;
				
				// Pour chaque cellules...
				for(int i = 0; i < cells.size(); i++)
				{
					// Si la cellule actuelle n'a qu'une valeur possible (1)
					if (cells.get(i).getLPossibles().size() == 1){
						cells_modif = true;
						// on met a jour la valeur
						cells.get(i).setValue(cells.get(i).getLPossibles().get(0));
						// on vide la liste des possibles
						cells.get(i).getLPossibles().clear();
					} //(1)

					// Si la cellule actuelle a deja une valeur défini (2)
					if (cells.get(i).getValue() != 0){

						//on retire cette valeur des autres listes de possibles.
						for(int j = 0; j < cells.size(); j++){
							if (cells.get(j).getLPossibles().remove(new Integer(cells.get(i).getValue())) == true)
								cells_modif = true;
						}
						// on vide la liste des possibles
						cells.get(i).getLPossibles().clear();
					} //(2)
					
					// Si la cellule actuelle n'a que 2 valeurs possibles (4)
					if (cells.get(i).getLPossibles().size() == 2){
						
						//Pour chaque cellules suivantes.
						for(int j = i+1; j < cells.size(); j++){
							if (cells.get(j).getLPossibles().size() == 2){
								
								// Si elle contient les 2 même valeurs...
								if (cells.get(i).getLPossibles().equals(cells.get(j).getLPossibles()) ){

									//on retire ces 2 valeurs des autres listes de possibles.
									for(int k = 0; k < cells.size(); k++){
										if (k != i && k != j){
											if (cells.get(k).getLPossibles().remove(new Integer(cells.get(i).getLPossibles().get(0))) == true)
												cells_modif = true;	
											if (cells.get(k).getLPossibles().remove(new Integer(cells.get(i).getLPossibles().get(1))) == true)
												cells_modif = true;	
										}

									}
								}
							}
						}
					} //(4)
					
					//On fait ici la liste de toutes les valeurs possibles parmi les 9 cellules
					for(int j = 0; j < cells.get(i).getLPossibles().size(); j++){
						if (!LPossiblesTot.contains(cells.get(i).getLPossibles().get(j)))
							LPossiblesTot.add(cells.get(i).getLPossibles().get(j));
					}
				}

				// Pour chaque valeurs possibles (3)
				for(int i = 0; i < LPossiblesTot.size(); i++){
					int occur_VP = 0;
					int indice_cell = -1;
					// Pour chaque cellules...
					for(int j = 0; j < cells.size(); j++)
					{
						// Si la cellule actuelle contient la valeur possible
						if (cells.get(j).getLPossibles().contains(LPossiblesTot.get(i))){
							occur_VP++;
							indice_cell = j;
						}
					}
					// Si on a trouvée qu'une seule fois la valeur possible parmis toutes les ListePossible
					if (occur_VP == 1){
						// on met a jour la valeur de la cellule contenant une seule fois la valeur possible
						cells.get(indice_cell).setValue(LPossiblesTot.get(i));
						cells_modif = true;
					}

				} //(3)
			}

			// On renvoie un message de type INFORM serialise avec le resultat
			ObjectMapper mapper1 = new ObjectMapper();
			StringWriter sw = new StringWriter();
			OperationDemand or = new OperationDemand(type,num,cells);
			try {
				mapper1.writeValue(sw, or);
				String s1 = sw.toString();

				ACLMessage reply = message.createReply();
				reply.setPerformative(ACLMessage.INFORM);
				reply.setContent(String.valueOf(s1));
				myAgent.send(reply);
			}
			catch(Exception ex) {}
			
			// L'agent a fini son job;
			isOccuped = false;
			addBehaviour(new SubBehaviour());
		}
	}

}