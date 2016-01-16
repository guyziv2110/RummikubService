/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;

public class RummikubCommandManager {
    private final List<RummikubCommand> rummCommandsHistory;

    public RummikubCommandManager() {
        this.rummCommandsHistory = new ArrayList<RummikubCommand>();
    }
    
    public void executeCommand(RummikubCommand rummCommand,
                               int gameId,
                               RummikubBoard rummBoard, 
                               RummikubPlayer rummPlayer) throws RummikubInvalidMoveException, RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException, IOException{
        rummCommandsHistory.add(rummCommand);
        rummCommand.execute(gameId, rummBoard, rummPlayer);
    }    
    
    public void undoCommands() {
        Collections.reverse(rummCommandsHistory);
        
        for (RummikubCommand rummCommand : rummCommandsHistory) {
            rummCommand.undo();
        }   
    }

    public void undoLastCommand() {
        Collections.reverse(rummCommandsHistory);
        if (rummCommandsHistory.size() > 0)
            rummCommandsHistory.get(0).undo();
    }
    
    public void clearCommands() {
        rummCommandsHistory.clear();
    }
    
    public RummikubCommand getLastCommand() {
        if (rummCommandsHistory.size() > 0)
            return rummCommandsHistory.get(rummCommandsHistory.size() - 1);
        return null;
    }    
}
