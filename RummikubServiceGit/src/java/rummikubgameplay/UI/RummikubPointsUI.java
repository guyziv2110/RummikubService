/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay.UI;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import rummikubengine.RummikubHumanPlayer;
import rummikubgameplay.RummikubHumanController;

public class RummikubPointsUI {
    
    private final HBox pointsBox;
    private final RummikubGamePlayMenuUI rummGamePlayMenuUI;
    private final RummikubHumanController currHumanControllerRef;
    private RummikubHumanPlayer rummHumanPlayer;
    private Label lblPointsText;
    private Label lblPointsValue;
    
    public RummikubPointsUI(RummikubHumanController rummHumanController,
                            RummikubGamePlayMenuUI rummGamePlayMenuUI) {
        this.pointsBox = new HBox();
        this.rummGamePlayMenuUI = rummGamePlayMenuUI;
        this.currHumanControllerRef = rummHumanController;
    }
    
    public void setHumanPlayer(RummikubHumanPlayer rummHumanPlayer) {
        this.rummHumanPlayer = rummHumanPlayer;
        lblPointsValue.setText(Integer.toString(rummHumanPlayer.getPoints()));
    }
        
    public Node createPointsMenu() {
        lblPointsText = new Label();
        lblPointsText.setText("Points : ");
        lblPointsText.setTextFill(Color.web("#FFF"));
        lblPointsText.setFont(new Font("Comic Sans MS", 20));
        lblPointsValue = new Label();
        lblPointsValue.setText("");
        lblPointsValue.setTextFill(Color.web("#FFF"));
        lblPointsValue.setFont(new Font("Comic Sans MS", 20));        
        pointsBox.setAlignment(Pos.CENTER);
        pointsBox.getChildren().addAll(lblPointsText, lblPointsValue);    
  
        currHumanControllerRef.finalizeDone().addListener((source, oldValue, newValue) -> {
                if (newValue) {
                    lblPointsValue.setText(Integer.toString(rummHumanPlayer.getPoints()));
                }
        });         
        
        return pointsBox;
    }
}
