/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public class RummikubPoolOutOfTilesException extends Exception{
    
      public RummikubPoolOutOfTilesException() {}

      public RummikubPoolOutOfTilesException(String message)
      {
         super(message);
      }        
}
