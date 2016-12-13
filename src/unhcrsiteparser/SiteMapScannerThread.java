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
    private ErrorHandler error;
    
    SiteMapScannerThread(UserInterface uiface) {
        ui=uiface;
    }
    SiteMapScannerThread(){
        throw new AssertionError("Don't you dare...!");
    }
    @Override
    public void run() {
        unhcrsp = UNHCRSiteParser.getUNHCRSiteParserInstance();
        error = ErrorHandler.getInstance(ui);
        
        ReadSiteMap();
        
        Platform.runLater(() -> {
                        error.setError("Found " + siteMap.getXMLLength() + " urls in sitemap");
                        });
        
        Platform.runLater(() -> {
                        ui.setFeedBack("Work in progress...");
                        });
        
        unhcrsp.beginTheWholeThing();
        int k=0;
        while ((SiteURL = siteMap.getNextURL())!= null)
        {
            k++;
            double c = k;
            double process = c/((double)siteMap.getXMLLength()/100);
        
            Platform.runLater(() -> {
                        ui.setFeedBack(SiteURL);
            });
            Platform.runLater(() -> {
                        
                        ui.setProgress("We are done with " + (int)process + " %");
            });
                        unhcrsp.parseASite(SiteURL);
                        unhcrsp.WriteASite(SiteURL);
            
        }   
            unhcrsp.WriteToFile();
            Platform.runLater(() -> {
                ui.setFeedBack("Site done!");
            });
            
        
    }
    private void ReadSiteMap (){
        try {
        siteMap = SitemapReader.getInstance(ui.getFeedURL());
    } catch (SAXException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
    
    }
    /**
     * This is not the method we use to loop through the site
     */
    private void LoopThroughSiteMap () { int k=0;
         while (((SiteURL = siteMap.getNextURL()) != null) && (k<40))
                {
                    System.out.println("asdfélkj");
                    //SiteURL = siteMap.getNextURL();
                    
                    unhcrsp.parseASite(SiteURL);
                    unhcrsp.WriteASite(SiteURL);
                    
                    k++;
                    
                }
                unhcrsp.WriteToFile();
    }
    
}
    
    

