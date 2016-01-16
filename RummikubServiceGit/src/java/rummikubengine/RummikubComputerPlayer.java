/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import ws.rummikub.PlayerType;

public class RummikubComputerPlayer extends RummikubPlayer {

    public RummikubComputerPlayer(String playerName) {
        super(playerName);
    }

    public RummikubComputerPlayer(RummikubPlayer rummPlayer) {
        super(rummPlayer);
    }
    
    @Override
    protected PlayerType getPlayerType() {
        return PlayerType.COMPUTER;
    }    
}
