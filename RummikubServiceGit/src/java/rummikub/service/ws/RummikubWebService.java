/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikub.service.ws;

import javax.jws.WebService;

/**
 *
 * @author guy
 */
@WebService(serviceName = "RummikubWebServiceService", portName = "RummikubWebServicePort", endpointInterface = "ws.rummikub.RummikubWebService", targetNamespace = "http://rummikub.ws/", wsdlLocation = "WEB-INF/wsdl/RummikubWebService/RummikubWebServiceService.wsdl")
public class RummikubWebService {
    
    public java.util.List<ws.rummikub.Event> getEvents(int playerId, int eventId) throws ws.rummikub.InvalidParameters_Exception {
        return RummikubWebServiceHandler.getInstance().getEvents(playerId, eventId);
    }

    public java.lang.String createGameFromXML(java.lang.String xmlData) throws ws.rummikub.InvalidXML_Exception, ws.rummikub.DuplicateGameName_Exception, ws.rummikub.InvalidParameters_Exception {
        return RummikubWebServiceHandler.getInstance().createGameFromXML(xmlData);
    }

    public java.util.List<ws.rummikub.PlayerDetails> getPlayersDetails(java.lang.String gameName) throws ws.rummikub.GameDoesNotExists_Exception {
        return RummikubWebServiceHandler.getInstance().getPlayersDetails(gameName);
    }

    public void createGame(java.lang.String name, int humanPlayers, int computerizedPlayers) throws ws.rummikub.InvalidParameters_Exception, ws.rummikub.DuplicateGameName_Exception {
        RummikubWebServiceHandler.getInstance().createGame(name, humanPlayers, computerizedPlayers);
    }

    public ws.rummikub.GameDetails getGameDetails(java.lang.String gameName) throws ws.rummikub.GameDoesNotExists_Exception {
        return RummikubWebServiceHandler.getInstance().getGameDetails(gameName);
    }

    public java.util.List<java.lang.String> getWaitingGames() {
        return RummikubWebServiceHandler.getInstance().getWaitingGames();
    }

    public int joinGame(java.lang.String gameName, java.lang.String playerName) throws ws.rummikub.GameDoesNotExists_Exception, ws.rummikub.InvalidParameters_Exception {
        return RummikubWebServiceHandler.getInstance().joinGame(gameName, playerName);
    }

    public ws.rummikub.PlayerDetails getPlayerDetails(int playerId) throws ws.rummikub.GameDoesNotExists_Exception, ws.rummikub.InvalidParameters_Exception {
        return RummikubWebServiceHandler.getInstance().getPlayerDetails(playerId);
    }

    public void createSequence(int playerId, java.util.List<ws.rummikub.Tile> tiles) throws ws.rummikub.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void addTile(int playerId, ws.rummikub.Tile tile, int sequenceIndex, int sequencePosition) throws ws.rummikub.InvalidParameters_Exception {
        RummikubWebServiceHandler.getInstance().addTile(playerId, tile, sequenceIndex, sequencePosition);
    }

    public void takeBackTile(int playerId, int sequenceIndex, int sequencePosition) throws ws.rummikub.InvalidParameters_Exception {
        //TODO implement this method
        throw new UnsupportedOperationException("Not implemented yet.");
    }

    public void moveTile(int playerId, int sourceSequenceIndex, int sourceSequencePosition, int targetSequenceIndex, int targetSequencePosition) throws ws.rummikub.InvalidParameters_Exception {
        RummikubWebServiceHandler.getInstance().moveTile(playerId,
                                                         sourceSequenceIndex,
                                                         sourceSequencePosition,
                                                         targetSequenceIndex,
                                                         targetSequencePosition);
    }

    public void finishTurn(int playerId) throws ws.rummikub.InvalidParameters_Exception {
        RummikubWebServiceHandler.getInstance().finishTurn(playerId);
    }

    public void resign(int playerId) throws ws.rummikub.InvalidParameters_Exception {
        RummikubWebServiceHandler.getInstance().resign(playerId);
    }
    
}
