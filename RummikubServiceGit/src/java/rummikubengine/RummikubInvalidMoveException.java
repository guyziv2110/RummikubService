/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

/**
 *
 * @author guy
 */
public class RummikubInvalidMoveException extends Exception{
      private RummikubPlayer rummPlayer;

      public RummikubInvalidMoveException() {}

      public RummikubInvalidMoveException(RummikubPlayer rummPlayer) {
          this.rummPlayer = rummPlayer;
      }

      public RummikubInvalidMoveException(String message)
      {
         super(message);
      }
      
      public RummikubPlayer getPlayer() {
          return this.rummPlayer;
      }      
}
