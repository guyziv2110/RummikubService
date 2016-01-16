/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay.UI;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.TranslateTransition;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.collections.ObservableList;
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
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;
import rummikubengine.RummikubBoard;
import static rummikubengine.RummikubCustomDataFormat.tileDataFormat;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTile;
import rummikubengine.RummikubTileColorAdapter;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.RummikubCommand;
import rummikubgameplay.RummikubCommandManager;
import rummikubgameplay.RummikubMoveTileOnBoardCommand;
import rummikubgameplay.RummikubPutTileOnBoardCommand;

public class RummikubBoardUI {

    private final RummikubCommandManager rummikubCommandManager;
    private final RummikubBoard rummikubBoard;
    private RummikubHumanPlayer rummHumanPlayer;
    private final SimpleBooleanProperty dragDone;
    private GridPane pane;
    
    public RummikubBoardUI(RummikubCommandManager rummikubCommandManager,
                           RummikubBoard rummikubBoard) {
        this.rummikubCommandManager = rummikubCommandManager;
        this.rummikubBoard = rummikubBoard;
        dragDone = new SimpleBooleanProperty(false);
    }
    
    public Node createBoard() {
        return createGridPane();
    }
    
    public void setRummPlayer(RummikubHumanPlayer rummHumanPlayer) {
        this.rummHumanPlayer = rummHumanPlayer;
    }
    
    public void fillBoard() {
        if(!rummikubBoard.isBoardEmpty()) {
            List<RummikubTile> tilesOnBoard = rummikubBoard.getTilesPlacedOnBoard();
            for (RummikubTile tile : tilesOnBoard) {
                animateMoveTileToBoard(tile, tile.getTileLine(), tile.getTileColumn(), true);
            }
        }
    }

    public void replaceNodeRowAndCol(final int currentRow,
                                     final int currentCol,
                                     final int toRow,
                                     final int toCol) {
        Node tempNode = getNodeByRowColumnIndex(currentRow, currentCol, pane);
        pane.getChildren().remove(getNodeByRowColumnIndex(currentRow, currentCol, pane));
        pane.add(createCell(), currentCol, currentRow);
        pane.add(tempNode, toCol, toRow); 
    }
    
    public void removeNodeByRowAndIndex(final int row,
                                        final int column) {
        pane.getChildren().remove(getNodeByRowColumnIndex(row, column, pane));
        pane.add(createCell(), column, row);
    }

    private int getColByNode(Node lookupNode) {
        int result = -1;
        ObservableList<Node> childrens = pane.getChildren();
        for(Node node : childrens) {
            if(node == lookupNode){
                result = pane.getColumnIndex(node);
                break;
            }
        }
    
        return result;
    }  
        
    public SimpleBooleanProperty dragDone() {
        return dragDone;
    }     
    
    private int getRowByNode(Node lookupNode) {
        int result = -1;
        ObservableList<Node> childrens = pane.getChildren();
        for(Node node : childrens) {
            if(node == lookupNode){
                result = pane.getRowIndex(node);
                break;
            }
        }
    
        return result;
    }  
    
    private Node getNodeByRowColumnIndex(final int row,
                                        final int column,
                                        GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }    
    
    private Node createGridPane() {
      pane = new GridPane();
      pane.setId("boardTilesPane");
      pane.setHgap(2);
      pane.setVgap(2);
      pane.setAlignment(Pos.CENTER);
      pane.setPadding(new Insets(5));

      for (int i = 0; i < rummikubBoard.getMaxBoardLines(); i++) {
        for (int j = 0; j < rummikubBoard.getMaxBoardCols(); j++) {
          pane.add(createCell(), j, i);
        }
      }

      return pane;
    }    

    private Node createCell() {
      final Label cell = new Label();
      cell.setPrefSize(44, 56);
      cell.setAlignment(Pos.CENTER);
      cell.setFont(new Font("Comic Sans MS", 14));
      cell.setTextFill(Color.web("#FFF"));
      if(cell.getText().length() == 0)
        cell.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #112E40");

      cell.setOnDragOver((event) -> {
          RummikubTile rummTile = (RummikubTile) event.getDragboard().getContent(tileDataFormat);
            if (rummTile != null && cell.getText().length() == 0) {
                event.acceptTransferModes(TransferMode.COPY_OR_MOVE);
            }
            event.consume();
      });

      cell.setOnDragEntered((event) -> {
          if(!event.isDropCompleted() && cell.getText().length() == 0) {
            RummikubTile rummTile = (RummikubTile) event.getDragboard().getContent(tileDataFormat);          
            if (rummTile != null) {
              cell.setStyle("-fx-background-color: green; -fx-border-color: gray;");
            }
            event.consume();
          }
      });

      cell.setOnDragExited((event) -> {
          if(!event.isDropCompleted() && cell.getText().length() == 0) {
            cell.setStyle("-fx-background-color: none");
            cell.setStyle("-fx-background-color: #112E40;-fx-border-color: gray; -fx-border-width: 1");
            event.consume();
          }
      });

      cell.setOnDragDropped((event) -> {
        Dragboard db = event.getDragboard();
        RummikubTile rummTile = (RummikubTile) db.getContent(tileDataFormat);          
        boolean success = false;
        
        if (rummTile != null) {
          cell.setText(rummTile.getTileNumber().toString());
          cell.setTextFill(RummikubTileColorAdapter.colorConvertor(rummTile.getTileColor()));
          cell.setStyle("-fx-background-color: #fff;");
          RummikubCommand command;
          if (!pane.getChildren().contains(event.getGestureSource())) {
                 //command = new RummikubPutTileOnBoardCommand(rummTile.getTileLine(), rummTile.getTileColumn(), getRowByNode(cell), getColByNode(cell));
             /* try {
                  rummikubCommandManager.executeCommand(command,
                          rummikubBoard,
                          rummHumanPlayer,            
                          this);
              } catch (RummikubInvalidMoveException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (RummikubPoolOutOfTilesException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (RummikubTilesPositionCollisionException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              }*/
          }
          else {
              /*
              command = new RummikubMoveTileOnBoardCommand(rummTile.getTileLine(), rummTile.getTileColumn(), getRowByNode(cell), getColByNode(cell));
              try {
                  rummikubCommandManager.executeCommand(command,
                          rummikubBoard,
                          rummHumanPlayer,            
                          this);
              } catch (RummikubInvalidMoveException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (RummikubPoolOutOfTilesException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (RummikubTilesPositionCollisionException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              } catch (IOException ex) {
                  Logger.getLogger(RummikubBoardUI.class.getName()).log(Level.SEVERE, null, ex);
              }      
*/
          }
          dragDone.set(true);;
          dragDone.set(false);;
          success = true;
        }
        event.setDropCompleted(success);
        event.consume();
      });
      
      cell.setOnDragDetected((event) -> {        
        if(cell.getText().length() > 0) {
            WritableImage snapshot = cell.snapshot(new SnapshotParameters(), null);
            Dragboard db = cell.startDragAndDrop(TransferMode.ANY);
            ClipboardContent content = new ClipboardContent();
            RummikubTile rummTile = (RummikubTile)rummikubBoard.getTileByLineAndColumn(getRowByNode(cell), getColByNode(cell));
            content.put(tileDataFormat, rummTile);        
            db.setContent(content);
            db.setDragView(snapshot, snapshot.getWidth() / 2, snapshot.getHeight() / 2);
        }
        event.consume();
      });

      cell.setOnDragDone((event) -> {
        if (event.getTransferMode() == TransferMode.MOVE) {
          cell.setText("");
          cell.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #112E40");
        }
        event.consume();
      });    

      return cell;
    }

    public void animateMoveTileToBoard(RummikubTile rummTile, 
                                       int line, 
                                       int col,
                                       boolean includeMoveAnimation) {
        
        Node nd = getNodeByRowColumnIndex(line, col, pane);
        Label currLabelCell =((Label)nd);
        currLabelCell.setText(rummTile.getTileNumber().toString());
        currLabelCell.setTextFill(RummikubTileColorAdapter.colorConvertor(rummTile.getTileColor()));
        currLabelCell.setStyle("-fx-background-color: #fff;");        
        
        if(includeMoveAnimation) {
              TranslateTransition translateTransition =
                  new TranslateTransition(Duration.millis(2000), currLabelCell);
              translateTransition.setFromX(col * 44 * -1);
              translateTransition.setToX(0);
              translateTransition.setCycleCount(1);
              translateTransition.setAutoReverse(false);
              translateTransition.play();        
        }
    }

    public void animateRemoveTileFromBoard(RummikubTile compTile) {
        Node nd = getNodeByRowColumnIndex(compTile.getTileLine(), compTile.getTileColumn(), pane);
        Label currLabelCell =((Label)nd);
        currLabelCell.setText("");
        currLabelCell.setTextFill(Color.web("#FFF"));
        currLabelCell.setStyle("-fx-border-color: gray; -fx-border-width: 1; -fx-background-color: #112E40");        
    }
}
