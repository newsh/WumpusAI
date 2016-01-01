package KI;

import de.northernstars.jwumpus.core.Action;
import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.core.WumpusObjects;
import java.util.ListIterator;

/**
 * Stellt Funktionen bereits zum Töten des Wumpus
 * 
 */
public class WumpusKilling {

	boolean wumpusShot = false;
	static int wumpiDetected = 0;
	Tile tileToShootFrom = new Tile(0, 0);
	Tile tileWumpusAt = new Tile(0, 0);
	Action shootDirection;
	int checkWumpusDead = 0; // Hilfsvariable, zum Bestimmen ob der Wumpus tot ist.

	/**
	 * Lässt Agenten zum Wumpus gehen und tötet ihn.
	 */

	void goKillWumpus(WumpusMap map, MapOrientation mO) {

		System.out.println("Starte jetzt die Wumpus-Tötungs Funktion");

		mO.findPathTo(findTileToShootFrom(map, mO), map);
		MapOrientation.stepsToExcecute.add(0, (Action) MapOrientation.stepsToExcecute.get(MapOrientation.stepsToExcecute.size() - 1)); // Setzt die in 'findTileToShootFrom()' bestimmte Schussaktion an den Beginn der Liste.
		MapOrientation.stepsToExcecute.remove(MapOrientation.stepsToExcecute.size() - 1); // Löschen der jetzt doppelt vorhandenen Schussaktion
		MapOrientation.stepsToExcecute.remove(1); // Entfernt die aus in 'findPathTo()' bestimmte "sichere nächste Aktion". Beim Wumpus töten soll nämlich erstmal geschossen werden.

		System.out.println("Zu erschiessender Wumpus befindet sich auf Feld Y: " + this.tileWumpusAt.row + " X: " + this.tileWumpusAt.column);
	}

	/**
	 * Hier wird der Bewegungablauf bestimmt, der ausgeführt wird, um zu prüfen, ob der Stench beseitigt ist (Wumpus also tot) und wie der Agent reagiert, wenn der direkt benachbarte Wumpus getroffen worden ist oder ein Wumpus dahinter.
	 * 
	 * @param map
	 */

	void checkWumpusDead(WumpusMap map, MapOrientation mO) {

		if(this.checkWumpusDead == 2) { // Bewegung ausgeführt? Erst dann das Feld auf Stench prüfen. Die Bewegung wurde ausgeführt, wenn die Funktion zweimal durchlaufen wurde.

			if (!map.getWumpusMapObject(mO.currentPosY, mO.currentPosX).contains(WumpusObjects.STENCH)) { // Prüfung abgeschlossen und kein Stench mehr auf dem Feld. Der benachbarte Wumpus wurde getötet.
				
				wumpiDetected--; // Ein Wumpus weniger auf der Map
				this.wumpusShot = false; // damit der nächste Aufruf von getAction() wieder undoStep() ausführt. Denn dort ist die auszuführende Bewegung nun abgelegt.
				mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column);  // Das Feld des erschossenen Wumpus entfernen, damit es von der KI bei Kontakt neu erkundet wird.
				
				
				// Die entsprechenden Nachbarfelder des Wumpusfeld löschen, damit sie von der KI neu besucht werden. Das erneute Besuchen der Felder ist notwendig, denn es gibt keine automatische Aktualisierung der Umgebung für den Agenten.
				switch (this.shootDirection) { 
				case SHOOT_DOWN:
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column - 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column + 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row - 1, this.tileWumpusAt.column);
					MapOrientation.stepsToExcecute.add(Action.MOVE_UP);
					break;
				case SHOOT_UP:
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column - 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column + 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row + 1, this.tileWumpusAt.column);
					MapOrientation.stepsToExcecute.add(Action.MOVE_DOWN);
					break;
				case SHOOT_RIGHT:
					mO.deleteDiscoveredTile(this.tileWumpusAt.row + 1, this.tileWumpusAt.column);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column + 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row - 1, this.tileWumpusAt.column);
					MapOrientation.stepsToExcecute.add(Action.MOVE_LEFT);
					break;
				case SHOOT_LEFT:
					mO.deleteDiscoveredTile(this.tileWumpusAt.row + 1, this.tileWumpusAt.column);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row, this.tileWumpusAt.column - 1);
					mO.deleteDiscoveredTile(this.tileWumpusAt.row - 1, this.tileWumpusAt.column);
					MapOrientation.stepsToExcecute.add(Action.MOVE_RIGHT);
				}
			}
			else { // Stench liegt noch immer auf dem Feld. Ein Wumpus wurde zwar getroffen, aber es muss ein dahinterliegender gewesen sein. Agent soll nochmal schießen. Anschließend wird das Feld erneut geprüft.
				MapOrientation.stepsToExcecute.add(this.shootDirection);
			}
		}
		
		// Zum Prüfen des Stenchfeldes muss der Agent das Feld verlassen und erneut besuchen. Dafür werden die beiden zuletzt ausgeführten Aktionen in umgekehrter Reihenfolge ausgeführt. lastMoventsPerformed ist zu dieser Stelle im Code leider leer, deswegen muss die Bewegung über lastVisitedTiles ermittelt werden.
		if (MapOrientation.stepsToExcecute.isEmpty()) { //Bewegungsablauf nur Zusammensetzen, wenn noch nicht geschehen, wenn die Liste also leer ist.
			this.checkWumpusDead = 0;
			if ((((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).row == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).row) && (((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).column - 1 == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).column)) {
				MapOrientation.stepsToExcecute.add(Action.MOVE_LEFT);
				MapOrientation.stepsToExcecute.add(Action.MOVE_RIGHT);
			}
			else if ((((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).row == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).row) && (((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).column + 1 == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).column)) {
				MapOrientation.stepsToExcecute.add(Action.MOVE_RIGHT);
				MapOrientation.stepsToExcecute.add(Action.MOVE_LEFT);
			}
			else if ((((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).row - 1 == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).row) && (((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).column == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).column)) {
				MapOrientation.stepsToExcecute.add(Action.MOVE_DOWN);
				MapOrientation.stepsToExcecute.add(Action.MOVE_UP);
			}
			else if ((((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).row + 1 == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).row) && (((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 2)).column == ((Tile) MapOrientation.lastVisitedTiles.get(MapOrientation.lastVisitedTiles.size() - 3)).column)) {
				MapOrientation.stepsToExcecute.add(Action.MOVE_UP);
				MapOrientation.stepsToExcecute.add(Action.MOVE_DOWN);
			}
		}
		this.checkWumpusDead++;
	}

	
	/**
	 * Bestimmt das Feld, auf dem sich der Wumpus befindet, das Feld vom dem der Wumpus aus erschossen werden kann und die Schussrichtung.
	 */
	
	Tile findTileToShootFrom(WumpusMap map, MapOrientation mO) {

		ListIterator<Tile> lIter = MapOrientation.lastVisitedTiles.listIterator();
		
		while (lIter.hasNext()) {
			
			Tile current = (Tile) lIter.next();
			
			if ((map.getWumpusMapObject(current.row, current.column).contains(WumpusObjects.STENCH)) && (mO.getDiscoveredTile(current.row + 1, current.column).wumpus)) { // Feld mit Stench gefunden und oberes Nachbarfeld mit Wumpus
				this.tileToShootFrom = current; // Feld, von dem aus der Agent schießt
				this.tileWumpusAt = mO.getDiscoveredTile(current.row + 1, current.column); // Position des Wumpus
				MapOrientation.stepsToExcecute.add(Action.SHOOT_UP); // Schuss in Bewegungsablauf legen
				this.shootDirection = Action.SHOOT_UP; // Schussrichtung speichern
				System.out.println("findTileToShootfrom() hat gefunden   Y:" + this.tileToShootFrom.row + " X:" + this.tileToShootFrom.column);
				return this.tileToShootFrom;
			}
			if ((map.getWumpusMapObject(current.row, current.column).contains(WumpusObjects.STENCH)) && (mO.getDiscoveredTile(current.row - 1, current.column).wumpus)) { // Feld mit Stench gefunden und unteres Nachbarfeld mit Wumpus
				this.tileToShootFrom = current; // Feld, von dem aus der Agent schießt
				this.tileWumpusAt = mO.getDiscoveredTile(current.row - 1, current.column);  // Position des Wumpus
				MapOrientation.stepsToExcecute.add(Action.SHOOT_DOWN); // Schuss in Bewegungsablauf legen
				this.shootDirection = Action.SHOOT_DOWN; // Schussrichtung speichern
				System.out.println("findTileToShootfrom() hat gefunden   Y:" + this.tileToShootFrom.row + " X:" + this.tileToShootFrom.column);
				return this.tileToShootFrom;
			}
			if ((map.getWumpusMapObject(current.row, current.column).contains(WumpusObjects.STENCH)) && (mO.getDiscoveredTile(current.row, current.column - 1).wumpus)) { // Feld mit Stench gefunden und linkes Nachbarfeld mit Wumpus
				this.tileToShootFrom = current; // Feld, von dem aus der Agent schießt
				this.tileWumpusAt = mO.getDiscoveredTile(current.row, current.column - 1);  // Position des Wumpus
				MapOrientation.stepsToExcecute.add(Action.SHOOT_LEFT); // Schuss in Bewegungsablauf legen
				this.shootDirection = Action.SHOOT_LEFT; // Schussrichtung speichern
				System.out.println("findTileToShootfrom() hat gefunden   Y:" + this.tileToShootFrom.row + " X:" + this.tileToShootFrom.column);
				return this.tileToShootFrom;
			}
			if ((map.getWumpusMapObject(current.row, current.column).contains(WumpusObjects.STENCH)) && (mO.getDiscoveredTile(current.row, current.column + 1).wumpus)) { // Feld mit Stench gefunden und rechts Nachbarfeld mit Wumpus
				this.tileToShootFrom = current; // Feld, von dem aus der Agent schießt
				this.tileWumpusAt = mO.getDiscoveredTile(current.row, current.column + 1); // Position des Wumpus
				MapOrientation.stepsToExcecute.add(Action.SHOOT_RIGHT); // Schuss in Bewegungsablauf legen
				this.shootDirection = Action.SHOOT_RIGHT; // Schussrichtung speichern
				System.out.println("findTileToShootfrom() hat gefunden   Y:" + this.tileToShootFrom.row + " X:" + this.tileToShootFrom.column);
				return this.tileToShootFrom;
			}
		}
		System.out.println("FEHLER: findTileToShootFrom() liefert new Tile(0,0). Das hätte nicht passieren dürfen");
		return new Tile(0, 0);
	}
}
