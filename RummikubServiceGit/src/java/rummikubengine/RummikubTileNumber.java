/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubengine;

public enum RummikubTileNumber {
    T1(1) ,T2(2), T3(3), T4(4), T5(5), T6(6), T7(7), T8(8), T9(9), T10(10), T11(11), T12(12), T13(13), JOKER(0);
    
    private final int number;

    private RummikubTileNumber(int number) {
        this.number = number;
    }

    public int getNumber() {
        return number;
    }
}
