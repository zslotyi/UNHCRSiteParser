/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

import org.w3c.dom.*;
import javax.xml.parsers.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.xml.sax.SAXException;

/**
 *
 * @author ballaz
 */
public class SitemapReader {

    private Document doc;
    private NodeList urls;
    private int k;
    
    /**
     * This is a public factory method that returns an instance of the SitemapReader
     * @param url
     * @return
     * @throws SAXException 
     */
    static SitemapReader getInstance(String url) throws SAXException  {
       
            return new SitemapReader(url);  
    }
    
    /**
     * It will loop through the given XML file and returns the next URL in the 
     * sitemap, or null, if the sitemap is over
     * @return 
     */
    String getNextURL () {
        if (urls == null)
        {
            this.getURLList();
        }
        
        if (k < urls.getLength())
        {
            
            Element e = (Element)urls.item(k);
             
                
                  String urlString =    e.getElementsByTagName("loc")
                       .item(0)
                       .getTextContent(); 
             k++;     
                  return urlString;
                
        }
        else
        {
            return null;
        }
    }
    /**
     * It returns the number of items in the sitemap (ie. the number of urls to
     * process)
     * @return 
     */
    int getXMLLength()
    {
        return urls.getLength();
    }
    /**
     * API ends here, from here on methods are for internal use only
     */
    
    /**
     * Constructor is private, from outside the class use the static factory instead
     * @param url
     * @throws SAXException 
     */
    private SitemapReader(String urlString) throws SAXException{
       
        try {
            URL url = new URL(urlString);
            URLConnection conn = url.openConnection();
            
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.parse(conn.getInputStream());
            k = 0;
            this.getURLList();
            
           
        } catch (Exception ex) {
            Logger.getLogger(SitemapReader.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void getURLList() {
        
        urls  = doc.getElementsByTagName("url");
        
    }
    
    /**
     * main method served testing purposes solely
     * @param args
     * @throws SAXException 
     */
   /*
        public static void main(String[] args) throws SAXException {
        SitemapReader self = getInstance("http://www.unhcr-centraleurope.org/pl/ogolne/sitemap.xml");
        
        
        
        urls = self.getURLList();
        int i;
        
        for (i=0;i<urls.getLength();i++)
        {
            Element e = (Element)urls.item(i);
             
            System.out.println(
                    e.getElementsByTagName("loc")
                    .item(0)
                    .getTextContent());
        }
        System.out.println("Number of elements:" + i);
    }
    */
}
