package KI;
public class Tile {
	
	int row;
	int column;
	
	
	//booleans geben an, ob das oberen/untere... Feld bereits geprüft ist
	boolean upChecked = false;
	boolean downChecked = false;
	boolean leftChecked = false;
	boolean rightChecked = false;
	
	boolean trap = false; // true = Falle sicher auf dem Feld. false = keine Falle, oder noch nicht geprüft
	boolean wumpus = false; // true = Wumpus sicher auf dem Feld. false = kein Wumpus, oder noch nicht geprüft
	boolean trapORwumpus = false; // true = auf diesem Feld befindet sich ein Wumpus oder eine Falle, kann derzeit nicht sicher bestimmt werden. Wird von Regel3 genutzt. Mit fortschreitender Wissensbasis wird es -sofern möglich- eindeutig ermittelt.
	
	Tile(int y, int x) {
		
		this.row = y;
		this.column = x;
	}
	
	
	/**
	 * Kopierkonstruktor. Wird verwendet um Kopien von Listen anzulegen, ohne dabei die selben Referenzen auf die einzelnen Felder zu benutzen.
	 * @param originalTile zu kopierendes Feld
	 */
	Tile(Tile originalTile) {

		this(originalTile.row, originalTile.column);
	}
	
	
	Tile(int y, int x, boolean trapBool, boolean wumpusBool, boolean trapORwumpusBool) {
		
		this.row = y;
		this.column = x;
		this.trap = trapBool;
		this.wumpus = wumpusBool;
		this.trapORwumpus = trapORwumpusBool;

		
	}
	
	/**
	 * Prüft, ob es mindestens ein noch unbesuchtes Nachbarfeld gibt.
	 * @return true, wenn es ein unbesuchtes Nachbarfeld gibt.
	 */
	boolean checkSurroundings() {
		if((upChecked == false) | (downChecked == false) | (leftChecked == false) | (rightChecked == false))
			return true;
		else return false;
	}
	
}
