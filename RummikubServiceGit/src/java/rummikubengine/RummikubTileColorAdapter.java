/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import javafx.scene.paint.Color;

public class RummikubTileColorAdapter {
    
    public static Color colorConvertor(RummikubTileColor rummTileColor) {
        switch(rummTileColor) {
            case BLUE : {
                return Color.BLUE;
            }
            case RED : {
                return Color.RED;
            }            
            case YELLOW : {
                return Color.rgb(204, 204, 0);
            }                        
            case BLACK : {
                return Color.BLACK;
            }      
        }   
        
        return null;   
    }
}
