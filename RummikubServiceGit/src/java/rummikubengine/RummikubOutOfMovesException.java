/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public class RummikubOutOfMovesException extends Exception{
      private RummikubPlayer rummPlayer;
      
      public RummikubOutOfMovesException() {}

      public RummikubOutOfMovesException(RummikubPlayer rummPlayer) {
          this.rummPlayer = rummPlayer;
      }
            
      public RummikubOutOfMovesException(String message)
      {
         super(message);
      }    
      
      public RummikubPlayer getPlayer() {
          return this.rummPlayer;
      }
}
