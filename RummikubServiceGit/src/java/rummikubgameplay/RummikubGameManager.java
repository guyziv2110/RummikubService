/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import rummikub.service.ws.utils.RummikubUniqueNameGenerator;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubBoardValidator;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubComputerPlayer;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPlayerAdapter;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileAdatper;
import rummikubengine.RummikubTilesPositionCollisionException;
import ws.rummikub.DuplicateGameName_Exception;
import ws.rummikub.Event;
import ws.rummikub.GameDetails;
import ws.rummikub.GameDoesNotExists_Exception;
import ws.rummikub.GameStatus;
import ws.rummikub.InvalidParameters_Exception;
import ws.rummikub.PlayerStatus;
import ws.rummikub.Tile;

/**
 *
 * @author guy
 */
public class RummikubGameManager {
    private List<RummikubGameSettings> rummGamesSettings;
    //private RummikubEventsManager rummEventsManager;
    private RummikubCommandManager rummCommandManager;
    
    //create an object of SingleObject
    private static RummikubGameManager instance = new RummikubGameManager();

    private Map<Integer, RummikubGameFlowHandler> rummGameFlowHandlerMap = 
            new HashMap<Integer,RummikubGameFlowHandler>();
    //make the constructor private so that this class cannot be
    //instantiated
    private RummikubGameManager(){
        rummGamesSettings = new ArrayList<>();
        rummCommandManager = new RummikubCommandManager();
        
    }

    //Get the only object available
    public static RummikubGameManager getInstance(){
       return instance;
    }
    
    public void AddGame(String gameName,
                    int numOfHumanPlayers,
                    int numOfComputerPlayers) throws DuplicateGameName_Exception {
        if(getRummikubGameSettingsByName(gameName) != null)
        {
            throw new ws.rummikub.DuplicateGameName_Exception("Duplicate", null);
        }
        else{
            RummikubGameSettings rummGameSettings = new RummikubGameSettings(new RummikubBoard(),
                                gameName, numOfHumanPlayers, numOfComputerPlayers);
            rummGameSettings.setStatus(GameStatus.WAITING);
            rummGamesSettings.add(rummGameSettings); 
        }

    }
    
    public String AddGame(String xmlData) throws SAXException, RummikubBadTilesStructureException, FileNotFoundException, RummikubColorConvertionException, RummikubBadXMLFormatException, JAXBException {
        RummikubGameSettings rummGameSettings = 
                new RummikubGameSettings(new RummikubBoard());
        
        RummikubLoadGameCommand rummCommand = new RummikubLoadGameCommand();
        RummikubSystemCommandManager rummSysCommandManager = new RummikubSystemCommandManager();
        RummikubBoardValidator rummBoardValidator = new RummikubBoardValidator();
        
        rummGameSettings.setXmlSettingsData(xmlData);
        // execute game loading command.
        rummSysCommandManager.ExecuteCommand(rummCommand, 
                                             rummGameSettings);            

        if (!rummBoardValidator.validateBoard(rummGameSettings.getRummBoard(), true)) {
            //RummikubMessages.WriteExceptionLater("lblMessage", "The tile structure in your file is incorrect.");
        }
        
        rummGameSettings.setStatus(GameStatus.WAITING);
        rummGamesSettings.add(rummGameSettings);
        
        return rummGameSettings.getRummName();
    }    
    
    private RummikubGameSettings getRummikubGameSettingsByName(String gameName) {
        Optional<RummikubGameSettings> rummGameSettings = 
                rummGamesSettings.stream()
                        .filter(g->g.getRummName()
                        .equals(gameName)).findFirst();
        
        if(rummGameSettings.isPresent()){
            return rummGameSettings.get();
        }
        
        else {
            return null;
        }        
        
        
    }
    
    public GameDetails GetGameDetails(String gameName) 
            throws GameDoesNotExists_Exception {
        GameDetails gd = getRummikubGameSettingsByName(gameName).getGd();
        
        if(gd != null){
            return gd;
        }
        else {
            throw new ws.rummikub.GameDoesNotExists_Exception("Game does not exists!", null);
        }
    }
    
    public List<String> getWaitingGames() {
        return rummGamesSettings.
                stream().
                filter(g -> g.getStatus().equals(GameStatus.WAITING)).
                map(att -> att.getRummName()).
                collect(Collectors.toList());
        
    } 
    
    public int joinGame(String gameName, String playerName) throws GameDoesNotExists_Exception, InvalidParameters_Exception, RummikubPoolOutOfTilesException  {
        RummikubGameSettings rummGameSettings = 
                getRummikubGameSettingsByName(gameName);
        RummikubPlayer rummPlayer;
        
        if(rummGameSettings == null) 
            throw new ws.rummikub.GameDoesNotExists_Exception("Game does not exists!", null);
        if(!rummGameSettings.getStatus().equals(GameStatus.WAITING)) 
            throw new ws.rummikub.InvalidParameters_Exception("Game isn't in waiting mode", null);
        if(rummGameSettings.getRummBoard().getRummikubPlayers().
                stream().
                filter(p -> p.getPlayerName().equals(playerName) &&
                       p.getPlayerStatus() == PlayerStatus.JOINED).
                count() > 0) 
            throw new ws.rummikub.InvalidParameters_Exception("Player with the same name already exists.", null);            
        
        if(rummGameSettings.isLoadedFromXML()) {
            rummPlayer= 
                    rummGameSettings.getRummBoard().getPlayerByName(playerName);
            
            if(rummPlayer instanceof RummikubHumanPlayer 
                    && rummPlayer.getPlayerStatus() != PlayerStatus.JOINED){
                rummPlayer.setPlayerStatus(PlayerStatus.JOINED);
                
            }
            else {
                throw new ws.rummikub.InvalidParameters_Exception("Player name doesn't match to the one of the player names defined in the XML.", null);   
            }
            
        }
        else {
            rummPlayer = new RummikubHumanPlayer(playerName);
            rummPlayer.setPlayerStatus(PlayerStatus.JOINED);
            rummGameSettings.getRummBoard().getRummikubPlayers().add(rummPlayer);
            rummGameSettings.getRummBoard().setRandomTilesToPlayer(rummPlayer);
        }
        
        rummPlayer.setJoinedGame(true);
        rummGameSettings.setJoinedHumanPlayers(rummGameSettings.getJoinedHumanPlayers() + 1);
        
        //rummGameSettings.getRummBoard().getPlayersWhoJoinedGame()
        if(rummGameSettings.getJoinedHumanPlayers() ==
                    rummGameSettings.getNumOfHumanPlayers()) {
                rummGameSettings.setStatus(GameStatus.ACTIVE);
                
                if(rummGameSettings.getRummBoard().getNumOfComputerPlayers() < rummGameSettings.getNumOfComputerPlayers()) {
                    for (int currComputerIdx = 0; 
                            currComputerIdx < rummGameSettings.getNumOfComputerPlayers(); 
                            currComputerIdx++)
                    {
                        RummikubPlayer rummCompPlayer = 
                                new RummikubComputerPlayer(RummikubUniqueNameGenerator.getInstance().computerRandomIdentifier());

                        rummCompPlayer.setPlayerStatus(PlayerStatus.JOINED);
                        rummGameSettings.getRummBoard().getRummikubPlayers().add(rummCompPlayer);
                        rummGameSettings.getRummBoard().setRandomTilesToPlayer(rummCompPlayer);

                    }
                }
                
                RummikubEventsManager.getInstance().
                        addGameStartedEvent(rummGameSettings.getGameId());
                
                RummikubGameFlowHandler rummGameFlowHandler = new RummikubGameFlowHandler();
                rummGameFlowHandler.setRummikubGameSettings(rummGameSettings);
                rummGameFlowHandler.setRummikubCommandManager(rummCommandManager);
                rummGameFlowHandler.isGameStartedProperty().set(true);
                rummGameFlowHandlerMap.put(rummGameSettings.getGameId(), rummGameFlowHandler);
                rummGameFlowHandler.startGameHandler();
                

                
        }
        
        return rummPlayer.getPlayerId();
    }
    
    private RummikubGameSettings getGameWithPlayerId (int playerId)
    {
        RummikubGameSettings retRummGameSettings  = null;
        
        for(RummikubGameSettings rummGameSettings : rummGamesSettings) {
            retRummGameSettings = rummGameSettings;
            if(rummGameSettings.getRummBoard().getPlayerById(playerId) != null) 
                break;
        }
        
        return retRummGameSettings;
    }
    
    public ws.rummikub.PlayerDetails getPlayerDetails(int playerId) 
            throws ws.rummikub.GameDoesNotExists_Exception, 
            ws.rummikub.InvalidParameters_Exception,
            RummikubColorConvertionException {
        
        RummikubPlayer rummPlayer = null;
        
        for(RummikubGameSettings rummGameSettings : rummGamesSettings) {
            rummPlayer = rummGameSettings.getRummBoard().getPlayerById(playerId);
            if(rummPlayer != null) break;
        }
   
        return new RummikubPlayerAdapter().getPlayerDetails(rummPlayer);     
    }    
    
    public List<ws.rummikub.PlayerDetails> getPlayersDetails(String gameName) 
            throws ws.rummikub.GameDoesNotExists_Exception, RummikubColorConvertionException {
        List<ws.rummikub.PlayerDetails> playerDetailsList = new LinkedList<>();
                
        RummikubGameSettings rummGameSettings = 
                getRummikubGameSettingsByName(gameName);
        
        if(rummGameSettings == null) 
            throw new ws.rummikub.GameDoesNotExists_Exception("Game does not exists!", null);
        
        for(RummikubPlayer rummPlayer : rummGameSettings.getRummBoard().getRummikubPlayers()){
            playerDetailsList.add(new RummikubPlayerAdapter().getPlayerDetails(rummPlayer));
        }
        
        return playerDetailsList;
    }        
    
    public List<ws.rummikub.Event> getEvents(int playerId, int eventId) throws InvalidParameters_Exception {
        RummikubGameSettings rummGame = getGameWithPlayerId(playerId);
        return RummikubEventsManager.getInstance().getEvents(rummGame.getGameId(), eventId);
    }        

    public void addTile(int playerId, Tile tile, int sequenceIndex, int sequencePosition) 
            throws InvalidParameters_Exception {
        try {
            RummikubGameSettings rummGame = getGameWithPlayerId(playerId);
            /*
            try {
            rummGame.
            getRummBoard().
            addTileBySequenceIndexAndPosition(playerId,
            new RummikubTileAdatper().getRummikubTile(tile),
            sequenceIndex,
            sequencePosition);
            } catch (RummikubTilesPositionCollisionException ex) {
            throw new ws.rummikub.InvalidParameters_Exception("Tiles colision", null);
            } catch (RummikubColorConvertionException ex) {
            throw new ws.rummikub.InvalidParameters_Exception("Color Conversion error", null);
            }*/
            
            RummikubTile rummTile = null;
            RummikubCommand command;
            
            try {
                rummTile = new RummikubTileAdatper().
                        getRummikubTile(rummGame.getRummBoard().getPlayerById(playerId), 
                                        tile);
            } catch (RummikubColorConvertionException ex) {
                Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            command = new RummikubPutTileOnBoardCommand(
                    rummTile,
                    sequenceIndex,
                    sequencePosition);
            
            rummCommandManager.executeCommand(command,
                    rummGame.getGameId(),
                    rummGame.getRummBoard(),
                    rummGame.getRummBoard().getPlayerById(playerId));
            RummikubEventsManager.getInstance().
                    addTileAddedEvent(rummGame.getGameId(), 
                                      rummGame.getRummBoard().getPlayerById(playerId).getPlayerName(), 
                                      new Tile[]{tile},
                                      -1, 
                                      -1, 
                                      sequenceIndex, 
                                      sequencePosition);
            
        } catch (RummikubInvalidMoveException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubPoolOutOfTilesException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubTilesPositionCollisionException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void finishTurn(int playerId) {
        try {
            RummikubGameSettings rummGame = getGameWithPlayerId(playerId);
            rummGameFlowHandlerMap.get(rummGame.getGameId()).endTurn(playerId);
        } catch (RummikubInvalidMoveException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubPoolOutOfTilesException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubTilesPositionCollisionException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void moveTile(int playerId, 
                         int sourceSequenceIndex, 
                         int sourceSequencePosition, 
                         int targetSequenceIndex, 
                         int targetSequencePosition) throws InvalidParameters_Exception  {
        try {
            RummikubGameSettings rummGame = getGameWithPlayerId(playerId);
            
            RummikubTile rummTile = null;
            RummikubCommand command;
            
            rummTile = rummGame.getRummBoard().getTileByLineAndColumn(sourceSequenceIndex, sourceSequencePosition);           
            if (rummTile == null) 
                throw new ws.rummikub.InvalidParameters_Exception("Tile doesn't not exist!", null);
            
            Tile tile = new RummikubTileAdatper().getWSTileFromRummTile(rummTile);
            
            command = new RummikubMoveTileOnBoardCommand(sourceSequenceIndex, sourceSequencePosition, targetSequenceIndex, targetSequencePosition);
            
            rummCommandManager.executeCommand(command,
                    rummGame.getGameId(),
                    rummGame.getRummBoard(),
                    rummGame.getRummBoard().getPlayerById(playerId));
            RummikubEventsManager.getInstance().
                    addTiledMovedEvent(rummGame.getGameId(), 
                                      rummGame.getRummBoard().getPlayerById(playerId).getPlayerName(), 
                                      new Tile[]{tile},
                                      sourceSequenceIndex, 
                                      sourceSequencePosition, 
                                      targetSequenceIndex, 
                                      targetSequencePosition);
            
        } catch (RummikubInvalidMoveException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubPoolOutOfTilesException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubTilesPositionCollisionException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubGameManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void resign(int playerId) {
        RummikubGameSettings rummGame = getGameWithPlayerId(playerId);     
        rummGameFlowHandlerMap.get(rummGame.getGameId()).resign(playerId);
        

    }

}
