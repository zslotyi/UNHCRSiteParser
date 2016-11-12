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
import javafx.beans.InvalidationListener;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.scene.layout.StackPane;



public class UserInterface {
    private static UserInterface ui;
    private Label feedBack;
    private Button btn;
    private StringProperty feedBackText;
    Task <Void> task ;
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
 * API Ends - private methods from here. to interact with the class use the API methods
 * instead
 */
    /**
     * Constructor is private - use public factory method instead
     * @param primaryStage 
     */
    private UserInterface (Stage primaryStage, UNHCRSiteParser usp){
       btn = new Button("Button");
       btn.setOnAction((ActionEvent event) -> {
           usp.StartProcessing(this);
       });
       feedBack = new Label("feedback");
       feedBackText = new SimpleStringProperty();
       //feedBack.textProperty().bind(feedBackText);
       feedBackText.set("asdf");
       
       GridPane root = new GridPane();
       root.add(btn,0,0);
       root.add(feedBack,0,1);
       System.out.println("hozzáadtuk");
       
        Scene scene = new Scene(root, 400, 400);
        
        primaryStage.setTitle("Hello World!");
        primaryStage.setScene(scene);
        primaryStage.show();
       
    }
    
}
