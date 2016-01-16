/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay.UI;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javax.xml.bind.JAXBException;
import org.xml.sax.SAXException;
//import rummikub.javafx.components.MenuButtonView;
import rummikubengine.RummikubSaveType;
//import rummikub.javafx.utils.RummikubMessages;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.RummikubCommand;
import rummikubgameplay.RummikubCommandManager;
import rummikubgameplay.RummikubGameSettings;
import rummikubgameplay.RummikubPickTileFromDeckCommand;
import rummikubgameplay.RummikubSaveGameCommand;
import rummikubgameplay.RummikubSystemCommandManager;

public class RummikubGamePlayMenuUI {
    private final VBox menuBox;
    private RummikubHumanPlayer rummHumanPlayer;
    private final RummikubCommandManager rummikubCommandManager;
    private final RummikubBoard rummikubBoard;
    private final RummikubTilesUI rummTilesUI;
    private boolean allowEndTurn;
    private final SimpleBooleanProperty commandDone;
    private final RummikubBoardUI rummBoardUI;
    private RummikubSystemCommandManager rummSysCommandManager;
    private final RummikubGameSettings rummGameSettings;  
    //private MenuButtonView pickTileMenuButton;
    //private MenuButtonView saveasGameMenuButton;
    //private MenuButtonView saveGameMenuButton;
    
    public RummikubGamePlayMenuUI(RummikubCommandManager rummikubCommandManager,
                           RummikubBoard rummikubBoard,
                           RummikubTilesUI rummTilesUI,
                           RummikubBoardUI rummBoardUI,
                           RummikubGameSettings rummGameSettings) {
        this.menuBox = new VBox();
        this.rummikubCommandManager = rummikubCommandManager;
        this.rummikubBoard = rummikubBoard;
        this.rummTilesUI = rummTilesUI;
        this.rummBoardUI = rummBoardUI;
        this.rummGameSettings = rummGameSettings;
        
        commandDone = new SimpleBooleanProperty(false);
        allowEndTurn=false;        
    }
 /*
    public Node createGamePlayMenu() {
       commandDone.set(false);
        allowEndTurn= false;
        pickTileMenuButton = new MenuButtonView("Pick Tile", null);
        menuBox.getChildren().add(pickTileMenuButton);
        menuBox.setMargin(pickTileMenuButton,new Insets(10,10,10,10));
        saveasGameMenuButton = new MenuButtonView("Save Game as", null);
        menuBox.getChildren().add(saveasGameMenuButton);
        menuBox.setMargin(saveasGameMenuButton,new Insets(10,10,10,10));
        saveGameMenuButton = new MenuButtonView("Save Game", null);
        menuBox.getChildren().add(saveGameMenuButton);  
        
        if (rummGameSettings.getSettingsFilePath() == null || 
            rummGameSettings.getSettingsFilePath().isEmpty()) {
            saveGameMenuButton.setDisable(true);
        }

        menuBox.setMargin(saveGameMenuButton,new Insets(10,10,10,10));
        menuBox.setPadding(new Insets(50));
        
        rummBoardUI.dragDone().addListener((source, oldValue, newValue) -> {
                    if (newValue) {
                        allowEndTurn = true;
                        pickTileMenuButton.changeButtonText("End Turn");   
                    }
        });
        
        saveasGameMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                saveGameHandler(RummikubSaveType.SAVEAS);
            
            }
        });        

        saveGameMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                if(rummGameSettings != null && 
                   rummGameSettings.getSettingsFilePath() != null &&
                   !rummGameSettings.getSettingsFilePath().isEmpty()) {
                    saveGameHandler(RummikubSaveType.SAVE);
                }
            }
        });            
    
        pickTileMenuButton.setOnMouseClicked(new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent event) {
                    RummikubCommand command =  new RummikubPickTileFromDeckCommand();
                        if (!allowEndTurn) {
                            allowEndTurn = true;
                            pickTileMenuButton.changeButtonText("End Turn");
                            
                        try {
                            rummikubCommandManager.executeCommand(command,
                                    rummikubBoard,
                                    rummHumanPlayer,null);
                        } catch (RummikubInvalidMoveException ex) {
                            Logger.getLogger(RummikubGamePlayMenuUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RummikubPoolOutOfTilesException ex) {
                            Logger.getLogger(RummikubGamePlayMenuUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (RummikubTilesPositionCollisionException ex) {
                            Logger.getLogger(RummikubGamePlayMenuUI.class.getName()).log(Level.SEVERE, null, ex);
                        } catch (IOException ex) {
                            Logger.getLogger(RummikubGamePlayMenuUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                        try {
                            rummTilesUI.ClearPane();
                        } catch (InterruptedException ex) {
                            Logger.getLogger(RummikubGamePlayMenuUI.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        rummTilesUI.addSingleTileToBoard(rummHumanPlayer);                        
                            
                        }
                        else {
                            commandDone.set(true);
                        }
            }
        });
        
        return menuBox;

    }


    public void setPlayerBox(RummikubHumanPlayer rummHumanPlayer) {
        this.rummHumanPlayer = rummHumanPlayer;
    }
    
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }         

    public void ClearBox() {
        while(!menuBox.getChildren().isEmpty()) {
            menuBox.getChildren().remove(0);     
        }
    }
    
    private void saveGameHandler(RummikubSaveType saveType) {
        RummikubSaveGameCommand r = new RummikubSaveGameCommand();
        File fileToSave;
        
        if(saveType == RummikubSaveType.SAVEAS) {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
            fileChooser.getExtensionFilters().add(extFilter);
            fileToSave = fileChooser.showSaveDialog(RummikubMessages.getRoot().getScene().getWindow());    
        }
        else {
            fileToSave = new File(rummGameSettings.getSettingsFilePath());
        }
                    
        if (fileToSave == null) return;
        Thread t1 = new Thread(new Runnable() {
            public void run() {
                try {
                    String saveFile = fileToSave.getPath();
                    // sets the XML file to the rummikub game settings.
                    rummGameSettings.setSettingsFilePath(saveFile);                            
                    if (rummGameSettings.getSettingsFilePath() != null &&
                         !rummGameSettings.getSettingsFilePath().isEmpty()) {
                         saveGameMenuButton.setDisable(false);
                     }                         
                    rummSysCommandManager.ExecuteCommand(r, rummGameSettings);                    
                } catch (SAXException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", ex.getMessage());
                } catch (RummikubBadTilesStructureException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", "The tile structure in your file is incorrect.");
                } catch (FileNotFoundException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", ex.getMessage());
                } catch (RummikubColorConvertionException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", "Couldn't convert one of your color tiles. Make sure you are using the correct colors.");
                } catch (RummikubBadXMLFormatException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", "Your XML file is not valid!.");
                } catch (JAXBException ex) {
                    RummikubMessages.WriteExceptionLater("userMessagesLabel", ex.getMessage());
                }
            }});
         t1.setDaemon(true);
         t1.start();      
    }    

    public void setCommandManager(RummikubSystemCommandManager rummSysCommandManager) {
        this.rummSysCommandManager = rummSysCommandManager;
    }
*/
}
