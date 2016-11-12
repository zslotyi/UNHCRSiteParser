/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xml.sax.SAXException;

/**
 *
 * @author ballaz
 */
public class UNHCRSiteParser extends Application {
 
private ParseSite parsedSite;
private SitemapReader siteMap;
private String SiteURL;
private UserInterface ui;
List<String> lines;
static Thread processingThread;
static UNHCRSiteParser instance;

    private Document parseExample(String url){
            Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
            Elements newsHeadlines = doc.select("#mp-itn b a");
        } catch (IOException ex) {
            Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
    
    
    @Override
    public void start(Stage primaryStage) {
        
     lines = new ArrayList<>();
     ui = UserInterface.getInstance(primaryStage, this);
     instance = this;
    }
    
    /**
     * API Begins
     * @param
     * @return 
     */
    static UNHCRSiteParser getUNHCRSiteParserInstance() {
        return instance;
    }
    void WriteToFile () {
    try {
        Path file = Paths.get("C:/users/ballaz/the-file-name.txt");
        Files.write(file, lines, Charset.forName("UTF-8"));
        //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    } catch (IOException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    void WriteASite() {
        String line = parsedSite.getElementStringValue("title");
                if (line.length()==0)
                {
                    line = "No value for title";
                }
        
        try {
            this.lines.add(line);
            }
        catch (NullPointerException e) {
            ui.setFeedBack("Something went wrong with this line: " + line);
        }
    }
    void parseASite (String url) {
        parsedSite = ParseSite.getInstance(url);
    }
    
    /**
     * Api Ends
     * 
     * @return 
     */
    Thread initProcesser(UserInterface ui){
        Thread t = new Thread(new SiteMapScannerThread(ui));
        return t;
    }
    
    void StartProcessing(UserInterface ui) {
        processingThread=this.initProcesser(ui);
        processingThread.start();
    }
    
    void sfb(String str)
    {
        ui.setFeedBack(str);
    }
    /**
     * Don't use the StartProcessing() method - it was only written for testing purposes.
     * Use the StartProcessing (UserInterface ui); method instead - it will create
     * a separate thread for reading thourgh the SiteMap
     */
    void StartProcessing(){
        sfb("We're strating off");
                
        
        try {
        siteMap = SitemapReader.getInstance("http://www.unhcr-centraleurope.org/pl/ogolne/sitemap.xml");
    } catch (SAXException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    
    
                while ((SiteURL = siteMap.getNextURL()) != null)
                {
                    //SiteURL = siteMap.getNextURL();
                    parseASite(SiteURL);
                    WriteASite();
                        
                    
                }
                WriteToFile();
    } 
    /**
     * 
     */
    
    
    
    
    private void displayASite(Stage primaryStage) {
        
        Label title = displayElement("title", "title");
        Label date = displayElement("p.docDateBar", "date");
        Label author = displayElement("div#content p em ", "author");
        Label city = displayElement("div#content p em ", "city");
        Label country = displayElement("div#content p em ", "country");
        Label photoCredit = displayElement("div.photoCredit","photocredit");
        Label photoCaption = displayElement("div.photoCaption","photocaption");
        Label content = displayHTMLElement("div#openDoc p","content");
        content.setWrapText(true);
        Label imgsrc = displayImgSRC("div.floatedPhoto img");
        Label metadescription = displayMetaDescription();
        
              
       
        
        GridPane root = new GridPane();
        root.add(title,0,0);
        root.add(date,0,1);
        root.add(author,0,2);
        root.add(city,0,3);
        root.add(country,0,4);
        root.add(imgsrc,0,5);
        root.add(photoCredit,0,6);
        root.add(photoCaption,0,7);
        root.add(content,0,8);
        root.add(metadescription,0,9);
        //root.add(formattedContentLabel,0,5);
        
        Scene scene = new Scene(root, 300, 250);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    /**
     * This method will take a String, uses it as a selector against a Document
     * that is already parsed, and will return it as a Label, ready to be displayed
     * @param id
     * @return Label
     */
    private Label displayElement(String id, String name) {
        //Check if we have an instance of ParseSite
        if (parsedSite == null)
        {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
        }
        String labelString = parsedSite.getElementStringValue(id);
                
                if (labelString.length() == 0)
                {
                    labelString = "There is no value for " + name;
                }
                else
                {
                    labelString = processSpecial(name, labelString);
                }
        
               
        
        return new Label(labelString);
        
    }
    /**
     * This private method will return the full html of an element identified by the
     * id that is used as a selector
     * @param id
     * @param name
     * @return 
     */
    private Label displayHTMLElement (String id, String name) {
             //Check if we have an instance of ParseSite
        if (parsedSite == null)
        {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
        }
        String labelString = parsedSite.getElementHTMLContent(id);
                
                if (labelString.length() == 0)
                {
                    labelString = "There is no value for " + name;
                }
                else
                {
                    labelString = processSpecial(name, labelString);
                }
        
               
        
        return new Label(labelString);
    }
    /**
     * This method will return the src attribute of a picture it is pointed at
     * by the id, that is used as a selector
     * @param id
     * @return 
     */
    private Label displayImgSRC(String id) {
         //Check if we have an instance of ParseSite
        if (parsedSite == null)
        {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
        }
        String labelString = parsedSite.getImageSRC(id);
                
                if (labelString.length() == 0)
                {
                    labelString = "There is no src value for " + id;
                }
                
        return new Label(labelString);        
    }
    /**
     * This method will return a Label with the text of the meta description on it
     * @return 
     */
    private Label displayMetaDescription(){
       if (parsedSite == null)
        {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
        }
        String labelString = parsedSite.getMetaDescription();
                
                if (labelString.length() == 0)
                {
                    labelString = "There is no src value for meta description";
                }
                
        return new Label(labelString);  
    }
    
    /**
     * This method will take care values that need special treatment (for example
     * author needs to be stripped by the "By " line etc.
     * @param name
     * @param value 
     */
    private String processSpecial (String name, String value) {
       
        switch(name)
        {
            case "author":
                value = value.replaceAll("By ", "");
                value = value.replaceAll("\\sin\\s.*", "");
                break;
            case "city":
                value = value.replaceAll(".*\\sin\\s", "");
                value = value.replaceAll(",.*","");
                break;
            case "country":
                value = value.replaceAll(".*,\\s", "");
                break;
            case "content":
                value = value.replaceAll("<p class=\"docDateBar.*?</p>","");
                value = value.replaceAll("<p><em>By.*","");
                value = value.replaceAll("<p>&nbsp;</p>","");
                break;
                
                
        }
        
        return value;
    }
    
    /**
     * This method will populate the private parsedSite variable with an instance
     * of a ParseSite that contains the parsed version of the URL passed to the 
     * method
     * @param url 
     */
    
}
