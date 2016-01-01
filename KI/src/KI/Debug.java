package KI;

import de.northernstars.jwumpus.core.Action;
import java.util.ListIterator;

/**
 * Stellt Methoden zum Debuggen bereit, um aktuelle Paramter wie Agentenposition
 * oder aufgedeckte Felder auszugeben.
 * 
 */
public class Debug {

	/**
	 * Gibt die bisher erkannten Grenzen der Map aus. Eine Grenze von '1000'
	 * entspricht 'Grenze noch nicht gefunden'.
	 */
	static void printBoundaries(MapOrientation mapOrientation) {

		System.out.println("");
		System.out.println("Obere Grenze: " + mapOrientation.yUpperBound);
		System.out.println("Untere Grenze: " + mapOrientation.yLowerBound);
		System.out.println("Linke Grenze: " + mapOrientation.xLeftBound);
		System.out.println("Rechte Grenze: " + mapOrientation.xRightBound);
		System.out.println("");
	}

	/**
	 * Gibt die Umgebungsfelder des aktuellen Agentenfeldes aus.
	 */
	static void printSurroundingsOfCurrentTile(MapOrientation mapOrientation) {

		System.out.println("");
		System.out.println("Y: " + mapOrientation.currentPosY + " X: "
				+ mapOrientation.currentPosX);
		System.out.println("leftChecked: "
				+ mapOrientation.getDiscoveredTile(mapOrientation.currentPosY,
						mapOrientation.currentPosX).leftChecked);
		System.out.println("upChecked: "
				+ mapOrientation.getDiscoveredTile(mapOrientation.currentPosY,
						mapOrientation.currentPosX).upChecked);
		System.out.println("rightChecked: "
				+ mapOrientation.getDiscoveredTile(mapOrientation.currentPosY,
						mapOrientation.currentPosX).rightChecked);
		System.out.println("downChecked: "
				+ mapOrientation.getDiscoveredTile(mapOrientation.currentPosY,
						mapOrientation.currentPosX).downChecked);
		System.out.println("");
	}

	/**
	 * Gibt die Surroundings aller bekannten Felder aus.
	 */
	static void printSurroundingsOfDiscoveredTiles() {

		for (Tile current : MapOrientation.discoveredTiles) {

			System.out.println("");
			System.out.println("Y: " + current.row + "X: " + current.column);
			System.out.println("upChecked: " + current.upChecked);
			System.out.println("downChecked: " + current.downChecked);
			System.out.println("leftChecked: " + current.leftChecked);
			System.out.println("rightChecked: " + current.rightChecked);
			System.out.println("WumpusDetected: " + current.wumpus);
			System.out.println("trapDetected: " + current.trap);
			System.out.println("trapORwumpus: " + current.trapORwumpus);
			System.out.println("");
			System.out.println("Größe der Liste: " + MapOrientation.discoveredTiles.size());
		}
	}

	/**
	 * 
	 * Gibt die derzeitige Position des Agenten aus.
	 */
	static void printCurrentPosition(MapOrientation mO) {

		System.out.println("");
		System.out.println("Agent befindet sich auf Feld Y: " + mO.currentPosY
				+ "X: " + mO.currentPosX);
		System.out.println("");
	}

	/**
	 * Gibt eine Liste der als nächstes auszuführenden Schritte aus.
	 * 
	 */
	static void printListOfstepsToExcecute() {

		System.out.println("");
		System.out.println("List<Tile> stepsToExcecute: ");
		ListIterator<Action> lIter = MapOrientation.stepsToExcecute.listIterator();
		while (lIter.hasNext()) {
			Action current = (Action) lIter.next();
			System.out.println(current);
		}
		System.out.println("Größe der Liste: " + MapOrientation.stepsToExcecute.size());
		System.out.println("");
	}

	/**
	 * Gibt eine Liste aller zuletzt betretenen Feldern aus.
	 */
	static void printListOfLastVisitedTiles() {

		System.out.println("");
		System.out.println("List<Tile> lastVisitedTiles: ");
		ListIterator<Tile> lIter = MapOrientation.lastVisitedTiles.listIterator();
		while (lIter.hasNext()) {

			Tile current = (Tile) lIter.next();
			System.out.println(current.row + " " + current.column);
		}
		System.out.println("Größe der Liste: " + MapOrientation.lastVisitedTiles.size());
		System.out.println("");
	}

	/**
	 * Gibt eine Liste aller bisher aufgedeckten Felder aus.
	 * 
	 */
	static void printListOfDiscoveredTiles() {

		System.out.println("");
		System.out.println("List<Tile> discoveredTiles: ");
		for (Tile current : MapOrientation.discoveredTiles) {
			System.out.println(current.row + " " + current.column);
		}
		System.out.println("Größe der Liste: " + MapOrientation.discoveredTiles.size());
		System.out.println("");
	}
}
