/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay.UI;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.SequentialTransition;
import javafx.animation.Timeline;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class RummikubMessagesUI {
    
    private static Label messageLabel;
    private static final GridPane pane = new GridPane();
    
    public static Node createMessagePane() {
        messageLabel = new Label();
        messageLabel.setText("");
        messageLabel.setId("userMessagesLabel");
        messageLabel.setTextFill(Color.web("#FFF"));
        messageLabel.setFont(new Font("Comic Sans MS", 20));        
        pane.setHgap(2);
        pane.setVgap(2);
        pane.setAlignment(Pos.CENTER);
        pane.setPadding(new Insets(25));
        pane.getChildren().add(messageLabel);

        return pane;
    }
    
    public static void showMessage(String message) {
        messageLabel.setText(message);
        Timeline blinker = createBlinker(messageLabel);
        blinker.setOnFinished(event -> messageLabel.setText(""));
        FadeTransition fader = createFader(messageLabel);
        SequentialTransition blinkThenFade = new SequentialTransition(
                messageLabel,
                blinker,
                fader
        );      
        
        blinkThenFade.play();       
    }
    
   public static void hideMessage() {
        FadeTransition animation = new FadeTransition();
        animation.setNode(messageLabel);
        animation.setDuration(Duration.seconds(0.9));
        animation.setFromValue(1.0);
        animation.setToValue(0.0);
        animation.play();    
        messageLabel.setText("");
    }    
   
    private static Timeline createBlinker(Node node) {
        Timeline blink = new Timeline(
                new KeyFrame(
                        Duration.seconds(0),
                        new KeyValue(
                                node.opacityProperty(), 
                                1, 
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(0.5),
                        new KeyValue(
                                node.opacityProperty(), 
                                0, 
                                Interpolator.DISCRETE
                        )
                ),
                new KeyFrame(
                        Duration.seconds(1),
                        new KeyValue(
                                node.opacityProperty(), 
                                1, 
                                Interpolator.DISCRETE
                        )
                )
        );
        
        blink.setCycleCount(3);
        return blink;
    }

    private static FadeTransition createFader(Node node) {
        FadeTransition fade = new FadeTransition(Duration.seconds(2), node);
        fade.setFromValue(1);
        fade.setToValue(0);
        return fade;
    }    
}
