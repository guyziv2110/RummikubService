/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package rummikubgameplay;

import java.io.FileNotFoundException;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import org.xml.sax.SAXException;
import rummikubengine.RummikubBadTilesStructureException;
import rummikubengine.RummikubBadXMLFormatException;
import rummikubengine.RummikubColorConvertionException;

public interface RummikubSystemCommand {
    public void execute(RummikubGameSettings rummGameSettings) throws 
            SAXException, 
            RummikubBadTilesStructureException, 
            FileNotFoundException, 
            RummikubColorConvertionException, 
            UnmarshalException,
            RummikubBadXMLFormatException,
            JAXBException;
}
