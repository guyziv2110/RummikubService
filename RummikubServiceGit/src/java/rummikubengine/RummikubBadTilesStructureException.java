/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public class RummikubBadTilesStructureException extends Exception{
    public RummikubBadTilesStructureException() {}

    //Constructor that accepts a message
    public RummikubBadTilesStructureException(String message)
    {
       super(message);
    }    
}
