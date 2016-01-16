/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubComputerPlayer;
import rummikubengine.RummikubGameMoveState;
import rummikubengine.RummikubNoValidMoveExistsException;
import rummikubengine.RummikubOutOfMovesException;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;

// we will use the game state in this class for future uses to make
// the AI smarter.

public class RummikubAIController  {
    private final RummikubBoard rummikubBoard;
    private final SimpleBooleanProperty commandDone;
    private int gameId;
    private Timeline fiveSecondsWonder;
    
    public RummikubAIController(int gameId, RummikubBoard rummikubBoard) {
        this.rummikubBoard = rummikubBoard;
        this.gameId = gameId;
        commandDone = new SimpleBooleanProperty(false);
    }
    
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }         
    
    public void playTurn(RummikubComputerPlayer rummComputerPlayer) throws 
             RummikubPoolOutOfTilesException, 
             RummikubNoValidMoveExistsException, RummikubOutOfMovesException, 
             RummikubTilesPositionCollisionException     {
         
        //commandDone.set(false);
        List<RummikubGameMoveState> listOfRummGameMoveState = getAllPossibleMoves(rummComputerPlayer);
        
        if (listOfRummGameMoveState.isEmpty() && rummikubBoard.isBoardPoolEmpty()) {
            throw new RummikubNoValidMoveExistsException(rummComputerPlayer);
        }
       
        // if none of the game move state is valid then the list of game move state is empty,
        // and therefore the computer must take a tile from the board.
        if(listOfRummGameMoveState.isEmpty()){
            if(rummikubBoard.isBoardPoolEmpty())
                throw new RummikubOutOfMovesException();
            else 
                this.rummikubBoard.addTileToPlayer(this.rummikubBoard.getSingleRummikubTile(), 
                                               rummComputerPlayer);
        }
        
        
        
     
        
        
        
    }

    private List<RummikubGameMoveState> getAllPossibleMoves(RummikubComputerPlayer rummComputerPlayer) throws RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException {
        List<RummikubGameMoveState> listOfRummGameMoveState = new ArrayList<RummikubGameMoveState>();
        listOfRummGameMoveState.addAll(findMatchesToPutOnBoard(rummComputerPlayer)); 

        return listOfRummGameMoveState;
    }
    
    private List<RummikubGameMoveState> findMatchesToPutOnBoard(RummikubComputerPlayer rummComputerPlayer) throws RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException {
        List<RummikubGameMoveState> listOfRummGameMoveState = 
                new ArrayList<RummikubGameMoveState>();
        RummikubAIPutColorSeriesOnBoardCommand putColorSeriesOnBoardCommand = 
                new  RummikubAIPutColorSeriesOnBoardCommand();
        RummikubAIPutNumberSeriesOnBoardCommand putNumberSeriesOnBoardCommand = 
                new RummikubAIPutNumberSeriesOnBoardCommand();
        RummikubAIAddTileToExistingSeriesOnBoardCommand addTileToExistingSeriesCommand = 
                new RummikubAIAddTileToExistingSeriesOnBoardCommand();
        
        Collections.sort(rummComputerPlayer.getPlayerDeckTiles(), RummikubTile::compareByColorAndNumber);       
        // try to preform put series command.
        putNumberSeriesOnBoardCommand.execute(gameId, listOfRummGameMoveState, rummikubBoard, rummComputerPlayer);
        // try to preform put color set command
        putColorSeriesOnBoardCommand.execute(gameId, listOfRummGameMoveState, rummikubBoard, rummComputerPlayer);
        // try to add tile to existing set.
        /*if(rummComputerPlayer.isDidFirstValidMove())
            addTileToExistingSeriesCommand.execute(gameId, listOfRummGameMoveState, rummikubBoard, rummComputerPlayer);*/
        listOfRummGameMoveState.addAll(listOfRummGameMoveState);
                
        return listOfRummGameMoveState;
    }
    
    public void setComputerPlayers(int numOfComputerPlayers) throws RummikubPoolOutOfTilesException {
        int currentComputerNum;
        
        for(currentComputerNum = 1; currentComputerNum <= numOfComputerPlayers; currentComputerNum++){
            createNewComputerPlayer(currentComputerNum);
        }        
    }
    
    private void createNewComputerPlayer(int playerNumber) throws RummikubPoolOutOfTilesException {
        String playerName;
        
        playerName = String.format("Computer #%1$d", playerNumber);
        rummikubBoard.addNewPlayer(new RummikubComputerPlayer(playerName));
        rummikubBoard.setRandomTilesToPlayer(rummikubBoard.getRummikubPlayers().get(rummikubBoard.getRummikubPlayers().size()-1));
    }       
}
