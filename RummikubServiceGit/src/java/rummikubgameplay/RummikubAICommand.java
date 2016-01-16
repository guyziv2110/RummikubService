/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubGameMoveState;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileAdatper;
import rummikubengine.RummikubTileNumber;
import rummikubengine.RummikubTilePosition;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;
import ws.rummikub.Tile;


public interface RummikubAICommand {
    public void execute(int gameId,
                        List<RummikubGameMoveState> gameMoveStates,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws RummikubTilesPositionCollisionException, RummikubPoolOutOfTilesException;   
    
    public static void putListOfTilesOnBoard(int gameId,
                                       List<RummikubTile> rummTiles, 
                                       RummikubBoard rummBoard,
                                       RummikubPlayer rummPlayer) throws RummikubTilesPositionCollisionException {
        
        RummikubTilePosition emptySpaceOnBoard = rummBoard.getEmptySpaceForTilesOnBoard(rummTiles.size());
        int emptyLineOnBoard = emptySpaceOnBoard.getRow();
        int currentCol = emptySpaceOnBoard.getCol();
        
        for (RummikubTile rummTile : rummTiles) {
            rummBoard.moveTileToBoard(rummTile, rummPlayer, emptyLineOnBoard, currentCol);
            
            Tile tile = null;
            try {
                tile = new RummikubTileAdatper().getWSTileFromRummTile(rummTile);
            } catch (RummikubColorConvertionException ex) {
                Logger.getLogger(RummikubAIAddTileToExistingSeriesOnBoardCommand.class.getName()).log(Level.SEVERE, null, ex);
            }            
            
            RummikubEventsManager.getInstance().addTileAddedEvent(
                gameId, 
                rummPlayer.getPlayerName(), 
                new Tile[]{tile},
                -1, 
                -1, 
                emptyLineOnBoard, 
                currentCol);            
            //rummBoardUI.animateMoveTileToBoard(rummTile, emptyLineOnBoard, currentCol, true);
            rummPlayer.addPoints(rummTile.getTileNumber().getNumber());
            currentCol++;
        }            
    }  
    
    public static boolean isValidMovePoints(List<RummikubTile> rummTiles, 
                                           RummikubBoard rummBoard,
                                           RummikubPlayer rummPlayer) {
        int movePoints = 0;
        int currPoints = 0; 
        if (!rummPlayer.isDidFirstValidMove()) {
            for (RummikubTile rummTile : rummTiles)
            {
                currPoints = rummTile.getTileNumber().getNumber();
                if(currPoints ==  RummikubTileNumber.JOKER.getNumber())
                    currPoints= calculateJokerPointsBySet(rummTiles);
               
                movePoints += currPoints;
            }

            if (movePoints < 30) return false;
            else {
                rummPlayer.setDidFirstValidMove(true);
            }
        }        

        return true;
    }
    
    public static int calculateJokerPointsBySet(List<RummikubTile> rummTiles) {
        if(rummTiles.get(0).getTileNumber() == rummTiles.get(1).getTileNumber()){
            return rummTiles.get(0).getTileNumber().getNumber();
        }
        else if (rummTiles.get(rummTiles.size()-1).getTileNumber().getNumber() < RummikubTileNumber.T13.getNumber()) {
            return rummTiles.get(rummTiles.size()-1).getTileNumber().getNumber() + 1;
        }
        else return rummTiles.get(0).getTileNumber().getNumber() - 1;
    }
}


