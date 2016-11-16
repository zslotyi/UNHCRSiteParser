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
public class ErrorHandler {
    /**
     * API begins
     */
    private final UserInterface ui;
    
    static ErrorHandler getInstance (UserInterface useri){
        
            return new ErrorHandler(useri);
        }
    void setError(String str){
        ui.setError(str);
    }
    /**
     * API ends
     */
    /**
     * Private constructor - use the factory method getInstance instead
     */
    private ErrorHandler(UserInterface useri){
        ui = useri;
    }
}
