/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import ws.rummikub.Color;
import ws.rummikub.PlayerDetails;
import ws.rummikub.Tile;

/**
 *
 * @author guy
 */
public class RummikubPlayerAdapter {
     private PlayerDetails pd;
     
     public PlayerDetails getPlayerDetails(RummikubPlayer rummPlayer) throws RummikubColorConvertionException {
         pd = new PlayerDetails();
         pd.setName(rummPlayer.getPlayerName());
         pd.setNumberOfTiles(rummPlayer.getPlayerDeckTiles().size());
         pd.setPlayedFirstSequence(rummPlayer.isDidFirstValidMove());
         pd.setStatus(rummPlayer.getPlayerStatus());
         pd.setType(rummPlayer.getPlayerType());
         
         for (RummikubTile rummTile : rummPlayer.getPlayerDeckTiles()) {
             Tile t = new Tile();
             t.setColor(rummTile.getTileColor().convertToWSColor());
             t.setValue(rummTile.getTileNumber().getNumber());
             pd.getTiles().add(t);
         }
         
         return pd;
     }
    
}
