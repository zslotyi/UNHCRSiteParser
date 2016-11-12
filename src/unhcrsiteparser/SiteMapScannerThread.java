/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import org.xml.sax.SAXException;


/**
 *
 * @author Andi
 */
public class SiteMapScannerThread implements Runnable{

    private final UserInterface ui;
    private SitemapReader siteMap;
    private UNHCRSiteParser unhcrsp;
    private String SiteURL;
    
    SiteMapScannerThread(UserInterface uiface) {
        ui=uiface;
    }
    SiteMapScannerThread(){
        throw new AssertionError("Don't you dare...!");
    }
    @Override
    public void run() {
        unhcrsp = UNHCRSiteParser.getUNHCRSiteParserInstance();
        
          
        Platform.runLater(() -> {
                        ui.setFeedBack("Work in progress...");
                        });
        ReadSiteMap();
        while ((SiteURL = siteMap.getNextURL())!= null)
        {
            Platform.runLater(() -> {
                        ui.setFeedBack(SiteURL);
            });
                        unhcrsp.parseASite(SiteURL);
                        unhcrsp.WriteASite();
            Platform.runLater(() -> {
                ui.setFeedBack("Site done!");
            });
        }                           
        
    }
    private void ReadSiteMap (){
        try {
        siteMap = SitemapReader.getInstance("http://www.unhcr-centraleurope.org/pl/ogolne/sitemap.xml");
    } catch (SAXException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    
    private void LoopThroughSiteMap () {
         while ((SiteURL = siteMap.getNextURL()) != null)
                {
                    System.out.println("asdfélkj");
                    //SiteURL = siteMap.getNextURL();
                    
                    unhcrsp.parseASite(SiteURL);
                    unhcrsp.WriteASite();
                        
                    
                }
                unhcrsp.WriteToFile();
    }
    
}
    
    

