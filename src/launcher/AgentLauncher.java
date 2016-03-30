package launcher;
import java.util.ArrayList;

import SudokuSim.SudokuManager;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
public class AgentLauncher {


	public static void main(String[] args) {


		Runtime rt = Runtime.instance();
		ProfileImpl p = null;
		try {
			p = new ProfileImpl("./Properties/SecondaryProperties.txt");
			ContainerController cc = rt.createAgentContainer(p);
			
			SudokuManager Manager = new SudokuManager();
			ArrayList<Integer> sudoku = Manager.ReadSudoku("src/sudoku1.txt");

			Object args1[] = new Object[1];
			args1[0] = sudoku;
		
			Integer i = 0;
			while(i<27){
				(cc.createNewAgent("Analyse"+i,"agents.AnalysisAgent", null)).start();
				i+=1;
			}
			
			AgentController ac = cc.createNewAgent("Simu","agents.SimulationAgent", args1);
			ac.start();
			System.out.println(ac.getState());


			AgentController ac1 = cc.createNewAgent("Envi",
					"agents.EnvironmentalAgent", args1);
			ac1.start();

		}
		catch (Exception ex) {
			ex.printStackTrace();
		}


	}


}

