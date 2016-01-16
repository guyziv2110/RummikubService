/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

import rummikubgameplay.jaxbobjects.Color;

public enum RummikubTileColor {
    BLUE, RED, YELLOW, BLACK, GREEN;

    // green is internal color - don't convert!
    public Color convertToColor() throws RummikubColorConvertionException {
        switch(this) {
            case BLUE : {
                return Color.BLUE;
            }
            case RED : {
                return Color.RED;
            }            
            case YELLOW : {
                return Color.YELLOW;
            }                        
            case BLACK : {
                return Color.BLACK;
            }     
            // just for serializing.
            case GREEN : {
                return Color.BLACK;
            }     
        }
        
        throw new RummikubColorConvertionException();
    }
    
    
    public ws.rummikub.Color convertToWSColor() throws RummikubColorConvertionException {
        switch(this) {
            case BLUE : {
                return ws.rummikub.Color.BLUE;
            }
            case RED : {
                return ws.rummikub.Color.RED;
            }            
            case YELLOW : {
                return ws.rummikub.Color.YELLOW;
            }                        
            case BLACK : {
                return ws.rummikub.Color.BLACK;
            }     
            // just for serializing.
            case GREEN : {
                return ws.rummikub.Color.BLACK;
            }     
        }
        
        throw new RummikubColorConvertionException();
    }    

}

