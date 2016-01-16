/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.util.List;
import ws.rummikub.PlayerType;

public class RummikubHumanPlayer extends RummikubPlayer {
    public RummikubHumanPlayer(String playerName) {
        super(playerName);
    }
    
    public List<RummikubTile> getPlayerDeckTiles() {
        return playerDeckTiles;
    }
    
    @Override
    protected PlayerType getPlayerType() {
        return PlayerType.HUMAN;
    }
}
