/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.FileNotFoundException;
import java.io.IOException;
import javafx.beans.property.SimpleBooleanProperty;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBoard;
import rummikubengine.RummikubBoardValidator;
import rummikubengine.RummikubColorConvertionException;
import rummikubengine.RummikubHumanPlayer;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubOutOfMovesException;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTilesPositionCollisionException;
import rummikubgameplay.UI.RummikubGamePlayMenuUI;
import rummikubgameplay.UI.RummikubMessagesUI;
import rummikubgameplay.UI.RummikubPointsUI;
import rummikubgameplay.UI.RummikubTilesUI;

public class RummikubHumanController {
    private final RummikubBoard rummikubBoard;
    private final RummikubCommandManager rummikubCommandManager;
    private final RummikubBoardValidator rummikubBoardValidator;
    private final RummikubSystemCommandManager rummSysCommandManager;
    private final RummikubGameSettings rummGameSettings;  
    private RummikubHumanPlayer currentPlayingPlayer;
    private final SimpleBooleanProperty commandDone;
    private final SimpleBooleanProperty finalizeDone;
    private final boolean endTurn;
    private final boolean allowEndTurn;
    private boolean firstLoad;
        
    public RummikubHumanController(RummikubBoard rummikubBoard, RummikubGameSettings rummGameSettings, RummikubCommandManager rummCommandManager) {
        this.rummSysCommandManager = new RummikubSystemCommandManager();
        this.rummikubBoardValidator = new RummikubBoardValidator();
        this.rummikubCommandManager = rummCommandManager;
        this.rummikubBoard = rummikubBoard;
        this.rummGameSettings = rummGameSettings;
        commandDone = new SimpleBooleanProperty(false);
        finalizeDone = new SimpleBooleanProperty(false);
        endTurn = false;
        allowEndTurn = false;
        firstLoad = true;
    }
    
    public SimpleBooleanProperty commandDone() {
        return commandDone;
    }     

    public SimpleBooleanProperty finalizeDone() {
        return finalizeDone;
    }         
    
    public void playTurn(RummikubHumanPlayer rummHumanPlayer, RummikubTilesUI rummTilesUI,
                        RummikubGamePlayMenuUI rummGamePlayMenuUI, RummikubPointsUI rummPointsUI) throws 
            RummikubPoolOutOfTilesException, 
            RummikubInvalidMoveException, 
            IOException, RummikubOutOfMovesException, 
            RummikubBadTilesStructureException, 
            SAXException, FileNotFoundException, 
            UnmarshalException, 
            RummikubColorConvertionException, 
            RummikubTilesPositionCollisionException,
            JAXBException, InterruptedException{
        
        commandDone.set(false);
        finalizeDone.set(false);
        rummTilesUI.ClearPane();
        rummTilesUI.addNumbersToPane(rummHumanPlayer);
        /*rummGamePlayMenuUI.setPlayerBox(rummHumanPlayer);
        rummGamePlayMenuUI.setCommandManager(rummSysCommandManager);
        rummGamePlayMenuUI.ClearBox();
        rummGamePlayMenuUI.createGamePlayMenu();*/
        rummPointsUI.setHumanPlayer(rummHumanPlayer);
        
        if (firstLoad) {
            firstLoad = false;   
 
            /*rummGamePlayMenuUI.commandDone().addListener((source, oldValue, newValue) -> {
                    if (newValue) {
                        try {
                            // final validations.
                            preformBoardValidation(rummHumanPlayer);
                        } catch (RummikubInvalidMoveException ex) {
                            RummikubMessagesUI.showMessage("Invalid move");
                        } catch (RummikubPoolOutOfTilesException ex) {
                            RummikubMessagesUI.showMessage("The pool is empty!");
                        } catch (RummikubTilesPositionCollisionException ex) {
                            RummikubMessagesUI.showMessage("Unable to move tile to the destination position: there is already tile in the destination position.");
                        } catch (IOException ex) {
                            RummikubMessagesUI.showMessage(ex.getMessage());
                        }
                        
                        preformPointsValidation(rummHumanPlayer);
                        rummikubCommandManager.clearCommands();                         
                        finalizeDone.set(true);
                    }
            });  */          
               
        }
    }
    
    private void preformBoardValidation(RummikubHumanPlayer rummPlayer) throws RummikubInvalidMoveException, RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException, IOException {
        if(!rummikubBoardValidator.validateBoard(rummikubBoard, true)) {
            rummikubCommandManager.undoCommands();
             RummikubMessagesUI.showMessage("Your last move(s) was invalid and therefore the game state will be returned to what it was before all your moves. Moreover you will have 3 extra cards in your deck as a punish");
             pickKTilesFromBoard(rummPlayer, 3);            
        }
    }
    
    private void pickKTilesFromBoard(RummikubHumanPlayer rummHumanPlayer, int k) throws RummikubInvalidMoveException, RummikubPoolOutOfTilesException, RummikubTilesPositionCollisionException, IOException {
        int i;
        if(rummikubBoard.isBoardPoolEmpty()) throw new RummikubPoolOutOfTilesException();
        for (i = 0; i < k; i++)
        {
            /*rummikubCommandManager.executeCommand(new RummikubPickTileFromDeckCommand(), 
                                       rummikubBoard, 
                                       rummHumanPlayer,
                                       null);*/
        }
    }    
    
    private void preformPointsValidation(RummikubHumanPlayer rummHumanPlayer) {
        if(!rummHumanPlayer.isDidFirstValidMove() && 
            rummHumanPlayer.getPoints() < 30 &&
            rummikubCommandManager.getLastCommand() != null &&
            !rummikubCommandManager.getLastCommand().getClass().getName().contains("RummikubPickTileFromDeckCommand")) {
            rummikubCommandManager.undoCommands();
            RummikubMessagesUI.showMessage("Your first move must be atleast 30 points.");
        }       
    }
}