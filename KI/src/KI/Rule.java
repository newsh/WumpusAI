package KI;

import de.northernstars.jwumpus.core.WumpusMap;
import de.northernstars.jwumpus.core.WumpusMapObject;
import de.northernstars.jwumpus.core.WumpusObjects;
import java.util.ListIterator;

public class Rule {

	/**
	 * Wendet auf das im Paramter angegebene Feld die Regel1 an und prüft, ob sich die Falle/Wumpus eindeutig bestimmen lassen kann und vermerkt -falls möglich- die Position.
	 * @param tile
	 *            Das zu untersuchende Feld.
	 */
	static void applyRule1(Tile tile, ListIterator<Tile> lIter, WumpusMapObject a, MapOrientation mapOrientation) {

		int safeTileCount = 0;
		if (a.contains(WumpusObjects.BREEZE)) {

			if ((tile.leftChecked) && (!mapOrientation.getDiscoveredTile(tile.row, tile.column - 1).trap)) {
				safeTileCount++;
			}

			if ((tile.rightChecked) && (!mapOrientation.getDiscoveredTile(tile.row, tile.column + 1).trap)) {
				safeTileCount++;
			}
			if ((tile.downChecked) && (!mapOrientation.getDiscoveredTile(tile.row - 1, tile.column).trap)) {
				safeTileCount++;
			}
			if ((tile.upChecked) && (!mapOrientation.getDiscoveredTile(tile.row + 1, tile.column).trap)) {
				safeTileCount++;
			}
			if (safeTileCount == 3) { // Wenn 3 der benachbarte Felder aufgedeckt und ohne Fallen sind, kann die Position der Falle sicher bestimmt werden.
				if (!tile.leftChecked) { // linkes Nachbarfeld unbesucht
					tile.leftChecked = true;
					if (!mapOrientation.searchTile(tile.row, tile.column - 1)) {
						System.err.println("Falle gesetzt auf linkes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel1)");
						mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, false, lIter); // Falle links setzen
					}
				}
				else if (!tile.rightChecked) { // rechtes Nachbarfeld unbesucht
					tile.rightChecked = true;
					if (!mapOrientation.searchTile(tile.row, tile.column + 1)) {
						System.err.println("Falle gesetzt auf rechtes Nachbarfeld von Y:" + tile.row + "X:" + tile.column + " (Regel1)");
						mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, false, lIter); // Falle rechts setzen
					}
				}
				else if (!tile.downChecked) { // unteres Nachbarfeld unbesucht
					tile.downChecked = true;
					if (!mapOrientation.searchTile(tile.row - 1, tile.column)) {
						System.err.println("Falle gesetzt auf unteres Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel1)");
						mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter); // Falle unten setzen
					}
				}
				else if (!tile.upChecked) { // oberes Nachbarfeld unbesucht
					tile.upChecked = true;
					if (!mapOrientation.searchTile(tile.row + 1, tile.column)) {
						System.err.println("Falle gesetzt auf oberes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel1)");
						mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, false, lIter); // Falle oben setzen
					}
				}
			}
		}
		else if (a.contains(WumpusObjects.STENCH)) {
			safeTileCount = 0;
			if ((tile.leftChecked) && (!mapOrientation.getDiscoveredTile(tile.row, tile.column - 1).wumpus)) {
				safeTileCount++;
			}
			if ((tile.rightChecked) && (!mapOrientation.getDiscoveredTile(tile.row, tile.column + 1).wumpus)) {
				safeTileCount++;
			}
			if ((tile.downChecked) && (!mapOrientation.getDiscoveredTile(tile.row - 1, tile.column).wumpus)) {
				safeTileCount++;
			}
			if ((tile.upChecked) && (!mapOrientation.getDiscoveredTile(tile.row + 1, tile.column).wumpus)) {
				safeTileCount++;
			}
			if (safeTileCount == 3) {
				if (!tile.leftChecked) {
					if (!mapOrientation.searchTile(tile.row, tile.column - 1)) {
						System.err.println("Wumpus gesetzt auf linkes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel1)");
						mapOrientation.setWumpusTile(tile.row, tile.column - 1, false, true, false, lIter);
					}
				}
				else if (!tile.rightChecked) {
					if (!mapOrientation.searchTile(tile.row, tile.column + 1)) {
						System.err.println("Wumpus gesetzt auf rechtes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel1)");
						mapOrientation.setWumpusTile(tile.row, tile.column + 1, false, true, false, lIter);
					}
				}
				else if (!tile.downChecked) {
					if (!mapOrientation.searchTile(tile.row - 1, tile.column)) {
						System.err.println("Wumpus gesetzt auf unteres Nachbarfeld von Y:" + tile.row + "X: " + tile.column + " (Regel1)");
						mapOrientation.setWumpusTile(tile.row - 1, tile.column, false, true, false, lIter);
					}
				}
				else if (!tile.upChecked) {
					if (!mapOrientation.searchTile(tile.row + 1, tile.column)) {
						System.err.println("Wumpus gesetzt auf oberes Nachbarfeld von Y:" + tile.row + "X: " + tile.column + " (Regel1)");
						mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter);
					}
				}
			}
		}
	}

	
	/**
	 * Wendet auf das im Paramter angegebene Feld die Regel2 an und prüft ob sich die Falle/Wumpus eindeutig bestimmen lassen kann und vermerkt -falls möglich- die Fallenposition.
	 * @param tile Das zu untersuchende Feld.
	 */
	
	static void applyRule2(Tile tile, ListIterator<Tile> lIter, WumpusMapObject a, WumpusMap map, MapOrientation mapOrientation) {

		if (a.getObjectsList().contains(WumpusObjects.BREEZE)) {
			if ((!map.getWumpusMapObject(tile.row - 1, tile.column - 1).contains(WumpusObjects.BREEZE)) && (!map.getWumpusMapObject(tile.row - 1, tile.column + 1).contains(WumpusObjects.BREEZE))) {
				if (!mapOrientation.searchTile(tile.row + 1, tile.column)) {
					System.err.println("Falle gesetzt auf oberes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column - 1).contains(WumpusObjects.BREEZE)) && (!map.getWumpusMapObject(tile.row + 1, tile.column + 1).contains(WumpusObjects.BREEZE))) {
				if (!mapOrientation.searchTile(tile.row - 1, tile.column)) {
					System.err.println("Falle gesetzt auf unteres Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column - 1).contains(WumpusObjects.BREEZE)) && (!map.getWumpusMapObject(tile.row - 1, tile.column - 1).contains(WumpusObjects.BREEZE))) {
				if (!mapOrientation.searchTile(tile.row, tile.column + 1)) {
					System.err.println("Falle gesetzt auf rechtes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column + 1).contains(WumpusObjects.BREEZE)) && (!map.getWumpusMapObject(tile.row - 1, tile.column + 1).contains(WumpusObjects.BREEZE)) && (!mapOrientation.searchTile(tile.row, tile.column - 1))) {
				System.err.println("Falle gesetzt auf linkes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, false, lIter);
			}
		}
		else if (a.getObjectsList().contains(WumpusObjects.STENCH)) {
			if ((!map.getWumpusMapObject(tile.row - 1, tile.column - 1).contains(WumpusObjects.STENCH)) && (!map.getWumpusMapObject(tile.row - 1, tile.column + 1).contains(WumpusObjects.STENCH))) {
				if (!mapOrientation.searchTile(tile.row + 1, tile.column)) {
					System.err.println("Wumpus gesetzt auf oberes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column - 1).contains(WumpusObjects.STENCH)) && (!map.getWumpusMapObject(tile.row + 1, tile.column + 1).contains(WumpusObjects.STENCH))) {
				if (!mapOrientation.searchTile(tile.row - 1, tile.column)) {
					System.err.println("Wumpus gesetzt auf unteres Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setWumpusTile(tile.row - 1, tile.column, false, true, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column - 1).contains(WumpusObjects.STENCH)) && (!map.getWumpusMapObject(tile.row - 1, tile.column - 1).contains(WumpusObjects.STENCH))) {
				if (!mapOrientation.searchTile(tile.row - 1, tile.column)) {
					System.err.println("Wumpus gesetzt auf rechtes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
					mapOrientation.setWumpusTile(tile.row, tile.column + 1, false, true, false, lIter);
				}
			}
			else if ((!map.getWumpusMapObject(tile.row + 1, tile.column + 1).contains(WumpusObjects.STENCH)) && (!map.getWumpusMapObject(tile.row - 1, tile.column + 1).contains(WumpusObjects.STENCH)) && (!mapOrientation.searchTile(tile.row - 1, tile.column))) {
				System.err.println("Wumpus linkes auf oberes Nachbarfeld von Y:" + tile.row + " X:" + tile.column + " (Regel2)");
				mapOrientation.setWumpusTile(tile.row, tile.column - 1, false, true, false, lIter);
			}
		}
	}

	/**
	 * Wendet auf das im Paramter angegebene Feld die Regel3/Regel3_5 an und prüft ob sich die Falle/Wumpus eindeutig bestimmen lassen kann und vermerkt -falls möglich- die Fallenposition.
	 * @param tile Das zu untersuchende Feld.
	 */
	
	static void applyRule3(Tile tile, ListIterator<Tile> lIter, WumpusMap map, MapOrientation mapOrientation) {

		// Entspricht Abbildung1
		if ((mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column - 1)) && (mapOrientation.hasNoTrapOrWumpus(tile.row - 1, tile.column))) { // Felder links und unten Frei
			if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row + 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column + 2))) {
				System.out.println("Setze jetzt WumpusTile und TrapTile nach Regel3");
				mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter); // Wumpus oben setzen
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, false, lIter); // Falle rechts setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row, tile.column + 2)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 2, tile.column))) {
				System.out.println("Setze jetzt WumpusTile und TrapTile nach Regel3");
				mapOrientation.setWumpusTile(tile.row, tile.column + 1, false, true, false, lIter); // Wumpus rechts setzen
				mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, false, lIter); // Falle Oben setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				System.out.println("Setze jetzt TrapTile/WumpusTile mit dem Vermerk als nicht genau bestimmbar");
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, true, lIter); // Falle rechts setzen mit Vermerk "Falle oder Wumpus"
				mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, true, lIter); // Falle oben setzen mit Vermerk "Falle oder Wumpus"
			}
		} 
		// Entspricht Abbildung 2
		else if ((mapOrientation.hasNoTrapOrWumpus(tile.row + 1, tile.column)) && (mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column + 1))) { // Felder oben und rechts frei
			if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row, tile.column - 2)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column + 1))) {
				System.out.println("Setze jetzt WumpusTile und TrapTilenach Regel3");
				mapOrientation.setWumpusTile(tile.row, tile.column - 1, false, true, false, lIter); // Wumpus links setzen
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter); // Falle unten setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row - 2, tile.column)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column - 2))) {
				System.out.println("Setze jetzt WumpusTile und TrapTilenach Regel3");
				mapOrientation.setWumpusTile(tile.row - 1, tile.column, false, true, false, lIter); // Wumpus unten setzen
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, false, lIter); // Falle links setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				System.out.println("Setze jetzt TrapTile/WumpusTile mit dem Vermerk als nicht genau bestimmbar");
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, true, lIter); // Falle links setzen mit Vermerk "Falle oder Wumpus"
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, true, lIter); // Falle unten setzen mit Vermerk "Falle oder Wumpus"
			}
		}
		// Entspricht Abbildung 3
		else if ((mapOrientation.hasNoTrapOrWumpus(tile.row + 1, tile.column)) && (mapOrientation.hasNoTrapOrWumpus(tile.row - 1, tile.column))) { // Felder oben und unten frei
			if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row, tile.column - 2)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column + 2)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column + 1))) {
				System.out.println("Setze jetzt WumpusTile und TrapTilenach Regel3");
				mapOrientation.setWumpusTile(tile.row, tile.column - 1, false, true, false, lIter); // Wumpus links setzen
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, false, lIter); // Falle rechts setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyStench(map, tile.row, tile.column + 2)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column - 2)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column - 1))) {
				mapOrientation.setWumpusTile(tile.row, tile.column + 1, false, true, false, lIter); // Wumpus rechts setzen
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, false, lIter); // Falle links setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, true, lIter); // Falle links setzen mit Vermerk "Falle oder Wumpus"
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, true, lIter); // Falle rechts setzen mit Vermerk "Falle oder Wumpus"
			}
		}
		// Entspricht Abbildung 4
		else if ((mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column - 1)) && (mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column + 1))) { // Felder links und rechts frei
			if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row + 2, tile.column)) || (mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column + 1))) {
				mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter); // Wumpus oben setzen
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter); // Falle unten setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row - 2, tile.column)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column + 1))) {
				mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter); // Wumpus unten setzen
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter); // Falle oben setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, true, lIter); // Falle oben setzen mit Vermerk "Falle oder Wumpus"
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, true, lIter); // Falle unten setzen mit Vermerk "Falle oder Wumpus"
			}
		}
		// Entspricht Abbildung 5
		else if ((mapOrientation.hasNoTrapOrWumpus(tile.row - 1, tile.column)) && (mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column + 1))) { // Felder rechts und unten frei
			if ((mapOrientation.hasOnlyStench(map, tile.row + 2, tile.column)) || (mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column - 2)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column - 1))) {
				mapOrientation.setWumpusTile(tile.row + 1, tile.column, false, true, false, lIter); // Wumpus oben setzen
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, false, lIter); // Falle links setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row, tile.column - 2)) || (mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column + 1))) {
				mapOrientation.setWumpusTile(tile.row, tile.column - 1, false, true, false, lIter); // Wumpus links setzen
				mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, false, lIter); // Falle oben setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				mapOrientation.setTrapTile(tile.row, tile.column - 1, true, false, true, lIter); // Falle links setzen mit Vermerk "Falle oder Wumpus"
				mapOrientation.setTrapTile(tile.row + 1, tile.column, true, false, true, lIter); // Falle rechts setzen mit Vermerk "Falle oder Wumpus"
			}
		}
		// Entspricht Abbildung 6
		else if ((mapOrientation.hasNoTrapOrWumpus(tile.row + 1, tile.column)) && (mapOrientation.hasNoTrapOrWumpus(tile.row, tile.column - 1))) { // Felder oben und links frei?
			if ((mapOrientation.hasOnlyStench(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyStench(map, tile.row, tile.column + 2)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row - 2, tile.column))) {
				mapOrientation.setWumpusTile(tile.row, tile.column + 1, false, true, false, lIter); // Wumpus rechts setzen
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, false, lIter); // Falle unten setzen
			}
			else if ((mapOrientation.hasOnlyStench(map, tile.row - 1, tile.column - 1)) || (mapOrientation.hasOnlyStench(map, tile.row - 2, tile.column)) || (mapOrientation.hasOnlyBreeze(map, tile.row + 1, tile.column + 1)) || (mapOrientation.hasOnlyBreeze(map, tile.row, tile.column + 2))) {
				mapOrientation.setWumpusTile(tile.row - 1, tile.column, false, true, false, lIter); // Wumpus unten setzen
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, false, lIter); // Falle rechts setzen
			}
			else { // Position von Falle und Wumpus nicht genau bestimmtbar, beide Felder sind aber unsicher. Daher auf beiden Feldern eine Falle vermerken.
				mapOrientation.setTrapTile(tile.row, tile.column + 1, true, false, true, lIter); // Wumpus rechts setzen
				mapOrientation.setTrapTile(tile.row - 1, tile.column, true, false, true, lIter); // Falle unten setzen
			}
		}
	}
}
