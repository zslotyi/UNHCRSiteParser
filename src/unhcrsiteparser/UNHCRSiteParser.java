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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
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
import org.jsoup.safety.Whitelist;
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
private ErrorHandler error;
private int id;
private String extraline;

/**
 * This is just for testing purposes - go ahead and ignore this method
 * @param url
 * @return 
 */    
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
     error = ErrorHandler.getInstance(ui);
     
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
        String cC = ui.getCountryCode();
        Path file = Paths.get(ui.getPath() + cC + ".xml");
        Files.write(file, lines, Charset.forName("UTF-8"));
        //Files.write(file, lines, Charset.forName("UTF-8"), StandardOpenOption.APPEND);
    } catch (IOException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
    }
    /**
     * This will generate the first part of the XML file, with version info and general
     * ite info
     * TODO: make text fields to generalize this part!!!
     */
    void beginTheWholeThing() {
        GenerateXML xml = GenerateXML.getInstance();
        this.lines.add(xml.beginTheWholeThing());
    }
    void WriteASite(String URL) {
        /**
         * We are processing the input of the xml item by item here
         */
        
        if (parsedSite.getElementHTMLContent(".error404").length() != 0)
        {
            System.out.println("This was a 404 " + URL + parsedSite.getElementHTMLContent("body.error404"));
        }
        else
        {
                GenerateXML xml = GenerateXML.getInstance();

                xml.setCountryCode(ui.getCountryCode());
                xml.setDate(displayDate());
                    xml.setFormattedContent(displayContent());
                /** This is the format for news items
                 * xml.setFormattedContent(displayHTMLElement("content","content"));
                 * This is another format
                 * xml.setFormattedContent(displayHTMLElement("div.contentText","content"));
                 */
                xml.setID("" + id++);
                xml.setNews(setIfNews(URL));
                xml.setTitle(parsedSite.getElementStringValue("title"));
                xml.setURL(URL.replaceAll("http://www.unhcr-centraleurope.org/", ""));


                String line = xml.getString(this.extraline);

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
    private String setIfNews(String URL)
    {
        if (URL.contains(ui.getNewsURL()))
                {
                    return "post";
                }
       
                    return "page";
  
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
          siteMap = SitemapReader.getInstance("http://www.unhcr-centraleurope.org/en/general/sitemap.xml");  
        //siteMap = SitemapReader.getInstance("http://www.unhcr-centraleurope.org/pl/ogolne/sitemap.xml");
    } catch (SAXException ex) {
        Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
    }
        
    
    int k = 0;
                while (((SiteURL = siteMap.getNextURL()) != null) && (k < 11))
                {
                    //SiteURL = siteMap.getNextURL();
                    parseASite(SiteURL);
                    WriteASite(SiteURL);
                        k++;
                    
                }
                WriteToFile();
    } 
    /**
     * 
     */
    
    
    
    
    /**
     *  This method served testing purposes only. don't uncomment it, as it will
     * not work!!!!!
     * 
     * 
     * 
     * private void displayASite(Stage primaryStage) {
        
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
    private String displayElement(String id, String name) {
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
        
               
        
        return labelString;
        
    }
    /**
     * This private method will return the full html of an element identified by the
     * id that is used as a selector
     * @param id
     * @param name
     * @return 
     */
    private String displayContent () {
          if (parsedSite == null)
            {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
            }
        
        String content; 
            content = parsedSite.getElementHTMLContent("div.content");
            if (content.length() == 0)
            {
                content = parsedSite.getElementHTMLContent("p.doc");
            }
            if (content.length() == 0)
            {
                content = parsedSite.getElementHTMLContent("div#content");
            }
            if (content.length() == 0)
            {
                content = parsedSite.getElementHTMLContent("div#contentText");
            }
            if (content.length() == 0)
            {
                content = "Still no content";
            }
            
            content = content.replaceAll("_assets/", "http://www.unhcr-centraleurope.org/_assets/");
            testForImage(content);
            
            content = cleanContent(content);
            content = Jsoup.clean(content, Whitelist.relaxed().preserveRelativeLinks(true));
            
            content = content.replaceAll("<div>\\s+<a><img alt=\"\">Print this.*?</div>","");
            
            
            //System.out.println(content);
            
        
        return content;
    }
    /**
     * This method will format the content to match the new site's criteria, and will
     * remove unnecessary elements, like breadcrumbs, print this stuff etc.
     * @param content
     * @return 
     */
    private String cleanContent(String content) {
        
        Document d = Jsoup.parse(content);
        
        d.select("h1").remove();
        //content = content.replaceAll(h1, "");
        
        d.select("h2").remove();
        //content = content.replaceAll(h2, "");
        
        d.select("div#contentExport").remove();
        
        d.select("div#divBreadcrumb").remove();
        
        d.select("img.catPhoto").remove();
        
        d.select("div.splashCaption").remove();
        
       // content = content.replaceAll(printthis, "");
        
        return d.outerHtml();
    }
    /**
     * This method will extract the date from a parsed site. If it doesn't have a
     * date field, it will assign the current date and return as such
     * @return 
     */
    private String displayDate() {
        if (parsedSite == null)
            {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
            }
        
        String date;
        date = parsedSite.getElementStringValue("p.docDateBar");
        
            if (date.length()==0)
            {
                SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date now = new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1));
                date = sdfDate.format(now);
            }
        
        return date;
    }
    private String displayHTMLElement (String id, String name) {
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
        
               
        
        return labelString;
    }
    /**
     * This method will return the src attribute of a picture it is pointed at
     * by the id, that is used as a selector
     * @param id
     * @return 
     */
    private String displayImgSRC(String id) {
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
                
        return labelString;        
    }
    /**
     * This method will return a Label with the text of the meta description on it
     * @return 
     */
    private String displayMetaDescription(){
       if (parsedSite == null)
        {
            throw new AssertionError("We should have a parsedSite set by the time we want to display an element as a label");
        }
        String labelString = parsedSite.getMetaDescription();
                
                if (labelString.length() == 0)
                {
                    labelString = "There is no src value for meta description";
                }
                
        return labelString;  
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
    
    private void testForImage (String str) {
        
        Document d = Jsoup.parse(str);
        
        Elements e = d.select(".catPhoto");
        
            if ((e != null) && (e.outerHtml().length()!=0)) {
                System.out.println("van kategóriakép ");
                      
                             System.out.println(e.attr("src") + "\n");
                String url = e.attr("src");
                String filename = url.replaceAll(".*/", "");
                String name = filename.replaceAll("\\..*", "");
                            System.out.println(name + "\n");
                String title = e.attr("alt");
                    if (title.length()==0)
                    {
                        title = name;
                    }
                            System.out.println(title);
                /**
                 * We do have a background image, now we do 2 things
                 * 
                 * 1., add an <item> to the XML with post type attachment, and the
                 * details of the image
                 * 
                 * 2., create an extra line for the posts <item> xml, that will
                 * create a _thumbnail_id meta_key with the value of the id of
                 * the attachment item we have just created.
                 * 
                 * These two steps will link up the two items in XML appropriately
                 */
                GenerateXML xml = GenerateXML.getInstance();
                xml.setCountryCode(ui.getCountryCode());
                xml.setDate(displayDate());
                xml.setID("" + id);
                
                this.lines.add(xml.addBackgroundImageXML(name, filename, url, title));
                
                this.extraline = 
                        "<wp:postmeta>\n" +
                        "<wp:meta_key><![CDATA[_thumbnail_id]]></wp:meta_key>\n" +
                        "<wp:meta_value><![CDATA[" + id + "]]></wp:meta_value>\n" +
                        "</wp:postmeta>";
                id++;
                
            }
            else
            {
                System.out.println("nincs kategóriakép\n");
                this.extraline = "";
            }
        
    }
}
