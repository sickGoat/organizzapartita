/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exception;

import entities.Account;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author antonio
 */
public class PrenotazioneNonRiuscitaException extends Exception {
    
    private String message;
    private List<Account> non_disponibili;
    
    public PrenotazioneNonRiuscitaException() {
        super();
    }

    public PrenotazioneNonRiuscitaException(String message) {
        super();
        this.message = message;
        this.non_disponibili = new ArrayList<>();
    }
    
    public PrenotazioneNonRiuscitaException(String message, List<Account> non_disponibili){
        super();
        this.message = message;
        this.non_disponibili = non_disponibili;
   }

    public List<Account> getNon_disponibili() {
        return non_disponibili;
    }

    public void setNon_disponibili(List<Account> non_disponibili) {
        this.non_disponibili = non_disponibili;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    @Override
    public String getMessage(){
        return message;
    }
   
}   