/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.io.Serializable;

public class RummikubTile implements Serializable{
    private RummikubTileColor tileColor;
    private RummikubTileNumber tileNumber;
    private RummikubTileState tileState;
    private int tileLine;
    private int tileColumn;
    
    // copy constructor
    public RummikubTile(RummikubTile rummTile) {
        this(rummTile.tileColor,
             rummTile.tileNumber,
             rummTile.tileLine,
             rummTile.tileColumn,
             rummTile.tileState);
    }
     
    public RummikubTile(RummikubTileColor tileColor, 
                        RummikubTileNumber tileNumber, 
                        int tileLine, 
                        int tileCol,
                        RummikubTileState tileState) {
        this.tileColor = tileColor;
        this.tileNumber = tileNumber;
        this.tileLine = tileLine;
        this.tileState = tileState;
        this.tileColumn = tileCol;
    }
    
    public int getTileLine() {
        return tileLine;
    }

    public int getTileColumn() {
        return tileColumn;
    }
    
    public RummikubTileColor getTileColor() {
        return tileColor;
    }
    
    public RummikubTileNumber getTileNumber() {
        return tileNumber;
    }
    
    public RummikubTileState getTileState() {
        return tileState;
    }

    public void setTileState(RummikubTileState tileState) {
        this.tileState = tileState;
    }
    
    public void setTileLine(int tileLine) {
        this.tileLine = tileLine;
    }
    
    public void setTileColumn(int tileColumn) {
        this.tileColumn = tileColumn;
    }    
    
    // copy existing tile.
    public void copyRummikubTile(RummikubTile rummTile) {
        this.tileColor = rummTile.tileColor;
        this.tileLine = rummTile.tileLine;
        this.tileNumber = rummTile.tileNumber;
        this.tileState = rummTile.tileState;
        this.tileColumn = rummTile.tileColumn;
        
    }
   
    public static int compareByColorAndNumber(RummikubTile firstRummTile, RummikubTile secondRummTile) {        
        int result = Integer.compare(firstRummTile.tileColor.ordinal(), secondRummTile.getTileColor().ordinal());
        if( result != 0 ) return result;
        
        result = Integer.compare(firstRummTile.tileNumber.getNumber(), secondRummTile.tileNumber.getNumber());
        return result;
    }
    
    public static int compareByLineAndColumnNumber(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        int result = Integer.compare(firstRummTile.tileLine, secondRummTile.getTileLine());
        if( result != 0 ) return result;
        
        result = Integer.compare(firstRummTile.tileColumn, secondRummTile.tileColumn);
        return result;
    }    
    
    public static int compareByLineNumber(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        int result = Integer.compare(firstRummTile.tileLine, secondRummTile.getTileLine());
        return result;
    }    

    public static int compareByLColumnNumber(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        int result = Integer.compare(firstRummTile.tileColumn, secondRummTile.getTileColumn());
        return result;
    }        
    
    public static boolean areTilesAscending(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        if(firstRummTile.getTileNumber() == RummikubTileNumber.JOKER ||
               secondRummTile.getTileNumber() == RummikubTileNumber.JOKER ) return true;            
        return firstRummTile.getTileNumber().getNumber() ==
                                     secondRummTile.getTileNumber().getNumber() - 1;
    }
    
    public static boolean areTilesNumberIdentical(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        if(firstRummTile.getTileNumber() == RummikubTileNumber.JOKER ||
               secondRummTile.getTileNumber() == RummikubTileNumber.JOKER ) return true;
        return firstRummTile.getTileNumber().getNumber() ==
                                     secondRummTile.getTileNumber().getNumber();
    }

    public static boolean compareByColor(RummikubTile firstRummTile, 
            RummikubTile secondRummTile) {
        
        if(firstRummTile.getTileNumber() == RummikubTileNumber.JOKER ||
               secondRummTile.getTileNumber() == RummikubTileNumber.JOKER ) return true;   
        return firstRummTile.tileColor.ordinal() == secondRummTile.getTileColor().ordinal();
    }    
}
