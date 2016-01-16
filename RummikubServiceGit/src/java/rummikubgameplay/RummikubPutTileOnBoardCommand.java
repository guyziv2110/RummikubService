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
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileAdatper;
import rummikubengine.RummikubTileColorAdapter;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;
import rummikubgameplay.UI.RummikubMessagesUI;

public class RummikubPutTileOnBoardCommand implements RummikubCommand {

    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private RummikubBoardUI rummBoardUI;
    private RummikubTile rummTile;
    private RummikubTile oldRummTile;
    private final int tileToRow;
    private final int tileToCol;
    private SimpleBooleanProperty commandDone;
    private int gameId;
    
    @Override
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }   
    
    @Override
    public void execute(int gameId,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws 
            RummikubInvalidMoveException, 
            RummikubInvalidMoveException, 
            RummikubTilesPositionCollisionException, 
            IOException
    {
     
        commandOnBoard = rummBoard;
        commandOnPlayer = rummPlayer;
        commandDone = new SimpleBooleanProperty(false); 
        this.gameId = gameId;
        executePutRequest();  
    }

    @Override
    public void undo() {
        int lastIndexOnBoard = rummTile.getTileLine();
        int lastPositionOnBoard = rummTile.getTileColumn();
        rummTile.copyRummikubTile(oldRummTile);
        commandOnBoard.addTileToPlayer(rummTile, commandOnPlayer);
        try {
            //rummBoardUI.removeNodeByRowAndIndex(tileToRow, tileToCol);
            RummikubEventsManager.getInstance().addTileReturnedEvent(
                    gameId,
                    commandOnPlayer.getPlayerName(),
                    new RummikubTileAdatper().getWSTileFromRummTile(rummTile),
                    lastIndexOnBoard,
                    lastPositionOnBoard);
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubPutTileOnBoardCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
        //addTileReturnedEvent
        commandOnPlayer.removePoints(rummTile.getTileNumber().getNumber());
    }
    
    public RummikubPutTileOnBoardCommand(RummikubTile t, int tileToRow, int tileToCol) {
        //rummTile = commandOnPlayer.getTileByLineAndColumn(lineTileInDeck, colTileInDeck); 
        rummTile = t;
        this.tileToRow = tileToRow;
        this.tileToCol = tileToCol;
    }
    
    private void executePutRequest() throws IOException, RummikubInvalidMoveException, RummikubTilesPositionCollisionException {
        //rummTile = commandOnPlayer.getTileByLineAndColumn(lineTileInDeck, colTileInDeck);
        oldRummTile = new RummikubTile(rummTile);               
        commandOnPlayer.addPoints(rummTile.getTileNumber().getNumber());
        try {                            
              commandOnBoard.moveTileToBoard(rummTile, commandOnPlayer, tileToRow, tileToCol);
              commandDone.set(true);
        } catch (RummikubTilesPositionCollisionException ex) {
         RummikubMessagesUI.showMessage("Unable to move tile to the destination position: there is already tile in the destination position.");
        }
    }
}
