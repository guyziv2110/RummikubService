/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import javafx.beans.property.SimpleBooleanProperty;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubgameplay.UI.RummikubBoardUI;

public class RummikubPickTileFromDeckCommand implements RummikubCommand {

    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private RummikubTile rummTile;
    private RummikubTile oldRummTile;
    private SimpleBooleanProperty commandDone;
    
    @Override
    public void execute(int gameId,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws RummikubPoolOutOfTilesException{
        
        commandOnBoard = rummBoard;
        commandOnPlayer = rummPlayer;
        rummTile = rummBoard.getSingleRummikubTile();
        oldRummTile = new RummikubTile(rummTile);
        rummBoard.addTileToPlayer(rummTile, commandOnPlayer);
    }

    @Override
    public void undo() {
        rummTile.copyRummikubTile(oldRummTile);
        commandOnBoard.removeTileToPlayer(rummTile, commandOnPlayer);
    }
    
    @Override
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }      
}
