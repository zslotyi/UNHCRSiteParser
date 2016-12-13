/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package unhcrsiteparser;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.WindowEvent;



public class UserInterface {
    private static UserInterface ui;
    private Label feedBack, error;
    private Button btn;
    private StringProperty feedBackText;
    Task <Void> task ;
    Label countryCodeLabel, feedURLLabel, writeFileLabel, progressLabel;
    TextField countryCode, feedURL, writeFilePath;
    Label newsURLLabel;
    TextField newsURL;
    /**
     * API begins
     * @author Andi
     */
    /**
     * This is a singleton class - the static factory ensures that only one instance is created
     * @param primaryStage
     * @return 
     */
    static UserInterface getInstance(Stage primaryStage, UNHCRSiteParser usp){
        if (ui == null)
        {ui = new UserInterface(primaryStage, usp);}
        return ui;
    }
    void setFeedBack(String str) {
        //feedBackText.set(str);
         task = new Task<Void>() {
            @Override public Void call() throws InterruptedException {
                // "message2" time consuming method (this message will be seen).
               updateMessage(str);

            // some actions
                Thread.sleep(3000);

        return null;
      }
      
         
            
    };
         feedBack.textProperty().bind(task.messageProperty());
         
         task.setOnSucceeded(e -> {
      feedBack.textProperty().unbind();
      
    });
         
         
         
          Thread thread = new Thread(task);
            thread.setDaemon(true);
            thread.start();
    }
    /**
     * This method sets the error message in the Error label
     * @param str 
     */
    void setError(String str){
        error.setText(str);
    }
    void setProgress(String str){
        progressLabel.setText(str);
    }
    String getCountryCode(){
        return countryCode.getText();
    }
    String getNewsURL () {
        return newsURL.getText();
    }
    String getFeedURL(){
        return feedURL.getText();
    }
    String getPath() {
        return writeFilePath.getText();
    }
/**
 * API Ends - private methods from here. to interact with the class use the API methods
 * instead
 */
    /**
     * Constructor is private - use public factory method instead
     * @param primaryStage 
     */
    private UserInterface (Stage primaryStage, UNHCRSiteParser usp){
       btn = new Button("Go!");
       btn.setOnAction((ActionEvent event) -> {
           usp.StartProcessing(this);
       });
       error = new Label();
       
       feedBack = new Label("feedback");
       feedBackText = new SimpleStringProperty();
       //feedBack.textProperty().bind(feedBackText);
       feedBackText.set("asdf");
       
       feedURLLabel = new Label ("Feed URL");
       feedURL = new TextField ("http://www.unhcr-centraleurope.org/en/general/sitemap.xml");
       countryCodeLabel = new Label ("Country Code");
       countryCode = new TextField ("en");
       newsURLLabel = new Label ("News folder url");
       newsURL = new TextField ("news");
       writeFileLabel = new Label ("Where to write the xml?");
       writeFilePath = new TextField ("C:/Users/ballaz/Documents/Website/");
       progressLabel = new Label();
            //Adding image logo
            Image image = new Image(getClass().getResourceAsStream("unhcr-logo.png"));
            Label label1 = new Label();
            label1.setGraphic(new ImageView(image));
            label1.setStyle("-fx-background-color: #ffffff; -fx-min-width:700px;");
       
       GridPane root = new GridPane();
       root.setStyle("-fx-background-color: #0072bc;");
       root.add(btn,0,5);
       root.add(feedURLLabel,0,1);
       root.add(feedURL,1,1);
       root.add(countryCodeLabel,0,3);
       root.add(countryCode,1,3);
       root.add(newsURLLabel,0,4);
       root.add(newsURL,1,4);
       root.add(label1,0,0,2,1);
       root.add(writeFileLabel,0,2);
       root.add(writeFilePath,1,2);
       
       root.add(feedBack,0,7,2,1);
       root.add(error,0,8,2,1);
       root.add(progressLabel,0,9,2,1);
       
        Scene scene = new Scene(root, 700, 600);
        importCSS(scene);
        
        primaryStage.setTitle("UNHCR Website Parser");
        primaryStage.setScene(scene);
        primaryStage.show();
        primaryStage.setOnCloseRequest((WindowEvent e) -> {
             try {
                 Platform.exit();
             } catch (Exception e1) {
                 e1.printStackTrace();
             }
        });
       
    }
    private void importCSS(Scene scene){
        String css = this.getClass().getResource("unhcr.css").toExternalForm();
        scene.getStylesheets().add(css);
    }
}
