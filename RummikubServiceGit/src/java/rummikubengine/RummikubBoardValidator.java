/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import static rummikubengine.RummikubMethodsExtensions.distinctByKey;

public class RummikubBoardValidator {
    /*private static RummikubBoardValidator instance = new RummikubBoardValidator();

    //make the constructor private so that this class cannot be
    //instantiated
    private RummikubBoardValidator(){}

    //Get the only object available
    public static RummikubBoardValidator getInstance(){
       return instance;
    }       */
    
    private boolean useColumnSorting;
    
    public boolean validateBoard(RummikubBoard rummikubBoard, boolean useColumnSorting/*, RummikubPlayer rummPlayer*/) {
        int i;
        boolean validResult = true;
        this.useColumnSorting = useColumnSorting;
        
        for(i = 0; i < rummikubBoard.getMaxBoardLines(); i++) {
            validResult &=  validateLineSet(rummikubBoard, i);
        }
        
        return validResult;
    }
    
    private boolean validateLineSet(RummikubBoard rummikubBoard, int line) {
        // runs on a line of tiles and determine if the line is a proper set or no.
        // check if isSeriesSet and then check if colorSet
        List<RummikubTile> rummSetsInLine = rummikubBoard.getRummikubTilesInLineK(line);
        List<List<RummikubTile>> rummSets = new ArrayList<List<RummikubTile>>();
        boolean validationResult = true;
        
        if (!rummSetsInLine.isEmpty()) {
       
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

            List<RummikubTile> rummSet;
            for(List<RummikubTile> rummTileList : rummSets) {
                rummSet = rummTileList;
                if(rummSet.isEmpty()) return true;

                if(rummSet.size() < 3) return false;

                validationResult &= isSeriesSet(rummSet) || 
                                    isColorSet(rummSet);            
            }
        }
        return validationResult;

    }
    
    private boolean isSeriesSet(List<RummikubTile> rummLineSet) {       
        if (isSeriesAscending(rummLineSet)) return true;
        
        return false;
    }
    
    private boolean isColorSet(List<RummikubTile> rummLineSet) {
        HashMap<RummikubTileColor, Integer> rummTileColorCountMap = new HashMap<RummikubTileColor, Integer>();
        for (RummikubTileColor rummColor : RummikubTileColor.values())
            rummTileColorCountMap.put(rummColor, 0 );

        if (isKColorSet(rummLineSet, rummTileColorCountMap, 4)) return true;
        if (isKColorSet(rummLineSet, rummTileColorCountMap, 3)) return true;
        
        return false;
    }
    
    private boolean isSeriesAscending(List<RummikubTile> rummSet) {
        int currentTileNumber;
        boolean result = true;
        RummikubTile firstRummTile;
        RummikubTile secondRummTile;
        
        if (this.useColumnSorting)
            Collections.sort(rummSet, RummikubTile::compareByLColumnNumber);
        
        for (currentTileNumber = 0; 
             currentTileNumber < rummSet.size() - 1; 
             currentTileNumber++) {
            
            firstRummTile = rummSet.get(currentTileNumber);
            secondRummTile = rummSet.get(currentTileNumber + 1);
            
            if ((!RummikubTile.areTilesAscending(firstRummTile, secondRummTile)) ||
                    (!RummikubTile.compareByColor(firstRummTile, secondRummTile)) ) {
               result = false;
               break;                
            }
        }
        
        return result;
    }

    private boolean isKColorSet(List<RummikubTile> rummSet, 
                                HashMap<RummikubTileColor, Integer> rummTileColorCountMap, 
                                int k) {
        return areTilesNumbersIdentical(rummSet) &&
                areColorsUnique(rummSet, rummTileColorCountMap, k);
    }
    
    private boolean areTilesNumbersIdentical(List<RummikubTile> rummSet) {
        int currentTileNumber;

        boolean result = true;
        RummikubTile firstRummTile;
        RummikubTile secondRummTile;        
        
        if (this.useColumnSorting)
            Collections.sort(rummSet, RummikubTile::compareByLColumnNumber);
        
        for (currentTileNumber = 0; 
             currentTileNumber < rummSet.size() - 1; 
             currentTileNumber++) {
            
            firstRummTile = rummSet.get(currentTileNumber);
            secondRummTile = rummSet.get(currentTileNumber + 1);
            
            if (!RummikubTile.areTilesNumberIdentical(firstRummTile, secondRummTile)) {
               result = false;
               break;                
            }
        }    
        
        return result;
    }
    
    private boolean areColorsUnique(List<RummikubTile> rummSet, 
                                HashMap<RummikubTileColor, Integer> rummTileColorCountMap, 
                                int k) {
        
        int rummSetCount;
        int distinctRummSetCount;
        
        rummSetCount = rummSet.size();
        distinctRummSetCount = (int) rummSet.
                               stream().
                               filter(distinctByKey(p -> p.getTileColor())).count();

                
        return (rummSetCount == distinctRummSetCount && rummSetCount == k);
    }
}
