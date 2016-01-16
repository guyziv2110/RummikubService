/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubColorConvertionException;

public class RummikubSystemCommandManager {
    private final List<RummikubSystemCommand> rummCommandsHistory = new ArrayList<RummikubSystemCommand>();    
    public void ExecuteCommand(RummikubSystemCommand rummSystemCommand, 
            RummikubGameSettings rummGameSettings) throws 
            SAXException, 
            RummikubBadTilesStructureException, 
            FileNotFoundException, 
            UnmarshalException, 
            RummikubColorConvertionException,
            RummikubBadXMLFormatException,
            JAXBException{
        
        rummCommandsHistory.add(rummSystemCommand);
        rummSystemCommand.execute(rummGameSettings);
    }  
}
