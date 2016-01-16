/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import rummikubgameplay.jaxbobjects.Players;
import rummikubgameplay.jaxbobjects.Rummikub;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import javax.xml.XMLConstants;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubComputerPlayer;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileState;
import rummikubgameplay.jaxbobjects.Board;
import rummikubgameplay.jaxbobjects.Board.Sequence;
import rummikubgameplay.jaxbobjects.PlayerType;
import rummikubgameplay.jaxbobjects.Players.Player;
import rummikubgameplay.jaxbobjects.Tile;

public class RummikubLoadGameCommand implements RummikubSystemCommand {
    private static final String RESOURCES = "resources";
    private RummikubGameSettings rummGameSettings;
    private final SimpleBooleanProperty loadFinished = new SimpleBooleanProperty(false);;
    
    public final SimpleBooleanProperty loadFinished(){
        return loadFinished;
    } 
    
    @Override
    public void execute(RummikubGameSettings rummGameSettings) throws 
            SAXException, 
            RummikubBadTilesStructureException, 
            UnmarshalException, 
            FileNotFoundException, 
            RummikubBadXMLFormatException, 
            JAXBException{
        
        
        this.rummGameSettings = rummGameSettings;

        URL csdURL = RummikubLoadGameCommand.class.getResource("/" + RESOURCES + "/" + "rummikub.xsd");
        SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
        Schema schema = schemaFactory.newSchema(csdURL);

        //get the XML content
        //InputStream xmlInputStream = new FileInputStream(rummGameSettings.getSettingsFilePath());
        InputStream xmlInputStream = new ByteArrayInputStream(rummGameSettings.getXmlSettingsData().getBytes(StandardCharsets.UTF_8));
        
        JAXBContext context = JAXBContext.newInstance(Rummikub.class);
        Unmarshaller unmarshaller = context.createUnmarshaller();
        //attach the Schema to the unmarshaller so it will use it to run validations
        //on the content of the XML
        unmarshaller.setSchema(schema);

        Rummikub rummikub = (Rummikub) unmarshaller.unmarshal(xmlInputStream);

        copyBoardToRummGameSettings(rummikub.getBoard());
        copyPlayersToRummGameSettings(rummikub.getPlayers());
        setGameSettingsInfo(rummikub);
       loadFinished.set(true);
 
    }

    private void copyBoardToRummGameSettings(Board board) throws RummikubBadXMLFormatException {
        List<Board.Sequence> boardSequence = board.getSequence();
        int line = 0;
        int col = 0;
        
        for (Sequence seq : boardSequence) {
            
            RummikubEventsManager.getInstance().
                    addSequenceCreatedEvent(rummGameSettings.getRummBoard(),
                                            rummGameSettings.getGameId(), 
                                            seq.getTile().toArray(new Tile[seq.getTile().size()]));
            
            for (Tile tile : seq.getTile()) {                
                Optional<RummikubTile> rummTile = 
                        rummGameSettings.getRummBoard().getRummikubTiles().
                        stream().
                        filter(t -> t.getTileNumber().getNumber() == tile.getValue() &&
                                    t.getTileColor().name() == tile.getColor().value() &&
                                    t.getTileState() == RummikubTileState.ONBOARDFACEDOWN).
                                    findFirst();
                
                if(rummTile.isPresent()) {
                    rummTile.get().setTileLine(line);
                    rummTile.get().setTileColumn(col);
                    rummTile.get().setTileState(RummikubTileState.ONBOARDFACEUP);
                    
                    
                    
                }
                else {
                    throw new RummikubBadXMLFormatException();
                }
                
                col++;
            }
            

            
            col = 0;
            line++;
        }
    }

    private void copyPlayersToRummGameSettings(Players players) throws RummikubBadXMLFormatException {
        RummikubPlayer rummPlayer = null;
        
        for (Player player : players.getPlayer()) {
            if (player.getType() == PlayerType.HUMAN)
            {
                this.rummGameSettings.setNumOfHumanPlayers(this.rummGameSettings.getNumOfHumanPlayers() + 1);
                 rummPlayer = new RummikubHumanPlayer(player.getName());
            }
            else if (player.getType() == PlayerType.COMPUTER) {
                this.rummGameSettings.setNumOfComputerPlayers(this.rummGameSettings.getNumOfComputerPlayers() + 1);
                 rummPlayer = new RummikubComputerPlayer(player.getName());
            }
            else {
                throw new RummikubBadXMLFormatException();
            }
            
            this.rummGameSettings.getRummBoard().addNewPlayer(rummPlayer);
            for (Tile tile : player.getTiles().getTile()) {
                Optional<RummikubTile> rummTile = 
                        rummGameSettings.getRummBoard().getRummikubTiles().
                        stream().
                        filter(t -> t.getTileNumber().getNumber() == tile.getValue() &&
                                    t.getTileColor().name().equals(tile.getColor().value()) &&
                                    t.getTileState() == RummikubTileState.ONBOARDFACEDOWN).
                                    findFirst();
                
                if(rummTile.isPresent()) {
                    rummTile.get().setTileState(RummikubTileState.ONDECK);
                    this.rummGameSettings.getRummBoard().addTileToPlayer(rummTile.get(), rummPlayer);
                }
                else {
                    throw new RummikubBadXMLFormatException();
                }
            }
        }     
    }

    private void setCurrentPlayerTurn(String playerName) throws RummikubBadXMLFormatException {
        Optional<RummikubPlayer> rummPlayer = rummGameSettings.getRummBoard().
                                              getRummikubPlayers().
                                              stream().
                                              filter(p->p.getPlayerName().equals(playerName)).
                                              findFirst();
        
        if(rummPlayer.isPresent()) {
            rummPlayer.get().setIsPlayerTurn(true);
        }
        else {
            throw new RummikubBadXMLFormatException();
        }
    }

    private void setGameSettingsInfo(Rummikub rummikub) throws RummikubBadXMLFormatException {
        setCurrentPlayerTurn(rummikub.getCurrentPlayer());
        setComputerPlayingInfo();
        rummGameSettings.setRummName(rummikub.getName());
        rummGameSettings.setLoadedFromXML(true);
    }

    private void setComputerPlayingInfo() {
        if(rummGameSettings.getRummBoard().getRummikubTiles().stream()
                .filter(t->t.getTileState() == RummikubTileState.ONBOARDFACEDOWN).count() == 0)
        {
            for (RummikubPlayer rummPlayer : rummGameSettings.getRummBoard().getRummikubPlayers()){
                if(rummPlayer instanceof RummikubComputerPlayer) rummPlayer.setPlaying(false);
            }
        }
    }

}
    
