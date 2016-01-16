/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;


public class RummikubBoard{
    public static final int DECKSIZE = 14;
    public static final int PLAYERDECKSIZEINROW = 20;
    private final int BOARD_LINES = 12;
    private final int BOARD_COLS = 22;    
    

    
    private List<RummikubTile> rummikubTiles = new ArrayList<RummikubTile>();
    private List<RummikubPlayer> rummikubPlayers = new LinkedList<>();
    
    // copy constrctor for the board.
    public RummikubBoard(RummikubBoard rummBoard) {
        this.rummikubTiles = new ArrayList<RummikubTile>(rummBoard.rummikubTiles);
        this.rummikubPlayers = new LinkedList<RummikubPlayer>(rummBoard.rummikubPlayers);
    }

    public List<RummikubTile> getRummikubTiles() {
        return rummikubTiles;
    }
    
    public int getRummikubPlayersCount() {
        return rummikubPlayers.size();
    }
    
    public List<RummikubPlayer> getRummikubPlayers() {
        return rummikubPlayers;
    }
    
    public RummikubBoard() {
        int tileNumber;
        long seed;
        
        for (tileNumber = 0; tileNumber < 13; tileNumber++) {
            initializeTiles(RummikubTileColor.BLACK, RummikubTileNumber.values()[tileNumber],
                            -1, -1, RummikubTileState.ONBOARDFACEDOWN);
            initializeTiles(RummikubTileColor.BLUE, RummikubTileNumber.values()[tileNumber],
                                        -1, -1, RummikubTileState.ONBOARDFACEDOWN);
            initializeTiles(RummikubTileColor.RED, RummikubTileNumber.values()[tileNumber],
                                        -1, -1, RummikubTileState.ONBOARDFACEDOWN);
            initializeTiles(RummikubTileColor.YELLOW, RummikubTileNumber.values()[tileNumber],
                                        -1, -1, RummikubTileState.ONBOARDFACEDOWN);                
        }
        
        initializeTiles(RummikubTileColor.BLACK, RummikubTileNumber.JOKER,
                                        -1, -1, RummikubTileState.ONBOARDFACEDOWN);   
        
        seed = System.nanoTime();
        Collections.reverse(rummikubTiles);
        //Collections.shuffle(rummikubTiles, new Random(seed));         
    }
   
    private void initializeTiles(RummikubTileColor rummTileColor, RummikubTileNumber rummTileNumber,
                                        int rummLine, int rummCol, RummikubTileState tileState) {
        
        rummikubTiles.add(new RummikubTile(rummTileColor, rummTileNumber, rummLine, rummCol, tileState));
        rummikubTiles.add(new RummikubTile(rummTileColor, rummTileNumber, rummLine, rummCol, tileState));
    }
    
    
    public List<RummikubTile> getTilesPlacedOnBoard() {
        List<RummikubTile> boardTiles = 
                rummikubTiles.
                stream().
                filter(t -> t.getTileState() == RummikubTileState.ONBOARDFACEUP).
                collect(Collectors.toList());
        
        return boardTiles;
    }
    
    public boolean isBoardEmpty() {
        List<RummikubTile> boardTiles = 
                rummikubTiles.
                stream().
                filter(t -> t.getTileState() == RummikubTileState.ONBOARDFACEUP).
                collect(Collectors.toList());
        
        return boardTiles.isEmpty();
    }
    
    public boolean isBoardPoolEmpty() {
        List<RummikubTile> boardTiles = 
                rummikubTiles.
                stream().
                filter(t -> t.getTileState() == RummikubTileState.ONBOARDFACEDOWN).
                collect(Collectors.toList());
        
        return boardTiles.isEmpty();
    }
    
    private  List<RummikubTile> getBoardTiles() throws RummikubPoolOutOfTilesException {
        List<RummikubTile> boardTiles = 
                rummikubTiles.
                stream().
                filter(t -> t.getTileState() == RummikubTileState.ONBOARDFACEDOWN).
                collect(Collectors.toList());
        
        if ( boardTiles.isEmpty() ) 
            throw new RummikubPoolOutOfTilesException();
        
        return boardTiles;
    }
    
    // get random tiles from the board.
    public List<RummikubTile> getRandomTiles() throws RummikubPoolOutOfTilesException {
        int i;
        List<RummikubTile> listOfRummTiles = new ArrayList<>();
        List<RummikubTile> searchList = getBoardTiles();
        
        for (i=0; i < DECKSIZE; i++) {
            searchList.get(i).setTileState(RummikubTileState.ONDECK);
            listOfRummTiles.add(searchList.get(i));
        }
        
        return listOfRummTiles;
    }
    
    public void setRandomTilesToPlayer(RummikubPlayer rummPlayer) throws RummikubPoolOutOfTilesException {
        int i;
        List<RummikubTile> searchList = getBoardTiles();
        
        for (i=0; i < DECKSIZE; i++) {
            searchList.get(i).setTileState(RummikubTileState.ONDECK);
            addTileToPlayer(searchList.get(i), rummPlayer);
            //rummPlayer.playerDeckTiles.add(searchList.get(i));
        }
    }
    
    public RummikubPlayer getPlayerById(int playerId) {
        Optional<RummikubPlayer> rummPlayer = 
                this.rummikubPlayers.stream().filter(t -> t.getPlayerId() == playerId).findFirst();
        
        if(rummPlayer.isPresent()){
            return rummPlayer.get();
        }
        else{
            return null;
        }            
    }
            
    public RummikubTile getSingleRummikubTile() throws RummikubPoolOutOfTilesException {
        RummikubTile rummTile;
        List<RummikubTile> searchList = getBoardTiles();
        rummTile = searchList.get(0);
        return rummTile;
    }

    public int getEmptyColOnLine(int line) {
        int minCol = 0;
        List<RummikubTile> rummTiles = getRummikubTilesInLineK(line);
        
        Collections.sort(rummTiles, RummikubTile::compareByLColumnNumber);
        for (RummikubTile rummTile : rummTiles) {
            if(rummTile.getTileColumn() >= 0 &&  rummTile.getTileColumn() != minCol) return minCol;
            minCol++;
        }        
        
        return minCol;
    }
    
    private RummikubTilePosition getKSpace(int line, int spaceSize) {
        int j;
        int emptySpace = 0;
        int spacer = 0;
        RummikubTilePosition rummTilePos = null;
        
        for (j = 0 ; j < getMaxBoardCols(); j++) {
            if (getTileByLineAndColumn(line, j) == null && spacer == 1) {
                spacer = 0;
            }
            
            else if(getTileByLineAndColumn(line, j) == null) {
                emptySpace++;
                
                if(emptySpace == spaceSize)
                {
                    rummTilePos = new RummikubTilePosition();
                    rummTilePos.setRow(line);
                    rummTilePos.setCol((j + 1) - emptySpace);
                    return rummTilePos;
                }
            }
            else {
                emptySpace = 0;
                spacer = 1;
            }
        }        
        
        return null;
    }
    
    public RummikubTilePosition getEmptySpaceForTilesOnBoard(int spaceSize) {
        int i;
        RummikubTilePosition rummTilePosition;
        
        for (i = 0; i < getMaxBoardLines(); i++) {
            rummTilePosition = getKSpace(i, spaceSize);
            if (rummTilePosition != null) 
                return rummTilePosition;
        }
                
        return null;
    }  
    
    public int getEmptyLineOnBoard() {
        int i;
        
        for (i = 0; i < getMaxBoardLines(); i++) {
            if (getRummikubTilesInLineK(i).isEmpty()) 
                return i;
        }
        
        return -1;
        //return getMaxBoardLines() + 1;
    }    
    
    public void addTileBySequenceIndexAndPosition(int playerId,
                                                  RummikubTile t,
                                                  int sequenceIndex, 
                                                  int sequencePosition) 
            throws RummikubTilesPositionCollisionException {
        
        /*
        List<RummikubTile> rummSeq = getRummikubSequenceByIndex(sequenceIndex);
        
        if(rummSeq == null) {
                int emptyLine = getEmptyLineOnBoard();
                moveTileToLineAndCol(t, 
                                     getEmptyColOnLine(emptyLine), 
                                     emptyLine);
                addTileToPlayer(t, getPlayerById(playerId));
        }
        else {
            // doesn't support SPLIT right now
            if(sequencePosition == 0) {
                moveTileToLineAndCol(t, 
                                     rummSeq.get(0).getTileColumn() - 1, 
                                     rummSeq.get(0).getTileLine());
                addTileToPlayer(t, getPlayerById(playerId));
            }
            else if(sequencePosition == rummSeq.size()) {
                moveTileToLineAndCol(t, 
                                     rummSeq.get(rummSeq.size() - 1).getTileColumn() + 1,
                                     rummSeq.get(0).getTileLine());
                addTileToPlayer(t, getPlayerById(playerId));    
            }
        }*/
    }
    
    public List<RummikubTile> getRummikubSequenceByIndex(int index) {
        List<List<RummikubTile>> rummSets = new ArrayList<List<RummikubTile>>();
        List<RummikubTile> rummTiles = 
                rummikubTiles.
                stream().
                filter(t -> t.getTileState() == RummikubTileState.ONBOARDFACEUP).
                collect(Collectors.toList());
        
            if (!rummTiles.isEmpty()) {
       
            Collections.sort(rummTiles, RummikubTile::compareByLineAndColumnNumber);

            int i;
            int currentSet = 0;
            rummSets.add(new ArrayList<>());
            rummSets.get(currentSet).add(rummTiles.get(0));

            for (i = 0; i < rummTiles.size() - 1; i++) 
            {
                if(rummTiles.get(i).getTileColumn() == rummTiles.get(i+1).getTileColumn() - 1 &&
                        rummTiles.get(i).getTileLine() == rummTiles.get(i+1).getTileLine()) {
                    rummSets.get(currentSet).add(rummTiles.get(i+1));
                }
                else {
                    currentSet++;
                    rummSets.add(new ArrayList<>());  
                    rummSets.get(currentSet).add(rummTiles.get(i + 1));
                }
            }  
        }
            
         
        return rummSets.get(index);
    }     
    
    public List<RummikubTile> getRummikubTilesInLineK(int k) {
        return rummikubTiles.stream().filter(line -> line.getTileLine() == k && line.getTileState() == RummikubTileState.ONBOARDFACEUP).collect(Collectors.toList());
    }    
        
    public void addNewPlayer(RummikubPlayer rummPlayer) {
        rummikubPlayers.add(rummPlayer);
    }
    
    public void removePlayerById(int rummPlayerId) {
        Optional<RummikubPlayer> rummPlayer = 
                this.rummikubPlayers.stream().filter(t -> t.getPlayerId()== rummPlayerId).findFirst();
        
        if(rummPlayer.isPresent()){
            rummikubPlayers.remove(rummPlayer.get());
        }
    }  
    
    public void removePlayerByName(String rummPlayerName) {
        Optional<RummikubPlayer> rummPlayer = 
                this.rummikubPlayers.stream().filter(t -> t.getPlayerName() == rummPlayerName).findFirst();
        
        if(rummPlayer.isPresent()){
            rummikubPlayers.remove(rummPlayer.get());
        }
    }    
    
    public int getNumOfComputerPlayers() {
        List<RummikubPlayer> rummPlayers = 
                this.rummikubPlayers.stream().filter(t -> t instanceof RummikubComputerPlayer ).collect(Collectors.toList());
        
        return rummPlayers.size();    
    }
    
    public int getNumOfHumanPlayers() {
        List<RummikubPlayer> rummPlayers = 
                this.rummikubPlayers.stream().filter(t -> t instanceof RummikubHumanPlayer ).collect(Collectors.toList());
        
        return rummPlayers.size();
    }
    
    public void addTileToPlayer(RummikubTile rummTile, RummikubPlayer rummPlayer) {
        rummTile.setTileState(RummikubTileState.ONDECK);
        int row = rummPlayer.playerDeckTiles.size() / PLAYERDECKSIZEINROW;
        int col = rummPlayer.playerDeckTiles.size() % PLAYERDECKSIZEINROW;
        rummTile.setTileLine(row);
        rummTile.setTileColumn(col);
        rummPlayer.playerDeckTiles.add(rummTile);
    }

    public void moveTileToBoard(RummikubTile rummTile, 
                                RummikubPlayer rummPlayer, int line, int col) throws RummikubTilesPositionCollisionException{
        rummPlayer.playerDeckTiles.remove(rummTile);
        rummTile.setTileColumn(-1);
        rummTile.setTileLine(-1);
        rummTile.setTileState(RummikubTileState.ONBOARDFACEUP);            
        moveTileToLineAndCol(rummTile, col, line);
    }
        
    public void removeTileToPlayer(RummikubTile rummTile, RummikubPlayer rummPlayer) {
        rummPlayer.playerDeckTiles.remove(rummTile);
        rummTile.setTileState(RummikubTileState.ONBOARDFACEDOWN);
    }    

    public RummikubTile getTileByLineAndColumn(int tileFromLine, int tileFromColumn) {
        Optional<RummikubTile> tile =  
                rummikubTiles.
                stream().
                filter(line -> line.getTileLine() == tileFromLine).
                filter(col -> col.getTileColumn() == tileFromColumn).
                filter(state -> state.getTileState()== RummikubTileState.ONBOARDFACEUP).
                findFirst();
        
        if(tile.isPresent()){
            return tile.get();
        }
        else{
            return null;
        }        
    }
    
    public int getMaxBoardCols() {
        return BOARD_COLS;
    }
    
    public int getMaxBoardLines() {
        return BOARD_LINES;
    }

    private boolean isTileExistsInColumnAndLine(int col, int line) {

        List<RummikubTile> rummTileList = rummikubTiles.stream().
                             filter(t -> t.getTileLine() == line && t.getTileColumn() == col && t.getTileState()==RummikubTileState.ONBOARDFACEUP).collect(Collectors.toList());
        
        return (rummikubTiles.stream().
                             filter(t -> t.getTileLine() == line && t.getTileColumn() == col && t.getTileState()==RummikubTileState.ONBOARDFACEUP).
                             count() > 0);
    }
    
    public void moveTileToLineAndCol(RummikubTile rummTile, 
                                     int tileToColumn, 
                                     int tileToLine) throws RummikubTilesPositionCollisionException {
        
        if (!isTileExistsInColumnAndLine(tileToColumn, tileToLine) ) {
            rummTile.setTileColumn(tileToColumn);
            rummTile.setTileLine(tileToLine);
        }
        else {
            throw new RummikubTilesPositionCollisionException();
        }
    }
    
    public void arrangeLineByColumn(int line) {
        List<RummikubTile> oldRummTiles;
        List<RummikubTile> newRummTiles;
        
        oldRummTiles = getRummikubTilesInLineK(line);
        newRummTiles = getRummikubTilesInLineK(line);
        Collections.sort(newRummTiles, RummikubTile::compareByColorAndNumber);
        
        for(RummikubTile t : oldRummTiles) {
            t.setTileColumn(newRummTiles.indexOf(t));
        }
    }

    public List<RummikubPlayer> getPlayersWhoJoinedGame() {
        List<RummikubPlayer> rummPlayersJoinedGame = rummikubPlayers.stream().
                filter(p->p.joinedGame == true).collect(Collectors.toList());
        
        return rummPlayersJoinedGame;
    }
    
    public RummikubPlayer getPlayerByName(String currentPlayerName) {
        Optional<RummikubPlayer> rummPlayer = rummikubPlayers.stream().
                filter(p->p.getPlayerName().equals(currentPlayerName)).findAny();
        
        if(rummPlayer.isPresent()) 
            return rummPlayer.get();
        else 
            return null;
    }

    public RummikubPlayer getPlayerInTurn() {
        Optional<RummikubPlayer> rummPlayer =  this.rummikubPlayers.stream().filter(p->p.isIsPlayerTurn() == true).findFirst();
        RummikubPlayer nextPlayer;
        RummikubPlayer currPlayer = null;
         
        for (Iterator<RummikubPlayer> i = this.rummikubPlayers.iterator(); i.hasNext();) {
            currPlayer = i.next();
            if(currPlayer == rummPlayer.get()) {
                if(i.hasNext())
                {
                    nextPlayer = i.next();
                }
                else 
                {
                    nextPlayer = rummikubPlayers.iterator().next();
                }
                
                nextPlayer.setIsPlayerTurn(true);
                currPlayer.setIsPlayerTurn(false);
                break;    
            }
        }
        
        return currPlayer;
    }

    public List<RummikubTile> getAllFaceUpTemp() {
        List<RummikubTile> rummTileList = rummikubTiles.stream().
                             filter(t -> t.getTileState()==RummikubTileState.ONBOARDFACEUP).collect(Collectors.toList());
        
        return rummTileList;
    }
}
