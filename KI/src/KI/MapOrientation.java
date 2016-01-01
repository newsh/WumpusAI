package KI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;

import de.northernstars.jwumpus.core.*;

/* Diese Klasse hält Informationen darüber, wo sich der Agent derzeit befindet und welche Felder bereits aufgedeckt sind */

public class MapOrientation {

	MapOrientation() {
		lastVisitedTiles.add(0, new Tile(0,0)); // Nicht schön, aber keine Ahnung wie man ansonsten ein ListArray initialisieren kann.
		discoveredTiles.add(0, new Tile(0,0));
	}
	
	// Die Initialisierung der Grenzen. "1000" entspricht "Grenze noch nicht gesetzt". Nach der Annahme, dass die Karte nicht 1000x1000 groß sein kann.
	int yUpperBound = 1000; // Obere Grenze der Map
	int yLowerBound = 1000; // Untere Grenze der Map
	int xLeftBound = 1000; // Linke Grenze der Map
	int xRightBound = 1000; // Rechte Grenze der Map

	int currentPosX = 0; // aktuelle x-Koordinate des Agenten
	int currentPosY = 0; // aktuelle y-Koordinate des Agenten
	
	static List<Tile> lastVisitedTiles = new ArrayList<Tile>(); // Enthält alle bisher aufgedeckten Felder. Kann sowohl vom Agenten selbst betretene Felder enthalten, als auch Fallen und Wumpi, die aus der Wissenesbasis und Regeln heraus ermittelt worden sind.
	static List<Tile> discoveredTiles = new ArrayList<Tile>(); // Enthält die Koordinaten der besuchten Felder. Entspricht dem vom Agenten zugelegten Weg.
	static List<Action> stepsToExcecute = new ArrayList<Action>(); // Vom Agenten als nächstes auszuführenden Schritte.
	List<Tile> potentiallySafeTiles = new ArrayList(); // Liste aller Felder, die mit einer Wahrscheinlichkeit von weniger als 100% Fallen enthalten.
	boolean backtrack = false; //Hilfsvariable. Gibt an, ob sich der Agent derzeit auf dem Rückweg aus einer Sackgasse befindet.
	/**
	 * 
	 * Legt die neuen Koordinaten des Agenten nach Ausführung der letzten Aktion
	 * fest.
	 * 
	 * @param action
	 *            Die auszuführende Aktion

	 * @return void
	 */
	void setPosition(Action action) {

		switch (action) {

		case MOVE_UP:
			currentPosY++;
			break;
		case MOVE_DOWN:
			currentPosY--;
			break;
		case MOVE_LEFT:
			currentPosX--;
			break;
		case MOVE_RIGHT:
			currentPosX++;
			break;

		}
	}
	

	/**
	 * Fügt das zuletzt besuchte Feld in eine Liste ein. Befindet sich der Agent auf dem Rückweg aus einer Sackgasse, werden die Felder nicht hinzugefügt.
	 * @param action
	 * @param actionSuccess
	 */
	void addLastVisitedTile(Action action) {
		
		if(backtrack == false) {
			stepsToExcecute.add(action); 
		}
		
		lastVisitedTiles.add(new Tile(currentPosY,currentPosX));
		
	}
	/**
	 * 
	 * Fügt das angegebene Feld in die Liste der bekannten Felder hinzu. Wenn das Feld bereits bekannt ist, wird es nicht hinzugefügt.
	 * @param row Y-Koordinate des neuen Feldes.
	 * @param column X-Koordinate des neuen Feldes.
	 */	
	void addDiscoveredTile(int row, int col) {
		
		boolean coordDuplicate = false;

		ListIterator<Tile> lIter = discoveredTiles.listIterator();
        while(lIter.hasNext()) {
            Tile current = lIter.next();
			if(current.column == col && current.row == row) 
				coordDuplicate =  true;
		}
		if (coordDuplicate == false) { //Feld hinzufügen, wenn noch nicht vorhanden.
			lIter.add(new Tile(row,col)); 
			checkAddedTileForBoundary(discoveredTiles.get(discoveredTiles.size()-1)); //Zuletzt hinzugefügtes Feld auf Grenzen prüfen.
		}

	}
	
	
	/**
	 * Löscht das angegeben Feld aus der Liste der bekannten Felder. 
	 * Dabei werden die Umgebungsavariablen der benachbarten Felder (falls vorhanden) entsprechend auf false gesetzt.
	 * @param row Y-Koordinate des zu löschenden Feldes.
	 * @param col X-Koordinate des zu löschenden Feldes.
	 */
	
	void deleteDiscoveredTile(int row, int col) {
		
		ListIterator<Tile> lIter = discoveredTiles.listIterator();
        while(lIter.hasNext()) {
            Tile current = lIter.next();
			if(current.column == col && current.row == row) {
				//oben benachbartes Feld vorhanden?Falls ja: Umgebungsvariable downChecked = false
				getDiscoveredTile(row+1,col).downChecked = false;
				//rechts benachbartes Feld vorhanden?Falls ja: leftChecked = false
				getDiscoveredTile(row,col+1).leftChecked = false;
				//links benachbartes Feld vorhanden?Falls ja: rightChecked = false
				getDiscoveredTile(row,col-1).rightChecked = false;
				//unten benachbartes Feld vorhanden?Falls ja: upChecked = false
				getDiscoveredTile(row-1,col).upChecked = false;
				//hier kann jetzt das Feld selbst gelöscht werden.
				lIter.remove();
			}
		}

	}
	
	/**
	 * Dursucht die Liste aller bekannten Felder nach einem bestimmten Feld.
	 * @param row Y-Koordinate des zu suchenden Feldes
	 * @param column X-Koordinate des zu suchenden Feldes
	 * @return true, wenn Element vorhanden.
	 */
	boolean searchTile(int row, int column) {
		
		ListIterator<Tile> lIter6 = discoveredTiles.listIterator();
		while(lIter6.hasNext()) {
		    Tile current = lIter6.next();	
		    if(current.row == row && current.column == column)
		    	return true;
		    
		}
	
		return false;
		
	}
	
	
	/**
	 * Gibt das Feld mit den angegebenen Koordinaten aus der Liste der aufgedeckten Felder zurück.
	 * @param row Y-Koordinate des Feldes
	 * @param column X-Koordinate des Feldes
	 * @return Gefundene Feld
	 * 
	 */
	Tile getDiscoveredTile(int row, int column) {   
		
		
		ListIterator<Tile> lIter = discoveredTiles.listIterator();
	    while(lIter.hasNext()) {
	    	Tile current = lIter.next();
	    	if(current.row == row && current.column == column)
	    		return current;
	    }
	    return new Tile(0,0);  // Wenn das Feld nicht dabei war, wird ein Dummy zurückgegeben.
	}
	
	
	
	/**
	 * Aktualisiert die Grenzen der Map und prüft ob bisher aufgedeckte Felder an einer Grenze liegen und aktualisiert diese falls nötig.
	 * @param lastAction Die zuletzt ausgeführte Aktion
	 */
	
	void updateBoundaries(Action lastAction) {
			
	
		switch(lastAction) {
		
		
		
			// Setzen der Grenzen und Korrektur der aktuellen Position.
			case MOVE_UP:
				this.currentPosY--;
				this.yUpperBound= this.currentPosY;
				break;
				
			case MOVE_DOWN:
				this.currentPosY++;
				this.yLowerBound= this.currentPosY;
				break;
				
			case MOVE_LEFT:
				this.currentPosX++;
				this.xLeftBound= this.currentPosX;
				break;
				
			case MOVE_RIGHT:
				this.currentPosX--;
				this.xRightBound= this.currentPosX;
				break;
		
		}
	
		
		ListIterator<Tile> lIter = discoveredTiles.listIterator();
		
		while(lIter.hasNext()) {  // Alle bisher aufgedeckten Felder werden durchlaufen und geprüft, ob sie an einer Wand liegen.
			Tile current = lIter.next();
		
	    	switch(lastAction) {
	    	
		    	case MOVE_UP:  		
					if(current.row == yUpperBound) //Wenn das Feld an der oberen Wand liegt -> upChecked = true setzen.
						current.upChecked = true;
					break;
		    	case MOVE_DOWN:
					if(current.row == yLowerBound) //Wenn das Feld an der unteren Wand liegt -> downChecked = true setzen.
						current.downChecked = true;
					break;
		    	case MOVE_LEFT:
					if(current.column == xLeftBound) //Wenn das Feld an der linken Wand liegt -> leftChecked = true setzen.
						current.leftChecked = true;
		    		break;
		    	case MOVE_RIGHT:
					if(current.column == xRightBound) //Wenn das Feld an der rechten Wand liegt -> rightChecked = true setzen.
						current.rightChecked = true;
					break;
	
	    	}
	    }
		
	}
	
	
	
	
	/**
	 * 
	 * Prüft, ob das zuletzt aufgedeckte Feld an einer Wand liegt. Hierfür muss die Grenze zuvor bereits ermittelt worden sein.
	 * Die Umgebungsvariablen werden aktualisiert, ohne dass der Agent dafür erneut gegen die Wand laufen muss.
	 * @param tile Das zu überprüfende Feld
	 */
	void checkAddedTileForBoundary(Tile tile) { 
	//	System.out.println("checkForBoundary() ausgeführt");
		if(tile.row == yUpperBound){
		//	System.out.println("Obere Grenze automatisch gesetzt für Y: "+ tile.row +" X: " + tile.column);
			tile.upChecked = true;}
		else if(tile.row == yLowerBound){
		//	System.out.println("Untere Grenze automatisch gesetzt für Y: "+ tile.row +" X: " + tile.column);
			tile.downChecked = true;}
		if(tile.column == xRightBound){
		//	System.out.println("Rechte Grenze automatisch gesetzt für Y: "+ tile.row +" X: " + tile.column);
			tile.rightChecked = true;}
		else if(tile.column == xLeftBound) {
		//	System.out.println("Linke Grenze automatisch gesetzt für Y: "+ tile.row +" X: " + tile.column);
			tile.leftChecked =true;}
	}
	
	
	/**
	 * Aktualisiert nach jeder durchgeführten Bewegung die Umgebungsvariablen beider Felder.
	 */
		
	void setTileSurroundings() { 
			
	
			//Für das zuletzt aufgedeckte Feld wird geprüft ob es benachbarte Felder gibt.
			ListIterator<Tile> lIter = discoveredTiles.listIterator();
	        while(lIter.hasNext()) {
	            Tile tile = lIter.next();
	            
				if(tile.row+1 == currentPosY && tile.column == currentPosX) {  // Prüfen, ob das oben anliegende Feld aufgedeckt ist.
					tile.upChecked = true;
		            getDiscoveredTile(currentPosY, currentPosX).downChecked = true;
				}
				if(tile.row-1 == currentPosY && tile.column == currentPosX) { // Prüfen, ob das unten anliegende Feld aufgedeckt ist.
					tile.downChecked = true;
		            getDiscoveredTile(currentPosY, currentPosX).upChecked = true;
				}
				if(tile.row == currentPosY && tile.column-1 == currentPosX) { // Prüfen, ob das links anliegende Feld aufgedeckt ist.
					tile.leftChecked = true;
		            getDiscoveredTile(currentPosY, currentPosX).rightChecked = true;
				}
				
				if(tile.row == currentPosY && tile.column+1 == currentPosX) { // Prüfen, ob das rechts anliegende Feld aufgedeckt ist.
					tile.rightChecked = true;
		            getDiscoveredTile(currentPosY, currentPosX).leftChecked = true;
				}
			}
		}
	
	
	/**
	 * Prüft, ob das Feld frei von Wumpus und Falle ist
	 * @param map
	 * @param row Y-Koordinate des zu untersuchenden Feldes
	 * @param column X-Koordinate des zu untersuchenden Feldes
	 * @return true, wenn sich auf dem Feld keine Falle und kein Wumpus befinden.
	 */
	boolean hasNoTrapOrWumpus(int row, int column) {

		try {
			if ((!getDiscoveredTile(row, column).wumpus) && (!getDiscoveredTile(row, column).trap)) {
				return true;
			}
			return false;
		} catch (Exception e) {
			// Muss nicht behandelt werden. Wird geworfen, wenn das Feld in der GUI noch nicht aufgedeckt ist. Ist i.O.
		}
		return false;
	}
	
	/**
	 * Prüft, ob sich auf dem Feld Breeze oder Stench befindet.
	 * @param map
	 * @param row Y-Koordinate des zu untersuchenden Feldes
	 * @param column X-Koordinate des zu untersuchenden Feldes
	 * @return true, wenn sich auf dem Feld Stench oder Breeze befindet.
	 */
	boolean hasBreezeOrWumpus(WumpusMap map, int row, int column) {

		try {
			WumpusMapObject a = map.getWumpusMapObject(row, column);
			
			//		 System.out.println(a.getObjectsList());
			//   System.out.println("Y: " + currentPosY + "X: "+ currentPosX);
		//		 printListOfstepsToExcecute();
		//		 printSurroundingsOfDiscoveredTiles();
			//	 printSurroundingsOfCurrentTile();
		//		 printListOfLastVisitedTiles();
		//		 printBoundaries();
//				 System.out.println("tileWumpusAt: y/x: " + tileWumpusAt.row + " " +tileWumpusAt.column);
//				 System.out.println("Anzahl der (erkannten) Wumpi auf der Map: "+ wumpiDetected);
			
			
			if ((a.getObjectsList().contains(WumpusObjects.BREEZE)) || (a.getObjectsList().contains(WumpusObjects.STENCH))) {
				return true;
			}
			return false;
		} catch (Exception e) {
		}
		return true;
	}
	
	/**
	 * Prüft, ob sich auf dem Feld ausschließlich Stench befindet
	 * @param map
	 * @param row Y-Koordinate des zu untersuchenden Feldes
	 * @param column X-Koordinate des zu untersuchenden Feldes
	 * @return true, wenn sich auf dem Feld ausschließlich Stench befindet
	 */
	boolean hasOnlyStench(WumpusMap map, int row, int column) {

		try {
			if ((map.getWumpusMapObject(row, column).contains(WumpusObjects.STENCH)) && (!map.getWumpusMapObject(row, column).contains(WumpusObjects.BREEZE))) {
				return true;
			}
			return false;
		} catch (Exception e) {
		}
		return false;
	}
	
	
	/**
	 * Prüft, ob sich auf dem Feld ausschließlich Breeze befindet
	 * @param map
	 * @param row Y-Koordinate des zu untersuchenden Feldes
	 * @param column X-Koordinate des zu untersuchenden Feldes
	 * @return true, wenn sich auf dem Feld ausschließlich Breeze befindet
	 */
	boolean hasOnlyBreeze(WumpusMap map, int row, int column) {

		try {
			if ((map.getWumpusMapObject(row, column).contains(WumpusObjects.BREEZE)) && (!map.getWumpusMapObject(row, column).contains(WumpusObjects.STENCH))) {
				return true;
			}
			return false;
		} catch (Exception e) {
		}
		return false;
	}
	
	
	/**
	 * Bestimmt den vom Agenten als nächstes auszuführenden Bewegungsablauf. Hierfür wird die derzeitige Wissensbasis geprüft, Fallen- und Wumpuspositionen eingetragen und dementsprechend reagiert.
	 * @param map
	 * @param wK
	 * @return
	 */
	Action arrangeNextSteps(WumpusMap map, WumpusKilling wK) {
		
		backtrack = true;
	
		if(stepsToExcecute.isEmpty()) {  // Wenn alle "sicheren" Felder aufgedeckt sind, werden alle eindeutigen Fallenfelder eingetragen.
			scanMapForHazards(map);
			
			if(WumpusKilling.wumpiDetected!=0) // wenn Wumpus vorhanden
				wK.goKillWumpus(map, this); // gehe Wumpus töten
			else
				findPathTo(calculateSafestTile(),map); // ansonsten, finde das Feld, welches am sichersten ist.
		//	return Action.NO_ACTION;
		}
		
		

		Action action = stepsToExcecute.get(stepsToExcecute.size()-1); // Letzte Bewegungsrichtung ermitteln.
		stepsToExcecute.remove((stepsToExcecute.size()-1)); // Letzten Eintrag löschen.
		//printListOfstepsToExcecute();
		// Kehrt die letzte Bewegungsrichtung um.
		switch(action) {
		
			case MOVE_DOWN:
				this.setPosition(Action.MOVE_UP);
				MyOwnAI.putLastAction(Action.MOVE_UP);
				return Action.MOVE_UP;
				
			case MOVE_UP:
				this.setPosition(Action.MOVE_DOWN);
				MyOwnAI.putLastAction(Action.MOVE_DOWN);
				return Action.MOVE_DOWN;
				
			case MOVE_LEFT:
				this.setPosition(Action.MOVE_RIGHT);
				MyOwnAI.putLastAction(Action.MOVE_RIGHT);
				return Action.MOVE_RIGHT;
				
			case MOVE_RIGHT:
				this.setPosition(Action.MOVE_LEFT);
				MyOwnAI.putLastAction(Action.MOVE_LEFT);
				return Action.MOVE_LEFT;
				
			case SHOOT_UP:
				wK.wumpusShot = true;
				MyOwnAI.putLastAction(Action.SHOOT_UP);
				return Action.SHOOT_UP;
				
			case SHOOT_DOWN:
				wK.wumpusShot = true;
				MyOwnAI.putLastAction(Action.SHOOT_DOWN);
				return Action.SHOOT_DOWN;
				
			case SHOOT_LEFT:
				wK.wumpusShot = true;
				MyOwnAI.putLastAction(Action.SHOOT_LEFT);
				return Action.SHOOT_LEFT;
				
			case SHOOT_RIGHT:
				wK.	wumpusShot = true;
				MyOwnAI.putLastAction(Action.SHOOT_RIGHT);
				return Action.SHOOT_RIGHT;
		
		
		}
		
		return Action.NO_ACTION; // wird nicht eintreffen
	}
	
	
	/**
	 * Prüft, ob sich aus den bisher aufgedeckten Feldern Fallen ergeben und trägt diese ggf. in die Liste der bekannten Felder ein. Dafür werden die Umgebungen überprüft und Regeln angewandt.
	 */
	void scanMapForHazards(WumpusMap map) {
	
		System.out.println("Jetzt arbeitet SCANMAPFORHAZZARDS()");
		ListIterator<Tile> lIter = discoveredTiles.listIterator();
		while(lIter.hasNext()) {  // Alle bisher aufgedeckten Felder werden durchlaufen
			
			Tile current = lIter.next();
			
			try {
			//	System.out.println("Prüfe jetzt Feld Y: "+current.row+"X: "+current.column);
				WumpusMapObject a = map.getWumpusMapObject(current.row, current.column);
				
				if(a.getObjectsList().contains(WumpusObjects.BREEZE) && a.getObjectsList().contains(WumpusObjects.STENCH)) { // Wenn Stench UND Breeze auf dem Feld sind.
					Rule.applyRule3(current,lIter, map, this);
				}
	
				else if((a.getObjectsList().contains(WumpusObjects.BREEZE)) || a.getObjectsList().contains(WumpusObjects.STENCH)) { // Wenn Stench ODER Breeze auf dem Feld ist.
					Rule.applyRule1(current, lIter, a, this); 
					Rule.applyRule2(current, lIter, a, map, this);
				}
			}
			catch(Exception e) {
				// Exception wird geworfen, wenn das abgefragte Feld in der GUI noch nicht aufgedeckt wurde. Fehlerbehandlung hier nicht nötig.
			}
		}

	}
	
	
	/**
	 * Vermerkt eine Fallenposition auf der Karte, falls noch nicht eingetragen.
	 * @param row Y-Koordinate der Falle
	 * @param column X-Koordinate der Falle
	 * @param trapBool Fallenfeld?
	 * @param wumpusBool Wumpusfeld?
	 * @param trapORwumpusBool Unsicher?
	 * @param lIter Iterator zum sicheren Hinzufügen in der Liste
	 */
	void setTrapTile(int row, int column, boolean trapBool, boolean wumpusBool, boolean trapORwumpusBool, ListIterator<Tile> lIter) {

		if (!searchTile(row, column)) { // Feld noch nicht eingetragen?
			lIter.add(new Tile(row, column, trapBool, wumpusBool, trapORwumpusBool));
			updateNeighboringTiles(row, column); // Nachbarfelder der Falle aktualisieren
		}
	} // Eine Prüfung auf "trapOrWumpus" entfällt hier, da eine Fallenposition nach Regel3 nicht erneut mit einer Falle überschrieben werden muss.
	
	
	
	/**
	 * Vermerkt eine Wumpusposition auf der Karte, falls noch nicht eingetragen.
	 * @param row Y-Koordinate des Wumpusfeld
	 * @param column X-Koordinate des Wumpusfeld
	 * @param trapBool Fallenfeld?
	 * @param wumpusBool Wumpusfeld?
	 * @param trapORwumpusBool Unsicher?
	 * @param lIter Iterator zum sicheren Hinzufügen in der Liste
	 */
	void setWumpusTile(int row, int column, boolean trapBool, boolean wumpusBool, boolean trapORwumpusBool, ListIterator<Tile> lIter) {

		if (!searchTile(row, column)) { // Feld noch nicht eingetragen?
			WumpusKilling.wumpiDetected ++;
			lIter.add(new Tile(row, column, trapBool, wumpusBool, trapORwumpusBool));
			updateNeighboringTiles(row, column); // Nachbarfelder des Wumpus aktualisieren
		}
		else if (getDiscoveredTile(row, column).trapORwumpus) { // Falls Feld bereits eingetragen: Prüfen auf "trapOrWumpus". Hier wird -falls möglich- die aus Regel3 resultierte Unsicherheit aufgelöst.
			getDiscoveredTile(row, column).wumpus = true;
			WumpusKilling.wumpiDetected++;
			getDiscoveredTile(row, column).trap = false;
			getDiscoveredTile(row, column).trapORwumpus = false;
		}
	}
	
	
	
	/**
	 * Aktualisiert die Umgebungsvariablen benachbarter Felder eines neu eingetragenen Feldes.
	 * @param row
	 * @param column
	 */
	void updateNeighboringTiles(int row, int column) {

		ListIterator<Tile> lIter = discoveredTiles.listIterator();
		while (lIter.hasNext()) {
			Tile current = (Tile) lIter.next();
			if ((current.row - 1 == row) && (current.column == column)) { // Oberes Nachbardeld?
				current.downChecked = true;
			}
			else if ((current.row + 1 == row) && (current.column == column)) { // Unteres Nachbarfeld?
				current.upChecked = true;
			}
			else if ((current.row == row) && (current.column + 1 == column)) { // linkes Nachbarfeld?
				current.rightChecked = true;
			}
			else if ((current.row == row) && (current.column - 1 == column)) { // rechtes Nachbarfeld?
				current.leftChecked = true;
			}
		}
	}
	
	
	/**
	 * Bestimmt den Weg von der aktuellen Position des Agenten bis zum angegebenen Feld und die dort auszuführende Aktion.
	 * Der Weg wird in der Liste 'stepsToExcecute' abgelegt.
	 * @param t Feld, zu dem der Weg ermittelt werden soll.
	 */
	void findPathTo(Tile destinationTile, WumpusMap map) {
		System.out.println("Jetzt arbeitet findPathTo()");
		// Zu beachten: Die Bewegungsrichtungen müssen rückwärts eingegeben werden. Das bedeutet: Die letzte Bewegung muss als erstes mit add.() hinzugefügt werden und die richtung auch noch invertiert werden. 
	
		int i;
		
		List<Tile> lastVisitedSubList = new ArrayList<Tile>();
		List<Tile> copyOfLastDiscoveredTiles = new ArrayList<Tile>();
		lastVisitedSubList.clear();
		copyOfLastDiscoveredTiles.clear();
		
		//Kopiervorgang der Liste lastVisitedTiles
		ListIterator<Tile> lIter = lastVisitedTiles.listIterator();
		while (lIter.hasNext()) {
			Tile current = lIter.next();
			copyOfLastDiscoveredTiles.add(new Tile(current)); // Die einzelnen Felder aus lastVisitedTiles werden über Kopierkonstruktor kopiert, damit dabei nicht die selbe Referenz auf die Felder verwendet wird.
		}
	
//		System.out.println("Die Originalliste");
//		for(Tile current : lastVisitedTiles) {
//			System.out.println(current.row + " " + current.column);
//		}
//		System.out.println("copyOfLastDiscoveredTiles nach dem Kopieren des Originals");
//		for(Tile current : copyOfLastDiscoveredTiles) {
//			System.out.println(current.row + " " + current.column);
//		}
//		
//		System.out.println("destinationTile " + destinationTile.row + " " + destinationTile.column);

			// Untermenge von lastVisitedTiles bestimmen, mit allen relevanten Feldern.
		for(i=lastVisitedTiles.size()-1; i>0; i--) {
			if( (lastVisitedTiles.get(i).row == destinationTile.row) && (lastVisitedTiles.get(i).column == destinationTile.column) )  // Wenn Zielfeld in der Liste gefunden
				lastVisitedSubList = copyOfLastDiscoveredTiles.subList(i,lastVisitedTiles.size()); // Erstellen einer Unterliste, welche nur alle relevanten Felder enthält (alle zwischen Zielort (i) und aktuellen Feld (zuletzt hinzugefügtes, also .size()-1)
		}
		
		
//		System.out.println("lastVisitedSubList nach dem Schneiden");
//		for(Tile current : lastVisitedSubList) {
//			System.out.println(current.row + " " + current.column);
//		}
		
		
		
		
		// Hier wird der Weg zusammengesetzt mittels lastVisitedSubList.
		
		
		
		if(hasOnlyBreeze(map, destinationTile.row, destinationTile.column)){  // Dieser Teil der Wegoptimierung soll nur durchgeführt werden, wenn es sich nicht um ein Wumpusfeld handelt. Bei der Wumpus-Tötungsfunktion ist es nämlich vorzuziehen,dass er einen Teil der bereits besuchten Felder erneut besucht, da die Stench-Felder nicht automatisch aktualisiert werden. Dies ist aber für scanMapForHazzards() unverzichtbar.
			for(int j = 0; j<=lastVisitedSubList.size()-1;j++) {  //Zum optimieren der Schrittanzahl. Zwar nicht perfekt, aber wesentlich besser, als den bisher zurückgelegten Weg zurückzulaufen.
	
				for(int k = 0; k<=lastVisitedSubList.size()-3 ; k++) {
					
					if(lastVisitedSubList.get(k).row == lastVisitedSubList.get(k+2).row && lastVisitedSubList.get(k).column == lastVisitedSubList.get(k+2).column) { // Wenn ein Feld im übernächsten Schritt erneut besucht worden ist (also Schritt rückgängig gemacht), können die zwei folgenden Felder entfernt werden.
						lastVisitedSubList.remove(k+1);
						lastVisitedSubList.remove(k+1);
					}
				}
	
			}
		}
		
		
//		System.out.println("lastVisitedSubList nach dem Optimieren");
//		for(Tile current : lastVisitedSubList) {
//			System.out.println(current.row + " " + current.column);
//		}
		

		
		
		for(i=lastVisitedSubList.size()-1; i>0; i--) { // Rückwärts iterieren
		
	
			//	ein Unterschied von y+1 : move_up
			//	ein Unterschied von y-1 : move_down
			//	ein Unterschied von x+1 : move_right
			//	ein Unterschied von x-1 : move_left
			// beachten: Bewegungsrichtungen invertiert eingeben.
			
		    // hier wird der sichere Weg zum Feld zusammengesetzt
		    if(lastVisitedSubList.get(i).row+1 == lastVisitedSubList.get(i-1).row)
		    	stepsToExcecute.add(Action.MOVE_DOWN);
		    else if(lastVisitedSubList.get(i).row-1 == lastVisitedSubList.get(i-1).row)
		    	stepsToExcecute.add(Action.MOVE_UP);
		    else if(lastVisitedSubList.get(i).column+1 == lastVisitedSubList.get(i-1).column)
		    	stepsToExcecute.add(Action.MOVE_LEFT);
		    else if(lastVisitedSubList.get(i).column-1 == lastVisitedSubList.get(i-1).column)
		    	stepsToExcecute.add(Action.MOVE_RIGHT);
	
		}
		
	
	   	// Hier wird bestimmt, welche Bewegungs als nächstes Ausgeführt wird, wenn der Agent das am sichersten eingestufte Feld erreicht hat.
		// Wenn sich der Agent zu einem Feld begibt, auf dem sich ein Wumpus befindet, wird diese Bewegung in der Funktion 'goKillWumpus()' durch die Schussrichtung ersetzt.
			
			boolean noWayFoundYet = true;
			
			while (noWayFoundYet) { // Solange zufällig Richtung bestimmen, bis eine unbesuchte Richtung gewählt ist.
				int randNumb = (int) Math.ceil(Math.random() * 4); // Zufallszahl zwischen 1 und 4
				switch (randNumb) {
				case 1:
					if (!destinationTile.downChecked) {
						MapOrientation.stepsToExcecute.add(Action.MOVE_UP);
						noWayFoundYet = false;
					}
					break;
				case 2:
					if (!destinationTile.upChecked) {
						MapOrientation.stepsToExcecute.add(Action.MOVE_DOWN);
						noWayFoundYet = false;
					}
					break;
				case 3:
					if (!destinationTile.leftChecked) {
						MapOrientation.stepsToExcecute.add(Action.MOVE_RIGHT);
						noWayFoundYet = false;
					}
					break;
				case 4:
					if (!destinationTile.rightChecked) {
						MapOrientation.stepsToExcecute.add(Action.MOVE_LEFT);
						noWayFoundYet = false;
					}
					break;
				}
			}

    	Collections.reverse(stepsToExcecute); // Die Liste muss umgekehrt werden, da arrangeNextSteps() mit invertierter Logik arbeitet.

	
//    	System.out.println("Zusammengebauter Weg: ");
//    	printListOfstepsToExcecute();

	}

	
	
	/**
	 * Berechnet von allen bisher aufgedeckten Feldern das als nächstes zu besuchende.
	 * Dabei wird das Feld ermittelt, das mit der niedrigsten Wahrscheinlichkeit eine Falle enthält.
	 * Die bisherige Implementierung wählt zufällig ein Feld, aus den als potentiell sicheren bestimmten Feldern. Es muss ein Algorithmus gefunden werden, der für jedes Feld die % bestimmt.).
	 */
	Tile calculateSafestTile() {
		
		System.out.println("Jetzt arbeitet calculateSafestTile()");

		ListIterator<Tile> lIter = discoveredTiles.listIterator();
		while(lIter.hasNext()) {  // Alle bisher aufgedeckten Felder werden durchlaufen
			Tile current = lIter.next();
			if((current.downChecked == false || current.upChecked == false || current.leftChecked == false || current.rightChecked == false) && (hasNoTrapOrWumpus(current.row, current.column)) ) { //Hat das Feld ein unbesuchtes Nachbarfeld und ist kein Fallen- oder Wumpusfeld?
				potentiallySafeTiles.add(current); // als potentiell sicheres Feld eintragen

			}
		}
		
		int randNumb = (int) Math.ceil(Math.random() *potentiallySafeTiles.size() - 1); // Zufällig ein Feld aus der Liste bestimmen
		System.err.println("Als möglich sicheres Feld wurde gefunden : Y: " + potentiallySafeTiles.get(randNumb).row + "X: " + potentiallySafeTiles.get(randNumb).column);
	
		return potentiallySafeTiles.get(randNumb);



	}


}
