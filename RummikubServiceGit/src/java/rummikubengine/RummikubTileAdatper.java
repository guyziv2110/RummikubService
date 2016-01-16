/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import rummikubgameplay.UI.RummikubBoardUI;
import ws.rummikub.PlayerDetails;
import ws.rummikub.Tile;

/**
 *
 * @author guy
 */
public class RummikubTileAdatper {
     //private RummikubTile rummTile;
     
     public Tile getWSTileFromRummTile(RummikubTile rummTile) 
             throws RummikubColorConvertionException {
        Tile t = new Tile();
        try {
            t.setColor(rummTile.getTileColor().convertToWSColor());
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
        }
       t.setValue(rummTile.getTileNumber().getNumber());
       return t;
     }
     
     public RummikubTile getRummikubTile(RummikubPlayer rummPlayer, Tile t) 
             throws RummikubColorConvertionException {
         
             List<RummikubTile> playersCorrespondingTiles = rummPlayer.getPlayerDeckTiles().stream().
                     filter(tile -> tile.getTileNumber().getNumber() == t.getValue() &&
                             tile.getTileColor().toString().equals(t.getColor().toString())).
                     collect(Collectors.toList());
             
             if(!playersCorrespondingTiles.isEmpty()) return playersCorrespondingTiles.get(0);
             else return null;
             
             /*rummTile = new RummikubTile(RummikubTileColor.valueOf(t.getColor().value()),
                                        RummikubTileNumber.values()[t.getValue() - 1], 
                                         -1, 
                                         -1, 
                                         RummikubTileState.ONDECK);*/
         
         //return rummTile;
     }
}
