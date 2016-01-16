/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileAdatper;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;
import ws.rummikub.Tile;

public class RummikubMoveTileOnBoardCommand implements RummikubCommand{
    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private RummikubTile rummTile;
    private RummikubTile oldRummTile;
    private SimpleBooleanProperty commandDone;
    private final int tileFromLine;
    private final int tileFromCol;
    private final int tileToLine;
    private final int tileToCol;
    private RummikubBoardUI rummBoardUI;    
    private int gameId;
    
    @Override
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }  
    
    @Override
    public void execute(int gameId,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws RummikubTilesPositionCollisionException, IOException {
        
        commandOnBoard = rummBoard; 
        this.gameId = gameId;
        this.commandOnPlayer = rummPlayer;
        executeMoveRequest();
    }

    @Override
    public void undo() {
        if(rummTile != null) {
            try {
                /*this.rummBoardUI.replaceNodeRowAndCol(rummTile.getTileLine(),
                rummTile.getTileColumn(),
                oldRummTile.getTileLine(),
                oldRummTile.getTileColumn());*/
                Tile tile = new RummikubTileAdatper().getWSTileFromRummTile(rummTile);
                RummikubEventsManager.getInstance().
                        addTiledMovedEvent(gameId,
                                "",
                                new Tile[]{tile},
                                tileToLine,
                                tileToCol,
                                tileFromLine,
                                tileFromCol);
            } catch (RummikubColorConvertionException ex) {
                Logger.getLogger(RummikubMoveTileOnBoardCommand.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            rummTile.copyRummikubTile(oldRummTile);
        }
    }
    
    public RummikubMoveTileOnBoardCommand(int tileFromRow, int tileFromCol, int tileToRow, int tileToCol) {
        this.tileFromLine = tileFromRow;
        this.tileFromCol = tileFromCol;
        this.tileToLine = tileToRow;
        this.tileToCol = tileToCol;
    }    

    private void executeMoveRequest() throws 
            IOException, RummikubTilesPositionCollisionException {
        
        RummikubTile fromRummTile, toRummTile;
        boolean moveRequestValid;
        
        fromRummTile = commandOnBoard.getTileByLineAndColumn(tileFromLine, tileFromCol);
        toRummTile = commandOnBoard.getTileByLineAndColumn(tileToLine, tileToCol);
        moveRequestValid = testMoveRequest(fromRummTile, toRummTile);

        if(moveRequestValid) {
            rummTile = fromRummTile;
            oldRummTile =  new RummikubTile(rummTile);
            commandOnBoard.moveTileToLineAndCol(rummTile, tileToCol, tileToLine);
        }        
    }

    private boolean testMoveRequest(RummikubTile fromRummTile, RummikubTile toRummTile) {
        if(fromRummTile != null && toRummTile == null) {
            return true;
        }
        
        else return false; 
    }
}
