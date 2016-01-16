/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubGameMoveState;
import static rummikubengine.RummikubMethodsExtensions.distinctByKey;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileNumber;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubBoardUI;

public class RummikubAIPutColorSeriesOnBoardCommand implements RummikubAICommand {
    private RummikubBoard commandOnBoard;
    private RummikubPlayer commandOnPlayer;
    private int gameId;
    
    @Override
    public void execute(int gameId,
                        List<RummikubGameMoveState> gameMoveStates,
                        RummikubBoard rummBoard, 
                        RummikubPlayer rummPlayer) throws RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException {
        commandOnBoard = rummBoard;
        commandOnPlayer = rummPlayer;
        this.gameId = gameId;
        gameMoveStates.addAll(findColorSetMatchesToPutOnBoard());
    }
    
     private List<RummikubGameMoveState> findColorSetMatchesToPutOnBoard() throws RummikubTilesPositionCollisionException {
        List<RummikubGameMoveState> gameMoveStates = new ArrayList<RummikubGameMoveState>();
        List<RummikubTile> setList;
        
        Map<RummikubTileNumber, List<RummikubTile>> rummNumToRummTileList = 
                commandOnPlayer.getPlayerDeckTiles().stream().
                        collect(Collectors.groupingBy(RummikubTile::getTileNumber));
         
        for (Map.Entry<RummikubTileNumber, List<RummikubTile>> sameNumberTilesEntry : 
                    rummNumToRummTileList.entrySet())
        {
           setList =   new LinkedList<>(sameNumberTilesEntry.getValue().stream().
                        filter(distinctByKey(p -> p.getTileColor())).
                        collect(Collectors.toList()));
           if (setList.size() == 4 || setList.size() == 3) {
               if(RummikubAICommand.isValidMovePoints(setList, commandOnBoard, commandOnPlayer)) {
                    RummikubAICommand.putListOfTilesOnBoard(gameId, setList, commandOnBoard, commandOnPlayer);
                    gameMoveStates.add(new RummikubGameMoveState(commandOnBoard));           
               }
           }
        } 
        
        return gameMoveStates;
    }    
}

