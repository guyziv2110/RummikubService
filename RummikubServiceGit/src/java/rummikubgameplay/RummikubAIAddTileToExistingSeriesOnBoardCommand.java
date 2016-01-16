/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubBoardValidator;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubGameMoveState;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileAdatper;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;
import ws.rummikub.Tile;

// we will use the game state in this class for future uses to make
// the AI smarter.

public class RummikubAIAddTileToExistingSeriesOnBoardCommand implements RummikubAICommand {
    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private RummikubTile oldRummTile;
    private List<RummikubGameMoveState> commandOnGameMoveStates;
    private RummikubBoardValidator rummikubBoardValidator;
    private int gameId;
    
    public RummikubAIAddTileToExistingSeriesOnBoardCommand() {
        this.rummikubBoardValidator = new RummikubBoardValidator();
    }
    
    @Override
    public void execute(int gameId,
                        List<RummikubGameMoveState> gameMoveStates, 
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) 
            throws RummikubTilesPositionCollisionException, 
                   RummikubPoolOutOfTilesException {
        commandOnBoard = rummBoard;
        commandOnPlayer = rummPlayer;
        this.gameId = gameId;
        gameMoveStates.addAll(findSeriesToAddMatchingTile());
    }
    
    private List<RummikubGameMoveState> findSeriesToAddMatchingTile() throws RummikubTilesPositionCollisionException {
        List<RummikubGameMoveState> listOfRummGameMoveState = new ArrayList<RummikubGameMoveState>();
        List<RummikubTile> currentRummTiles = new ArrayList<> (commandOnPlayer.getPlayerDeckTiles());
        int maxBoardLines = commandOnBoard.getMaxBoardLines();
        RummikubGameMoveState rummGameMoveState = null;
        int i;
        
        // AI looking for a row to add a comptaible tile using the validator.
        if (commandOnBoard.getMaxBoardLines() == -1) return listOfRummGameMoveState;
        for (RummikubTile compTile : currentRummTiles) {
            oldRummTile = new RummikubTile(compTile);
            
            for (i = 0; i < maxBoardLines; i++) {
                rummGameMoveState = new  RummikubGameMoveState(commandOnBoard);
                addTileToExistingLine(compTile, i);
                Collections.sort(rummGameMoveState.getRummBoard().getRummikubTiles(), RummikubTile::compareByColorAndNumber);
                if(!rummikubBoardValidator.validateBoard(rummGameMoveState.getRummBoard(), true)) {
                     // undo if board is not valid.
                     removeAddedTile(compTile);
                }            
                else {
                    commandOnBoard.arrangeLineByColumn(i);
                    listOfRummGameMoveState.add(rummGameMoveState);
                    break;
                }
            }
        }        
        
        return listOfRummGameMoveState;
    }

    private void addTileToExistingLine(RummikubTile compTile, int line) throws RummikubTilesPositionCollisionException{
        int emptyCol = commandOnBoard.getEmptyColOnLine(line);
        commandOnBoard.moveTileToBoard(compTile, 
                    commandOnPlayer, 
                    line,
                    emptyCol);
        
        Tile tile = null;
        try {
            tile = new RummikubTileAdatper().getWSTileFromRummTile(compTile);
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubAIAddTileToExistingSeriesOnBoardCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        RummikubEventsManager.getInstance().addTileAddedEvent(
                gameId, 
                commandOnPlayer.getPlayerName(), 
                new Tile[]{tile},
                -1, 
                -1, 
                line, 
                emptyCol);
          
        //rummBoardUI.animateMoveTileToBoard(compTile, line, emptyCol, false);
    }    

    private void removeAddedTile(RummikubTile compTile) {
        try {
            RummikubEventsManager.getInstance().addTileReturnedEvent(
                    gameId,
                    commandOnPlayer.getPlayerName(),
                    new RummikubTileAdatper().getWSTileFromRummTile(compTile),
                    compTile.getTileLine(),
                    compTile.getTileColumn());
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubAIAddTileToExistingSeriesOnBoardCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //rummBoardUI.animateRemoveTileFromBoard(compTile);        
        compTile.copyRummikubTile(oldRummTile);
        commandOnBoard.addTileToPlayer(compTile, commandOnPlayer);
    }
}
