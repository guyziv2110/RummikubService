/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javafx.beans.property.SimpleBooleanProperty;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubgameplay.jaxbobjects.Rummikub;
import javax.xml.bind.Marshaller;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubComputerPlayer;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubTile;
import rummikubgameplay.jaxbobjects.Board;
import rummikubgameplay.jaxbobjects.Board.Sequence;
import rummikubgameplay.jaxbobjects.PlayerType;
import rummikubgameplay.jaxbobjects.Players;
import rummikubgameplay.jaxbobjects.Tile;

public class RummikubSaveGameCommand implements RummikubSystemCommand{
    private RummikubGameSettings rummGameSettings;
    private SimpleBooleanProperty commandDone = new SimpleBooleanProperty(false);;;
     
    public final SimpleBooleanProperty commandDone(){
        return commandDone;
    } 
    
    @Override
    public void execute(RummikubGameSettings rummGameSettings) throws SAXException, RummikubBadTilesStructureException, FileNotFoundException, RummikubColorConvertionException, JAXBException {
        this.rummGameSettings = rummGameSettings;
        OutputStream xmlOutputStream = new FileOutputStream(new File(rummGameSettings.getSettingsFilePath()));
        JAXBContext context = JAXBContext.newInstance(Rummikub.class);
        Marshaller m = context.createMarshaller();
        Rummikub rumm = new Rummikub();
        copyCurrentInstanceToRumm(rumm);
        m.marshal(rumm, xmlOutputStream);
        commandDone.set(true);
    }

    private void copyCurrentInstanceToRumm(Rummikub rumm) throws RummikubColorConvertionException {
        copyGameSettingsSequenceToBoard(rumm);
        copyGameSettingsPlayersToRumm(rumm);
        copyGameSettingsInfoToRumm(rumm);
    }

    private void copyGameSettingsSequenceToBoard(Rummikub rumm) throws RummikubColorConvertionException {
        Board b = new Board();
        Sequence seq;
        Tile t;
        
        int line;
        for(line = 0; line < rummGameSettings.getRummBoard().getMaxBoardLines(); line++) {
            List<List<RummikubTile>> rummSets = new ArrayList<List<RummikubTile>>();
            List<RummikubTile> rummSetsInLine = rummGameSettings.getRummBoard().getRummikubTilesInLineK(line);
            
            if(rummSetsInLine.isEmpty()) break;
            
            Collections.sort(rummSetsInLine, RummikubTile::compareByLColumnNumber);
            int i;
            int currentSet = 0;
            rummSets.add(new ArrayList<>());
            rummSets.get(currentSet).add(rummSetsInLine.get(0));

            for (i = 0; i < rummSetsInLine.size() - 1; i++) 
            {
                if(rummSetsInLine.get(i).getTileColumn() == rummSetsInLine.get(i+1).getTileColumn() - 1) {
                    rummSets.get(currentSet).add(rummSetsInLine.get(i+1));
                }
                else {
                    currentSet++;
                    rummSets.add(new ArrayList<>());  
                    rummSets.get(currentSet).add(rummSetsInLine.get(i + 1));
                }
            }

            for(List<RummikubTile> rummTileList : rummSets) {
                seq = new Sequence();
                for (RummikubTile rummTile : rummTileList) {
                   t = new Tile();
                   t.setColor(rummTile.getTileColor().convertToColor());
                   t.setValue(rummTile.getTileNumber().getNumber());
                   seq.getTile().add(t);
               }                
                b.getSequence().add(seq);
            } 
        }
        
        rumm.setBoard(b);
    }

    private void copyGameSettingsPlayersToRumm(Rummikub rumm) throws RummikubColorConvertionException {
        Tile t;
        Players.Player.Tiles tiles;
        Players players = new Players();
        
        List<RummikubPlayer> rummPlayers =  rummGameSettings.getRummBoard().
                getRummikubPlayers();
        
        for(RummikubPlayer rummPlayer : rummPlayers) {
            Players.Player player = new Players.Player();
            player.setName(rummPlayer.getPlayerName());
            if (rummPlayer instanceof RummikubHumanPlayer)
                 player.setType(PlayerType.HUMAN);
            else if (rummPlayer instanceof RummikubComputerPlayer)
                 player.setType(PlayerType.COMPUTER);
            
            if(rummPlayer.isIsPlayerTurn()) rumm.setCurrentPlayer(player.getName());  
            
            tiles = new Players.Player.Tiles();
            
            for(RummikubTile rummTile : rummPlayer.getPlayerDeckTiles()) {
                t = new Tile();
                t.setColor(rummTile.getTileColor().convertToColor());
                t.setValue(rummTile.getTileNumber().getNumber());                
                tiles.getTile().add(t);
            }
           
            player.setPlacedFirstSequence(rummPlayer.isDidFirstValidMove());
            player.setTiles(tiles);
            players.getPlayer().add(player);
        }
        rumm.setPlayers(players);        
    }

    private void copyGameSettingsInfoToRumm(Rummikub rumm) {
        rumm.setName(rummGameSettings.getRummName());
        
    }




    
}
