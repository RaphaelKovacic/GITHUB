package main;

import java.util.ArrayList;

import SudokuSim.SudokuManager;
import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
/**
 * @author ia04p011
 *
 */
public class MainBoot {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String MAIN_PROPERTIES_FILE = "./Properties/MainProperties.txt";
		Runtime rt = Runtime.instance();
		Profile p = null;
		try{
		p = new ProfileImpl(MAIN_PROPERTIES_FILE);
		AgentContainer mc = rt.createMainContainer(p);
		}
		catch(Exception ex) {
			
			System.out.println("ExceptionMainController");
		}
	}
}
