/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public class RummikubBadXMLFormatException extends Exception {
    public RummikubBadXMLFormatException() {}

    //Constructor that accepts a message
    public RummikubBadXMLFormatException(String message)
    {
       super(message);
    }     
}
