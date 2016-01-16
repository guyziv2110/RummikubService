/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Timer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleBooleanProperty;
import rummikubengine.RummikubBoardValidator;
import rummikubengine.RummikubComputerPlayer;
import rummikubengine.RummikubInvalidMoveException;
import rummikubengine.RummikubNoValidMoveExistsException;
import rummikubengine.RummikubOutOfMovesException;
import rummikubengine.RummikubPlayer;
import rummikubengine.RummikubPoolOutOfTilesException;
import rummikubengine.RummikubTilesPositionCollisionException;

/**
 *
 * @author guy
 */
public class RummikubGameFlowHandler {
    private final SimpleBooleanProperty isGameStarted = new SimpleBooleanProperty(false);
    private RummikubGameSettings rummGameSettings;
    private final RummikubBoardValidator rummBoardValidator;
    private RummikubPlayer currentRummPlayer;
    private RummikubCommandManager rummCommandManager;
    private RummikubAIController rummikubAIController;
    private javax.swing.Timer timer;
    
    public SimpleBooleanProperty isGameStartedProperty() {
        return isGameStarted;
    }  
    
    public RummikubGameFlowHandler() {
        this.rummBoardValidator = new RummikubBoardValidator();
        
        isGameStartedProperty().addListener((obs, ov, nv) -> {
            //startGameHandler();
        });
    }
    
    public void setRummikubGameSettings(RummikubGameSettings rummGameSettings) {
        this.rummGameSettings =  rummGameSettings;
    }
    
    public void setRummikubCommandManager(RummikubCommandManager rummCommandManager) {
        this.rummCommandManager = rummCommandManager;
    }    
    
    public void startGameHandler() {
        rummGameSettings.getRummBoard().getRummikubPlayers().get(0).setIsPlayerTurn(true);
        setNextStepHandler();
        
        /*currentRummPlayer = rummGameSettings.getRummBoard().getPlayerInTurn();
        
        RummikubEventsManager.getInstance().addPlayerTurnEvent(
                            rummGameSettings.getGameId(),   
                            currentRummPlayer.getPlayerName());*/
    }
    
    private void computerPlayTurn() {
        rummikubAIController 
                    = new RummikubAIController(rummGameSettings.getGameId(),
                                               rummGameSettings.getRummBoard());
         
            try {
                rummikubAIController.playTurn((RummikubComputerPlayer)currentRummPlayer);
            } catch (RummikubPoolOutOfTilesException ex) {
                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RummikubNoValidMoveExistsException ex) {
                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RummikubOutOfMovesException ex) {
                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
            } catch (RummikubTilesPositionCollisionException ex) {
                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            
 
        
            new java.util.Timer().schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            try {
                                endTurn(currentRummPlayer.getPlayerId());
                            } catch (RummikubInvalidMoveException ex) {
                                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (RummikubPoolOutOfTilesException ex) {
                                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (RummikubTilesPositionCollisionException ex) {
                                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
                            } catch (IOException ex) {
                                Logger.getLogger(RummikubGameFlowHandler.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }, 
                    8000 
            );        
                
    }
    
    private void humanPlayerTurn() {
            new java.util.Timer().schedule( 
                    new java.util.TimerTask() {
                        @Override
                        public void run() {
                            resign(currentRummPlayer.getPlayerId());
                        }
                    }, 
                    30000 
            );  
    }
    
    private void setNextStepHandler() {
        currentRummPlayer = rummGameSettings.getRummBoard().getPlayerInTurn();
        
        RummikubEventsManager.getInstance().addPlayerTurnEvent(
                            rummGameSettings.getGameId(),   
                            currentRummPlayer.getPlayerName());
        
        if(currentRummPlayer instanceof RummikubComputerPlayer) {
            computerPlayTurn();
        }
        else {
            humanPlayerTurn();
        }
    }
    
    //public void endTurn

    public void endTurn(int playerId) throws 
            RummikubInvalidMoveException, 
            RummikubPoolOutOfTilesException, 
            RummikubTilesPositionCollisionException, 
            IOException {
        // only pick tile because the player didn't do any move.
        if(rummCommandManager.getLastCommand() == null) {
            pickKTilesFromBoard(currentRummPlayer, 1);
        }
        else {
            preformBoardValidation(currentRummPlayer);
            preformPointsValidation(currentRummPlayer);
        }
        
        rummCommandManager.clearCommands();
        
        RummikubEventsManager.getInstance().addFinishedTurnEvent(
                            rummGameSettings.getGameId(),   
                            currentRummPlayer.getPlayerName());        
        
        setNextStepHandler();        
    }
    
    private void preformBoardValidation(RummikubPlayer rummPlayer)
            throws 
            RummikubInvalidMoveException, 
            RummikubPoolOutOfTilesException, 
            RummikubTilesPositionCollisionException, 
            IOException {
        
        if(!rummBoardValidator.validateBoard(rummGameSettings.getRummBoard(), true)) {
            rummCommandManager.undoCommands();
             //rummikubView.showWarningMessage("Your last move(s) was invalid and therefore the game state will be returned to what it was before all your moves. Moreover you will have 3 extra cards in your deck as a punish");
             pickKTilesFromBoard(rummPlayer, 3);            
        }
    }
    
    private void pickKTilesFromBoard(RummikubPlayer rummPlayer, int k) 
            throws 
            RummikubInvalidMoveException, 
            RummikubPoolOutOfTilesException, 
            RummikubTilesPositionCollisionException, 
            IOException {
        
        int i;
        if(rummGameSettings.getRummBoard().isBoardPoolEmpty()) 
            throw new RummikubPoolOutOfTilesException();
        for (i = 0; i < k; i++)
        {
            rummCommandManager.executeCommand(new RummikubPickTileFromDeckCommand(), 
                                       rummGameSettings.getGameId(),
                                       rummGameSettings.getRummBoard(), 
                                       rummPlayer);
        }
    }    
    
    private void preformPointsValidation(RummikubPlayer rummPlayer) {
        if(!rummPlayer.isDidFirstValidMove() && 
            rummPlayer.getPoints() < 30 &&
            rummCommandManager.getLastCommand() != null &&
            !rummCommandManager.getLastCommand().getClass().getName().contains("RummikubPickTileFromDeckCommand")) {
            rummCommandManager.undoCommands();
        }       
    }    

    public void resign(int playerId) {
        RummikubPlayer rummPlayer = rummGameSettings.getRummBoard().getPlayerById(playerId);
        
        rummCommandManager.undoCommands();
        rummGameSettings.getRummBoard().removePlayerById(playerId);
        //set next turn
        RummikubEventsManager.getInstance().addPlayerResignedEvent(
                rummGameSettings.getGameId(), 
                rummPlayer.getPlayerName());
        
        new java.util.Timer().schedule( 
                new java.util.TimerTask() {
                    @Override
                    public void run() {
                        if(rummGameSettings.getRummBoard().getRummikubPlayersCount() == 1)
                        {
                            RummikubEventsManager.getInstance().addGameWinnerEvent(
                                    rummGameSettings.getGameId(), 
                                    rummGameSettings.getRummBoard().getRummikubPlayers().get(0).getPlayerName());                            
                        }
                        else {
                            setNextStepHandler(); 
                        }

                    }
                }, 
                5000 
        );        
        
        
        
        
    }


}
