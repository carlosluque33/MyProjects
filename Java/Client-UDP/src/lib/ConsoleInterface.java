package lib;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ConsoleInterface {
    public static final int DO_NOT_CHANGE =0;
    public static final int TO_UPPPER_CASE =2;
    public static final int TO_LOWER_CASE = 1;
    BufferedReader reader = null;

    public ConsoleInterface() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }
    
    public ConsoleInterface(String scannerText) {
        reader = new BufferedReader(new StringReader(scannerText));
    }
    
    public String readLine(){
        String ret;
        try {
            ret = reader.readLine();
        } catch (IOException ex) {
            throw processError(ex);
        }
        return ret;
    }

  
    public int readInt(String message){
        String line;
        int ret;
        boolean correct=false;
        do{
            System.out.print(message);
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                throw processError(ex);
            }
            correct = line.matches("[0-9]*");
            if(!correct){
                System.out.println("Cal que entris un enter si us plau.");
            }
        }while(!correct);
        ret = Integer.parseInt(line);  
        return ret;        
    }
    
   
    public boolean readYesNo(String missatge, char yesChar, int changeCase){
       boolean correct=false;
       String line;
       char ret;
       do{
            System.out.print(missatge);
            try {
                line = reader.readLine();
            } catch (IOException ex) {
                throw processError(ex);
            }
            correct = line.length()>0;
            if(!correct){
                System.out.println("Cal que escriviu un caracter i premeu "
                        + "'Entrar'");
            }
       }while(!correct);

       if(changeCase==TO_UPPPER_CASE){
            ret = line.toUpperCase().charAt(0);        
       }else if(changeCase==TO_LOWER_CASE){
            ret = line.toLowerCase().charAt(0);        
       }else{
            ret = line.charAt(0);        
       }
        return ret==yesChar;
    }
   
    public void showError(Exception ex){
        System.out.println("Error: " + ex.getLocalizedMessage() );
        Logger.getLogger(ConsoleInterface.class.getName())
                                        .log(Level.SEVERE, null, ex);
    }

    
    public void showError(String message, Exception ex){
        System.out.println("Error: " + message);
        Logger.getLogger(ConsoleInterface.class.getName())
                                        .log(Level.SEVERE, message, ex);
    }
    
    
    public void showError(String message){
        System.out.println("Error: " + message);
    }

    
    public void showMessage(String message){
        System.out.println(message);
    }
    
    
    public boolean isKeyReady(){
        boolean ret=false;
        try {
            ret = reader.ready();
        } catch (IOException ex) {
            throw processError(ex);
        }
        return ret;
            
    }    
    
    
    
    public char read() {
        char character = 0;
        try {
            character = (char)reader.read();
        } catch (IOException ex) {
            throw processError(ex);
        }        
        return character;
    }

    
    private NoSuchElementException processError(Exception ex){
        NoSuchElementException except = new NoSuchElementException(ex.getMessage());
        except.initCause(ex);
        return except;
    }
}

