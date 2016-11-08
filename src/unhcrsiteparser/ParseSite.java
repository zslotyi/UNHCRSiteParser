/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 *
 * @author ballaz
 */
public class ParseSite {

private final Document parsedDocument;
    
    /**
     * Beginning of API
     *  
     */
     /**
      * Public factory method
      * @param url
      * @returns an instance of the ParseSite class
      */
    public static ParseSite getInstance (String url) {
        return new ParseSite(url);
    }
    /**
     * returns an Elements identified by the id string passed to the method
     * @param id
     * @return Elements (with the appropriate selector, identified by the id
     */
    Elements getElement(String id){
        Elements elementToReturn = null;
        
        try {
            elementToReturn = parsedDocument.select(id);
        }
        catch (Exception ex) {
            Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return elementToReturn;
    }
    /**
     * This method returns the string content of an element identified by the id passed
     * to the method, that is used as a selector on the html document that is contained
     * in parsedDocument
     * @param id
     * @return String (the content of the appropriate selector)
     */
    String getElementStringValue(String id) {
        Elements element = getElement(id);
        return element.text();
    }
    /**
     * Returns the full HTML content of the selector passed to the method in the
     * id string
     * @param id
     * @return 
     */
    String getElementHTMLContent(String id){
        Elements element = getElement(id);
        return element.outerHtml();
    }
    /**
     * This method returns the src of an img that is found based on the selector
     * passed to this method
     * @param id
     * @return 
     */
    String getImageSRC(String id){
        Elements element = getElement(id);
        return element.attr("src");
    }
    String getMetaDescription(){
        Elements element = getElement("meta[name=description]");
        return element.attr("content");
    }
    
    /**
     * API ends! private methods for internal use from here. from outside the class
     * use the API to interact with the class!
     */
    
    /**
     * This method parses a URL. The method is for internal use, from outside the class, 
     * use the static factory method instead
     * @param url 
     */
    private ParseSite(String url) {
      parsedDocument = parseDocument(url);  
    }
    /**
     * This is the private constructor of the class. Use the static factory instead!
     * @param url
     * @return 
     */
    private Document parseDocument(String url){
            Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            Logger.getLogger(UNHCRSiteParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return doc;
    }
}
