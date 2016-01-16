/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

// this class is dedicated for future development to make the AI smarter.
public class RummikubGameMoveState {
    private final RummikubBoard rummBoard;
    private RummikubMove rummMove;
    private int rummMovePoints;

    public RummikubGameMoveState(RummikubBoard rummBoard) {
        this.rummBoard = new RummikubBoard(rummBoard);
    }

    public RummikubBoard getRummBoard() {
        return rummBoard;
    }

    public RummikubMove getRummMove() {
        return rummMove;
    }

    public void setRummMove(RummikubMove rummMove) {
        this.rummMove = rummMove;
    }

    public int getRummMovePoints() {
        return rummMovePoints;
    }

    public void setRummMovePoints(int rummMovePoints) {
        this.rummMovePoints = rummMovePoints;
    }
}
