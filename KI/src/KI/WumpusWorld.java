package KI;
import de.northernstars.jwumpus.core.JWumpus;

public class WumpusWorld {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// Create new instance of JWumpus and pass instance of AI as parameter 
		new JWumpus( new MyOwnAI(),true);
		
	}

}
