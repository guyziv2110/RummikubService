/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikub.service.ws;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubgameplay.RummikubGameManager;
import ws.rummikub.GameDoesNotExists_Exception;
import ws.rummikub.InvalidParameters_Exception;
import ws.rummikub.InvalidXML;
import ws.rummikub.InvalidXML_Exception;
import ws.rummikub.RummikubFault;

/**
 *
 * @author guy
 */
public class RummikubWebServiceHandler {
    private static RummikubWebServiceHandler instance = new RummikubWebServiceHandler();

    //make the constructor private so that this class cannot be
    //instantiated
    private RummikubWebServiceHandler(){}

    //Get the only object available
    public static RummikubWebServiceHandler getInstance(){
       return instance;
    }    
    
    public void createGame(String name, int humanPlayers, int computerizedPlayers) 
            throws ws.rummikub.InvalidParameters_Exception, 
                   ws.rummikub.DuplicateGameName_Exception {
        RummikubGameManager.getInstance().AddGame(name, humanPlayers, computerizedPlayers);
    }
    
    public String createGameFromXML(String xmlData) 
            throws ws.rummikub.InvalidXML_Exception, 
            ws.rummikub.DuplicateGameName_Exception, 
            ws.rummikub.InvalidParameters_Exception {
        try {
            return RummikubGameManager.getInstance().AddGame(xmlData);
        } catch (SAXException ex) {
            Logger.getLogger(RummikubWebServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubBadTilesStructureException ex) {
            Logger.getLogger(RummikubWebServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FileNotFoundException ex) {
            Logger.getLogger(RummikubWebServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubColorConvertionException ex) {
            Logger.getLogger(RummikubWebServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (RummikubBadXMLFormatException ex) {
            Logger.getLogger(RummikubWebServiceHandler.class.getName()).log(Level.SEVERE, null, ex);
        } catch (JAXBException ex) {
            /*InvalidXML invalidXML = new InvalidXML();
            invalidXML.setMessage(ex.getMessage());
            RummikubFault rummFault = new RummikubFault();
            rummFault.setFaultCode("00");
            rummFault.setFaultString(););
            invalidXML.setFaultInfo(rummFault);*/
            throw new InvalidXML_Exception("Invalid XML", null);
        }

        // should be removed.
        return null;
    }    
    
    public ws.rummikub.GameDetails getGameDetails(String gameName) throws GameDoesNotExists_Exception {
        return RummikubGameManager.getInstance().GetGameDetails(gameName);
    }   
    
    public List<String> getWaitingGames() {
        return RummikubGameManager.getInstance().getWaitingGames();
    }  
    
    public int joinGame(String gameName, String playerName) 
            throws ws.rummikub.GameDoesNotExists_Exception, 
            ws.rummikub.InvalidParameters_Exception {
        try{
            return RummikubGameManager.getInstance().joinGame(gameName, playerName);
        } catch (RummikubPoolOutOfTilesException ex) {
            throw new ws.rummikub.InvalidParameters_Exception("Out of pool!", null);
        }
        
    }
    
    public List<ws.rummikub.PlayerDetails> getPlayersDetails(String gameName) 
            throws ws.rummikub.GameDoesNotExists_Exception {
        try {
            return RummikubGameManager.getInstance().getPlayersDetails(gameName);
        } catch (RummikubColorConvertionException ex) {
            // should be handled here.
        }
        
        return null;
    }    
    
    public ws.rummikub.PlayerDetails getPlayerDetails(int playerId) 
            throws ws.rummikub.GameDoesNotExists_Exception, ws.rummikub.InvalidParameters_Exception {
        try {
            return RummikubGameManager.getInstance().getPlayerDetails(playerId);
        } catch (RummikubColorConvertionException ex) {
            // should be handled here.
        }
        
        return null;
    }    
    
    public java.util.List<ws.rummikub.Event> getEvents(int playerId, int eventId) 
            throws ws.rummikub.InvalidParameters_Exception {
        return RummikubGameManager.getInstance().getEvents(playerId, eventId);
    }    
    
    public void addTile(int playerId, 
                        ws.rummikub.Tile tile, 
                        int sequenceIndex, 
                        int sequencePosition) 
            throws ws.rummikub.InvalidParameters_Exception {
        
        RummikubGameManager.getInstance().addTile(playerId, 
                                                  tile,
                                                  sequenceIndex,
                                                  sequencePosition);

    }    
    
    public void createSequence(int playerId, java.util.List<ws.rummikub.Tile> tiles) throws ws.rummikub.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }    
    
    public void finishTurn(int playerId) 
            throws ws.rummikub.InvalidParameters_Exception {
        RummikubGameManager.getInstance().finishTurn(playerId);
    }    

    public void moveTile(int playerId, 
                         int sourceSequenceIndex, 
                         int sourceSequencePosition, 
                         int targetSequenceIndex, 
                         int targetSequencePosition) throws InvalidParameters_Exception {
        RummikubGameManager.getInstance().moveTile(playerId,
                                                   sourceSequenceIndex,
                                                   sourceSequencePosition,
                                                   targetSequenceIndex,
                                                   targetSequencePosition);
    }

    public void resign(int playerId) {
        RummikubGameManager.getInstance().resign(playerId);
    }
    
}
