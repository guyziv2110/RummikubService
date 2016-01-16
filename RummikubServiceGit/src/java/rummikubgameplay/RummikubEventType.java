/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

/**
 *
 * @author guy
 */
public enum RummikubEventType {
    GAME_STARTED, 
    GAME_OVER, 
    GAME_WINNER, 
    PLAYER_TURN, 
    PLAYER_FINISHED_TURN,
    PLAYER_RESIGNED,
    SEQUENCE_CREATED,
    TILE_ADDED,
    TILE_MOVED,
    TILE_RETURNED,
    REVERT;
    
}
