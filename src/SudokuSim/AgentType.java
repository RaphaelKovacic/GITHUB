package SudokuSim;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.util.Int2D;

public class AgentType implements Steppable {
    public int x, y;
    public static int LEVEL = 2;
	@Override
	public void step(SimState state) {
		
	}
  protected int friendsNum(Beings beings) {
	return friendsNum(beings,x,y);
 }
  protected int friendsNum(Beings beings,int l,int c) {
		int nb = 0;
	    for (int i = -1 ; i <= 1 ; i++) {
	    for (int j = -1 ; j <= 1 ; j++) {
	      if (i != 0 || j != 0) {
	    	  Int2D flocation = new Int2D(beings.yard.stx(l + i),beings.yard.sty(c + j));
	    	  Object ag = beings.yard.get(flocation.x,flocation.y);
	          if (ag != null) {
	        	  if (ag.getClass() == this.getClass())
	        		  nb++;
	          }
	      }
	    }
	  }
	  return nb;
	 }
  
  
}
