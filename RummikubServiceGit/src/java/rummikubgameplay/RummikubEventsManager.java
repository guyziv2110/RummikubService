/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTilePosition;
import ws.rummikub.Color;
import ws.rummikub.Event;
import ws.rummikub.EventType;
import ws.rummikub.Tile;

/**
 *
 * @author guy
 */
public class RummikubEventsManager {
   private List<RummikubEvent> rummikubEvents;
    
    //create an object of SingleObject
    private static RummikubEventsManager instance = new RummikubEventsManager();

    //make the constructor private so that this class cannot be
    //instantiated
    private RummikubEventsManager(){
        rummikubEvents = new ArrayList<>();
    }

    //Get the only object available
    public static RummikubEventsManager getInstance(){
       return instance;
    }
    
    private int getNextEventId(int gameId) {
        long nextEvent =  rummikubEvents.stream().
                filter(event -> event.getEventInGameId() == gameId).
                count();
        
        return (int)nextEvent + 1;
    }
    
    public List<ws.rummikub.Event> getEvents(int gameId, int eventId) 
            throws ws.rummikub.InvalidParameters_Exception {
        List<Event> retEvents = 
                rummikubEvents.stream()
                .filter(e -> e.getEventInGameId() == gameId )
                .map(att -> att.getEvent())
                .collect(Collectors.toList());

        List<Event> s = retEvents.stream()
        .filter(ev -> ev.getId() > eventId)
        .collect(Collectors.toList());
                
        
        System.out.println("Requested event id = " + eventId);
        System.out.println("Result for event id  = " + eventId);
        System.out.println("*********************************");
        for(Event er : s) {
            System.out.println("Event ID =  " + er.getId());
            System.out.println("Event TYPE =  " + er.getType());
        }
        System.out.println("*********************************");
        if (eventId == 0) return retEvents;
        else return retEvents.stream()
                .filter(ev -> ev.getId() > eventId)
                .collect(Collectors.toList());
    }

    private void addEvent(int gameId,
                          //int eventId,
                          int timeoutCount,
                          EventType eventType,
                          String playerName,
                          Tile[] tiles,
                          int sourceSequenceIndex,
                          int sourceSequencePosition,
                          int targetSequenceIndex,
                          int targetSequencePosition) {
        
        RummikubEvent rummEvent = new RummikubEvent();
        rummEvent.setEventInGameId(gameId);
        Event ev = new Event();
        ev.setId(getNextEventId(gameId));
        ev.setTimeout(timeoutCount);
        ev.setPlayerName(playerName);
        ev.setType(eventType);
        ev.getTiles().addAll((Arrays.asList(tiles)));
        ev.setSourceSequenceIndex(sourceSequenceIndex);
        ev.setSourceSequencePosition(sourceSequencePosition);
        ev.setTargetSequenceIndex(targetSequenceIndex);
        ev.setTargetSequencePosition(targetSequencePosition);
        
        rummEvent.setEvent(ev);
        this.rummikubEvents.add(rummEvent);
    }
    
    public void addTileAddedEvent(int gameId,
                                  String playerName,
                                  Tile[] t,
                                  int sourceSequenceIndex, 
                                  int sourceSequencePosition,
                                  int targetSequenceIndex,
                                  int targetSequencePosition) {
        addEvent(gameId, 
                 0,
                 EventType.TILE_ADDED, 
                 playerName, 
                 t, 
                 sourceSequenceIndex, 
                 sourceSequencePosition, 
                 targetSequenceIndex, 
                 targetSequencePosition);
    }
    
  public void addSequenceCreatedEvent(RummikubBoard rummBoard,
                                      int gameId,
                                      rummikubgameplay.jaxbobjects.Tile[] jaxbTiles) {
      
      
        RummikubTilePosition emptySpaceOnBoard = 
                rummBoard.getEmptySpaceForTilesOnBoard(jaxbTiles.length);
        int emptyLineOnBoard = emptySpaceOnBoard.getRow();
        int currentCol = emptySpaceOnBoard.getCol();
        
        for (rummikubgameplay.jaxbobjects.Tile tile : jaxbTiles) {
            
            Tile wsTile = new Tile();
            wsTile.setColor(Color.fromValue(tile.getColor().value()));
            wsTile.setValue(tile.getValue());
            addTileAddedEvent(
                    gameId, 
                    "", 
                    new Tile[]{wsTile}, 
                    -1, 
                    -1, 
                    emptyLineOnBoard, 
                    currentCol);

            currentCol++;
        }        
      
      
        addEvent(gameId, 
                 0,
                 EventType.SEQUENCE_CREATED, 
                 "", 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);
    }    

    public void addGameStartedEvent(int gameId) {
                addEvent(gameId, 
                 0,
                 EventType.GAME_START, 
                 null, 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);
    }
        
    
    public void addPlayerTurnEvent(int gameId, String playerName) {
                addEvent(gameId, 
                 30,
                 EventType.PLAYER_TURN, 
                 playerName, 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);
    }    
    
    public void addTileReturnedEvent(int gameId, 
                                    String playerName, 
                                    Tile t,
                                    int sourceSequenceIndex,
                                    int sourceSequencePosition) {
                addEvent(gameId, 
                 0,
                 EventType.TILE_RETURNED, 
                 playerName, 
                 new Tile[]{t}, 
                 sourceSequenceIndex, 
                 sourceSequencePosition, 
                 -1, 
                 -1);
    }      
    
    public void addFinishedTurnEvent(int gameId, 
                                    String playerName) {
                addEvent(gameId, 
                 0,
                 EventType.PLAYER_FINISHED_TURN, 
                 playerName, 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);
    }         
    
    public void addTiledMovedEvent(int gameId,
                                  String playerName,
                                  Tile[] t,
                                  int sourceSequenceIndex, 
                                  int sourceSequencePosition,
                                  int targetSequenceIndex,
                                  int targetSequencePosition) {
        addEvent(gameId, 
                 0,
                 EventType.TILE_MOVED, 
                 playerName, 
                 t, 
                 sourceSequenceIndex, 
                 sourceSequencePosition, 
                 targetSequenceIndex, 
                 targetSequencePosition);
    }    
    
    public void addPlayerResignedEvent(int gameId, 
                                       String playerName) {
                addEvent(gameId, 
                 0,
                 EventType.PLAYER_RESIGNED, 
                 playerName, 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);
    }      

    public void addGameWinnerEvent(int gameId, String playerName) {
                addEvent(gameId, 
                 0,
                 EventType.GAME_WINNER, 
                 playerName, 
                 new Tile[0], 
                 -1, 
                 -1, 
                 -1, 
                 -1);  
    }
}
