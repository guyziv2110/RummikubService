/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubGameMoveState;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;

public class RummikubAIPutNumberSeriesOnBoardCommand implements RummikubAICommand{
    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private int gameId;
    
    @Override
    public void execute(int gameId,
                        List<RummikubGameMoveState> gameMoveStates, 
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws 
            RummikubPoolOutOfTilesException, 
            RummikubTilesPositionCollisionException {
        commandOnBoard = rummBoard;
        commandOnPlayer = rummPlayer; 
        this.gameId = gameId;
        gameMoveStates.addAll(findSeriesMatchesToPutOnBoard());
    }
    
    
    private List<RummikubGameMoveState> findSeriesMatchesToPutOnBoard() throws 
            RummikubTilesPositionCollisionException {
        
        List<RummikubGameMoveState> listOfRummGameMoveState = new ArrayList<RummikubGameMoveState>();
        List<RummikubTile> currentRummTiles = new ArrayList<> (commandOnPlayer.getPlayerDeckTiles());        
        int currentTileNumber;
        int startSeq = -1;
        
        Collections.sort(currentRummTiles, RummikubTile::compareByColorAndNumber);
        
        for (currentTileNumber = 0; currentTileNumber< commandOnPlayer.getPlayerDeckTiles().size() - 1; currentTileNumber++){
            if((isSequentialTiles(commandOnPlayer.getPlayerDeckTiles().get(currentTileNumber),
                                   commandOnPlayer.getPlayerDeckTiles().get(currentTileNumber + 1)))) {
                if (startSeq == -1) 
                    startSeq = currentTileNumber;
            }
            else {
                if(startSeq > 0 && currentTileNumber - startSeq + 1 >= 3) 
                {     
                    if(RummikubAICommand.isValidMovePoints(currentRummTiles.subList(startSeq, currentTileNumber + 1), commandOnBoard, commandOnPlayer)) {
                        RummikubAICommand.putListOfTilesOnBoard(gameId, currentRummTiles.subList(startSeq, currentTileNumber + 1), commandOnBoard, commandOnPlayer);
                        listOfRummGameMoveState.add(new RummikubGameMoveState(commandOnBoard));
                    }
                }
                startSeq = -1;
            }
        }
        
        return listOfRummGameMoveState;
    }    
   
    private boolean isSequentialTiles(RummikubTile firstTile, RummikubTile secondTile) {
        if((firstTile.getTileNumber().getNumber() == secondTile.getTileNumber().getNumber() - 1) && 
            (firstTile.getTileColor() == secondTile.getTileColor()))  {
            return true;
        }
        return false;
    }
}

 