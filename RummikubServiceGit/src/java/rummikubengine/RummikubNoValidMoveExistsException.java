/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public class RummikubNoValidMoveExistsException extends Exception{
      private RummikubPlayer rummPlayer;
      
      public RummikubNoValidMoveExistsException() {}

      public RummikubNoValidMoveExistsException(RummikubPlayer rummPlayer) {
          this.rummPlayer = rummPlayer;
      }
      
      public RummikubNoValidMoveExistsException(String message)
      {
         super(message);
      }    
      
      public RummikubPlayer getPlayer() {
          return this.rummPlayer;
      }      
}
