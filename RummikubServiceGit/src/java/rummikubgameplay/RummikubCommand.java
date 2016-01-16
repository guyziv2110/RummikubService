/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.IOException;
import javafx.beans.property.SimpleBooleanProperty;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;

/**
 *
 * @author guy
 */
public interface RummikubCommand {

    public void execute(int gameId,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws 
            RummikubInvalidMoveException, 
            RummikubPoolOutOfTilesException, 
            RummikubTilesPositionCollisionException, 
            IOException;

    public void undo();
    
    public SimpleBooleanProperty commandDone();
    
    
}
