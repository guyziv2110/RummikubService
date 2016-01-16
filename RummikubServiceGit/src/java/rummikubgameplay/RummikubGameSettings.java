/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import rummikubengine.RummikubBoard;
import ws.rummikub.GameDetails;
import ws.rummikub.GameStatus;

public class RummikubGameSettings {
    // optional holding gamedetails as member (like proxy).
    private GameDetails gd = new GameDetails();

    public GameDetails getGd() {
        return gd;
    }
    private boolean loadedFromXML;

    public boolean isLoadedFromXML() {
        return loadedFromXML;
    }

    public void setLoadedFromXML(boolean loadedFromXML) {
        this.loadedFromXML = loadedFromXML;
    }

    public int getJoinedHumanPlayers() {
        return joinedHumanPlayers;
    }

    public void setJoinedHumanPlayers(int joinedHumanPlayers) {
        this.joinedHumanPlayers = joinedHumanPlayers;
        this.gd.setJoinedHumanPlayers(this.joinedHumanPlayers);
    }
    
    private int gameId;

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }
    private String rummName;
    private RummikubBoard rummBoard;
    private String SettingsFilePath;
    private String xmlSettingsData;
    private int numOfHumanPlayers;

    public void setNumOfHumanPlayers(int numOfHumanPlayers) {
        this.gd.setHumanPlayers(numOfHumanPlayers);
        this.numOfHumanPlayers = numOfHumanPlayers;
    }

    public void setNumOfComputerPlayers(int numOfComputerPlayers) {
        this.gd.setComputerizedPlayers(numOfHumanPlayers);
        this.numOfComputerPlayers = numOfComputerPlayers;
    }
    static int currId = 0;
    
    public int getNumOfHumanPlayers() {
        return numOfHumanPlayers;
    }

    public int getNumOfComputerPlayers() {
        return numOfComputerPlayers;
    }
    private int numOfComputerPlayers;
    private int joinedHumanPlayers;
    private GameStatus status;

    public GameStatus getStatus() {
        return status;
    }

    public void setStatus(GameStatus status) {
        this.status = status;
        this.gd.setStatus(status);
    }
    
    // add template like simpsons

    public RummikubGameSettings(RummikubBoard rummBoard) {
        this.rummBoard = rummBoard;
        this.gameId = currId;
        currId++;
    }
    
    public RummikubGameSettings(RummikubBoard rummBoard,
                                String gameName,
                                int numOfHumanPlayers,
                                int numOfComputerPlayers) {
        this.rummBoard = rummBoard;
        this.rummName = gameName;
        this.gd.setName(gameName);
        this.numOfHumanPlayers = numOfHumanPlayers;
        this.gd.setHumanPlayers(numOfHumanPlayers);
        this.numOfComputerPlayers = numOfComputerPlayers;
        this.gd.setComputerizedPlayers(numOfHumanPlayers);
        this.gameId = currId;
        currId++;
    }    

    public String getRummName() {
        return rummName;
    }

    public void setRummName(String rummName) {
        this.rummName = rummName;
        this.gd.setName(rummName);
    }

    public RummikubBoard getRummBoard() {
        return rummBoard;
    }

    public void setRummBoard(RummikubBoard rummBoard) {
        this.rummBoard = rummBoard;
    }

    public String getSettingsFilePath() {
        return SettingsFilePath;
    }

    public void setSettingsFilePath(String SettingsFilePath) {
        this.SettingsFilePath = SettingsFilePath;
    }

    public String getXmlSettingsData() {
        return xmlSettingsData;
    }

    public void setXmlSettingsData(String xmlSettingsData) {
        this.xmlSettingsData = xmlSettingsData;
    }
    
}
