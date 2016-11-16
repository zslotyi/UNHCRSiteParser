/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

/**
 *
 * @author ballaz
 */
public class GenerateXML {
    
/**
 * API begins
 * @return 
 */    
private String title, date, id, titleURL, formattedContent, countryCode, news;

static GenerateXML getInstance() {
    return new GenerateXML();
}

void setTitle(String titleText)
{
    title = titleText;
}
void setFormattedContent (String str) {
    formattedContent = str;
}
void setID (String str) {
    id = str;
}
void setDate (String str) {
    date = str;
}
void setURL (String str) {
    titleURL = str;
}
void setCountryCode (String str) {
    countryCode = str;
}
void setNews (String str) {
    news = str;
}

    String getString () {
        String str = 
"<item>\n" +
"<title>\n" +
"" + title + "\n" +
"</title>\n" +
"<link>\n" +
"http://www.unhcr.org/" + countryCode + "/" + id + "-" + titleURL + "\n" +
"</link>\n" +
"<pubDate>" + date + " +0000</pubDate>\n" +
"<dc:creator>\n" +
"<![CDATA[ ballaz@unhcr.org ]]>\n" +
"</dc:creator>\n" +
"<guid isPermaLink=\"false\">http://www.unhcr.org/" + countryCode + "/?p=" + id + "</guid>\n" +
"<description/>\n" +
"<content:encoded>\n" +
"<![CDATA[\n" +
"" + formattedContent + "\n" +
"]]>\n" +
"</content:encoded>\n" +
"<excerpt:encoded>\n" +
"<![CDATA[ ]]>\n" +
"</excerpt:encoded>\n" +
"<wp:post_id>" + id + "</wp:post_id>\n" +
"<wp:post_date>\n" +
"<![CDATA[ " + date + " ]]>\n" +
"</wp:post_date>\n" +
"<wp:post_date_gmt>\n" +
"<![CDATA[ " + date + " ]]>\n" +
"</wp:post_date_gmt>\n" +
"<wp:comment_status>\n" +
"<![CDATA[ closed ]]>\n" +
"</wp:comment_status>\n" +
"<wp:ping_status>\n" +
"<![CDATA[ closed ]]>\n" +
"</wp:ping_status>\n" +
"<wp:post_name>\n" +
"<![CDATA[\n" +
"" + titleURL + "\n" +
"]]>\n" +
"</wp:post_name>\n" +
"<wp:status>\n" +
"<![CDATA[ publish ]]>\n" +
"</wp:status>\n" +
"<wp:post_parent>0</wp:post_parent>\n" +
"<wp:menu_order>0</wp:menu_order>\n" +
"<wp:post_type>\n" +
"<![CDATA[ " + news + " ]]>\n" +
"</wp:post_type>\n" +
"<wp:post_password>\n" +
"<![CDATA[ ]]>\n" +
"</wp:post_password>\n" +
"<wp:is_sticky>0</wp:is_sticky>\n" +
"<wp:postmeta>\n" +
"<wp:meta_key>\n" +
"<![CDATA[ _edit_last ]]>\n" +
"</wp:meta_key>\n" +
"<wp:meta_value>\n" +
"<![CDATA[ 3 ]]>\n" +
"</wp:meta_value>\n" +
"</wp:postmeta>\n" +
"</item>";
    
    return str;
    }
    
    
    private GenerateXML () {
        
        
    }
    
    
}
