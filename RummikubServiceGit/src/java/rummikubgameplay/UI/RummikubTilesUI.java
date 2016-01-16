/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay.UI;

import javafx.animation.FadeTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.util.Duration;
import rummikubengine.RummikubBoard;
import static rummikubengine.RummikubCustomDataFormat.tileDataFormat;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileColorAdapter;

public class RummikubTilesUI {
    private static final int NUMBER_OF_NODES = 40;
    private RummikubHumanPlayer rummHumanPlayer;
    private final GridPane pane = new GridPane();
    private boolean includeAddedElementFading = false;

    public RummikubTilesUI() {}
    
    public Node createDefaultPane() {
        pane.setAlignment(Pos.CENTER);
        return pane;
    }
    
    public void addSingleTileToBoard(RummikubHumanPlayer rummPlayer) {
        includeAddedElementFading = true;
        addNumbersToPane(rummPlayer);
    }
    
    public Node addNumbersToPane(RummikubHumanPlayer rummPlayer) {
        rummHumanPlayer = rummPlayer;
        pane.setHgap(2);
        pane.setVgap(2);
        pane.setId("playerTilesPane");
        pane.setPadding(new Insets(10));
        createNumbersNodes();
        return pane;
    }

    private Node[] createNumbersNodes() {
        Node[] nodes = new Node[rummHumanPlayer.getPlayerDeckTiles().size()];
        int j = RummikubBoard.PLAYERDECKSIZEINROW;
        for (int i = 0; i < rummHumanPlayer.getPlayerDeckTiles().size(); i++) {
          nodes[i] = createNumberNode(rummHumanPlayer.getPlayerDeckTiles().get(i));
          pane.add(nodes[i], j%RummikubBoard.PLAYERDECKSIZEINROW, j/RummikubBoard.PLAYERDECKSIZEINROW);
          j++;
          
          if (includeAddedElementFading && (i == rummHumanPlayer.getPlayerDeckTiles().size() - 1)) {
            FadeTransition ft = new FadeTransition(Duration.millis(3000), (Label)nodes[i]);
            ft.setFromValue(0.1);
            ft.setToValue(1);
            ft.setAutoReverse(false);
            ft.play();     
            includeAddedElementFading = false;
          }
        }
        return nodes;
    }


    private Node createNumberNode(RummikubTile rummTile) {
        final Label label = new Label(rummTile.getTileNumber().toString() + "");
        label.setPrefSize(35, 50);
        label.setFont(new Font("Comic Sans MS", 14));
        label.setAlignment(Pos.CENTER);
        label.setStyle("-fx-border-color: gray; -fx-border-width: 1");
        label.setTextFill(RummikubTileColorAdapter.colorConvertor(rummTile.getTileColor()));

        label.setOnDragDetected((event) -> {
          WritableImage snapshot = label.snapshot(new SnapshotParameters(), null);
          Dragboard db = label.startDragAndDrop(TransferMode.ANY);
          ClipboardContent content = new ClipboardContent();
          content.put(tileDataFormat, rummTile);
          db.setContent(content);
          db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);
          event.consume();
        });

        label.setOnDragDone((event) -> {
          if (event.getTransferMode() == TransferMode.MOVE) {
            label.setText("");
            pane.getChildren().remove(label);
          }
          event.consume();
        });
        
        return label;
    }    

    public void ClearPane() throws InterruptedException {
        while(!pane.getChildren().isEmpty()) {
            pane.getChildren().remove(0);     
        }       
    }
}
