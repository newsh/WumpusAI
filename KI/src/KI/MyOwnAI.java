package KI;
import de.northernstars.jwumpus.core.*;

public class MyOwnAI implements WumpusAI{
	
	private WumpusMap map;
	private long remainingTime = 0;
	private int remainingArrows = 0;
	private ActionSuccess lastActionSuccess = ActionSuccess.SUCCESSFULL;
	static private Action lastAction;
	private PlayerState playerState = PlayerState.UNKNOWN;
	private WumpusKilling wumpusKilling = new WumpusKilling();
	private MapOrientation mapOrientation= new MapOrientation();
	private boolean dangerDetected = false; // Stench oder Breeze wurde wahrgenommen
	@Override
	public void putWumpusWorldMap(WumpusMap map) {
		this.map = new WumpusMap(map);
		dangerDetected = mapOrientation.hasBreezeOrWumpus(map,mapOrientation.currentPosY, mapOrientation.currentPosX); //Prüfen, ob der Agent auf einen Fallenindikator gelaufen ist.
	}

	 public static void putLastAction(Action action) {
		lastAction = action;
	}
	
	
	@Override
	public void putLastActionSuccess(ActionSuccess actionSuccess) {
		
		lastActionSuccess = actionSuccess;

		if(actionSuccess == ActionSuccess.HIT_WALL) 
			mapOrientation.updateBoundaries(lastAction);
		else {
			mapOrientation.addDiscoveredTile(mapOrientation.currentPosY,mapOrientation.currentPosX);
			mapOrientation.addLastVisitedTile(lastAction);
			mapOrientation.setTileSurroundings();
		}

		
		
//		mapOrientation.printListOfLastVisitedTiles();
//		mapOrientation.printListOfStepsToExcecute();  //Achtung beim Debuggen: in arrangeNextSteps muss die Liste auch nochmal ausgegeben werden.
//		mapOrientation.printSurroundingsOfDiscoveredTiles();
//		mapOrientation.printCurrentPosition(lastAction);
//		System.out.println(lastAction);
		
	}

	@Override
	public void putRemainingTime(long time) {

		remainingTime = time;
	
		}
	

	@Override
	public void putPlayerArrows(int arrows) {
		remainingArrows = arrows;
	}

	@Override
	public Action getAction() {
		

        if(wumpusKilling.wumpusShot)
        	wumpusKilling.checkWumpusDead(map, mapOrientation); // in dieser Methode dann reinschreiben, dass die entsprechende Umgebungsvariablen (2 Felder müssen bearbeitet werden) = false sind, wenn das Feld sicher geworden ist.
		
		Tile currentTile = mapOrientation.getDiscoveredTile(mapOrientation.currentPosY, mapOrientation.currentPosX);
		
        if( (!currentTile.checkSurroundings()) | (dangerDetected == true) ){ // Prüfen, ob alle benachbarten Felder bereits besucht worden sind oder BREEZE/STENCH vorhanden.
			return mapOrientation.arrangeNextSteps(map, wumpusKilling); // Ein Schritt zurückgehen.
		}
		
				
		while(true) { // Solange eine Zufallszahl generieren, bis der Agent sich bewegt hat.
			
			int randNumb = (int) Math.ceil(Math.random()*4); //generiert Zufallszahl von 1 bis 4
			
			switch(randNumb) {
			
				case 1:
					if(currentTile.downChecked == false) { // Prüfen, ob das untere Feld bereits besucht ist.
						mapOrientation.backtrack = false;
						mapOrientation.setPosition(Action.MOVE_DOWN);
						lastAction = Action.MOVE_DOWN;
						return Action.MOVE_DOWN;
			         }
					 break;
					 
				case 2:
					if(currentTile.upChecked == false) { // Prüfen, ob das obere Feld bereits besucht ist.
						mapOrientation.backtrack = false;
						mapOrientation.setPosition(Action.MOVE_UP);
						lastAction = Action.MOVE_UP;
						return Action.MOVE_UP;
					}
					break;
					
				case 3: 
					if(currentTile.leftChecked == false) { // Prüfen, ob das linke Feld bereits besucht ist.
						mapOrientation.backtrack = false;
						mapOrientation.setPosition(Action.MOVE_LEFT);
						lastAction = Action.MOVE_LEFT;
						return Action.MOVE_LEFT;
					}
					break;
			
				case 4:
					if(currentTile.rightChecked == false) { // Prüfen, ob das rechte Feld bereits besucht ist.
						mapOrientation.backtrack = false;
						mapOrientation.setPosition(Action.MOVE_RIGHT);
						lastAction = Action.MOVE_RIGHT;
						return Action.MOVE_RIGHT;
					}
					break;
			}

		}
	}

	@Override
	public void putPlayerState(PlayerState playerState) {

		this.playerState = playerState;
	}

	@Override
	public void resetAi() {
	//	System.out.println("resetAI() wurde ausgeführt");
		playerState = PlayerState.UNKNOWN;
		mapOrientation.currentPosX=0;
		mapOrientation.currentPosY=0;
		MapOrientation.discoveredTiles.clear();
		MapOrientation.stepsToExcecute.clear();
		MapOrientation.lastVisitedTiles.clear();
		mapOrientation.xLeftBound = 1000;
		mapOrientation.xRightBound = 1000;
		mapOrientation.yUpperBound = 1000;
		mapOrientation.yLowerBound = 1000;
		mapOrientation.backtrack = false;
		wumpusKilling.wumpusShot = false;
		WumpusKilling.wumpiDetected = 0;
		wumpusKilling.tileToShootFrom = new Tile(0,0);
		wumpusKilling.tileWumpusAt = new Tile(0,0);
		lastActionSuccess = ActionSuccess.SUCCESSFULL;
		lastAction = Action.NO_ACTION;
		mapOrientation= new MapOrientation();
		dangerDetected = false;

	}


/**
 * Debugfunktion. Agent bewegt sich willkürlich auf der Map.
 * 
 * @return Die auszuführende Bewegungsrichtug.
 */
Action moveRandom() {
	
	// Hier wird zufällig eine Richtung gewählt
			int randNumb = (int) Math.ceil(Math.random()*4); //Zufallszahl von 1 bis 4
			switch(randNumb) {
			
			case 1: 
				mapOrientation.setPosition(Action.MOVE_DOWN);
				lastAction = Action.MOVE_DOWN;
				System.out.println(lastAction);
				return Action.MOVE_DOWN;
				
			case 2:
				mapOrientation.setPosition(Action.MOVE_UP);
				lastAction = Action.MOVE_UP;
				System.out.println(lastAction);
				return Action.MOVE_UP;

			case 3: 
				mapOrientation.setPosition(Action.MOVE_LEFT);
				lastAction = Action.MOVE_LEFT;
				System.out.println(lastAction);
				return Action.MOVE_LEFT;
				
			case 4:
				mapOrientation.setPosition(Action.MOVE_RIGHT);
				lastAction = Action.MOVE_RIGHT;
				System.out.println(lastAction);
				return Action.MOVE_RIGHT;
			
			}
			return Action.NO_ACTION;
}
	

/**
 * Debugfunktion. Agent bewegt sich nur nach oben
 * @return
 */
Action moveUpOnly() {
	
	mapOrientation.setPosition(Action.MOVE_UP);
	lastAction = Action.MOVE_UP;
	System.out.println(lastAction);
	return Action.MOVE_UP;
	
}





}