/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleBooleanProperty;
import ws.rummikub.PlayerDetails;
import ws.rummikub.PlayerStatus;
import ws.rummikub.PlayerType;

public abstract class RummikubPlayer {
    protected String playerName;
    protected List<RummikubTile> playerDeckTiles;
    protected int playerId;
    protected boolean playing;
    protected int points;
    protected boolean isPlayerTurn;
    protected boolean didFirstValidMove;
    protected boolean joinedGame;

    public boolean isJoinedGame() {
        return joinedGame;
    }

    public void setJoinedGame(boolean joinedGame) {
        this.joinedGame = joinedGame;
    }
    protected PlayerStatus playerStatus;

    abstract protected PlayerType getPlayerType();
    
    public PlayerStatus getPlayerStatus() {
        return playerStatus;
    }

    public void setPlayerStatus(PlayerStatus playerStatus) {
        this.playerStatus = playerStatus;
    }

    // static counter for creating players with distinct ids.
    static int currId = 0;
    
    private SimpleBooleanProperty commandDone;
    
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }   
    
    // copy constructor
    public RummikubPlayer(RummikubPlayer rummPlayer) {
        this.playerDeckTiles = rummPlayer.playerDeckTiles;
        this.playerId = currId;
        this.playerName = rummPlayer.playerName;
        this.playing = rummPlayer.playing;
        this.points = rummPlayer.points;
        this.isPlayerTurn = false;
        this.didFirstValidMove = false;
        this.joinedGame = false;
        currId++;
        commandDone = new SimpleBooleanProperty(false); 
    }
    
    public RummikubPlayer(String playerName) {
        this.playerDeckTiles = new ArrayList<RummikubTile>();
        this.playerId = currId;
        this.playerName = playerName;
        this.playing = true;
        this.points = 0;
        this.didFirstValidMove = false;
        this.joinedGame = false;
        currId++;
        commandDone = new SimpleBooleanProperty(false); 
    }    
    
    public boolean isDidFirstValidMove() {
        return didFirstValidMove;
    }

    public void setDidFirstValidMove(boolean didFirstValidMove) {
        this.didFirstValidMove = didFirstValidMove;
    }
    
    public boolean isIsPlayerTurn() {
        return isPlayerTurn;
    }

    public void setIsPlayerTurn(boolean isPlayerTurn) {
        this.isPlayerTurn = isPlayerTurn;
    }
        
    public int getPoints() {
        return points;
    }

    public void removePoints(int points) {
        this.points -= points;        
    }
    
    public void addPoints(int points) {
        this.points += points;        
    }
    
    public int getPlayerId() {
        return playerId;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public boolean isPlaying() {
        return playing;
    }
    
    public void setPlaying(boolean isPlaying) {
        this.playing = isPlaying;
    }
        
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public List<RummikubTile> getPlayerDeckTiles() {
        return playerDeckTiles;
    }
    
    public RummikubTile getTileByLineAndColumn(int tileFromLine, int tileFromColumn) {
        Optional<RummikubTile> tile =  
                playerDeckTiles.
                stream().
                filter(line -> line.getTileLine() == tileFromLine).
                filter(col -> col.getTileColumn() == tileFromColumn).
                findFirst();
        
        if(tile.isPresent()){
            return tile.get();
        }
        else{
            return null;
        }        
    }
    
    public RummikubTile getPlayerTileFromByNumber(int tileNumber) {
        return playerDeckTiles.get(tileNumber);
    }    
    
    // compare two players by their points.
    public static int compareByPoints(RummikubPlayer firstPlayer, 
                                      RummikubPlayer secondPlayer) {
        int result = Integer.compare(firstPlayer.getPoints(), secondPlayer.getPoints());
        return result;
    }     
}
