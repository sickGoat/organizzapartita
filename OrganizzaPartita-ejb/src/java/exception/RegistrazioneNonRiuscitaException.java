/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package exception;

/**
 *
 * @author antonio
 */
public class RegistrazioneNonRiuscitaException extends Exception {
    
    public static final String USERNAME_NON_DISPONIBILE = "Username non disponibile";
    public static final String EMAIL_NON_DISPONIBILE = "Email non disponibile";
    public String message;
    
    public RegistrazioneNonRiuscitaException(String message) {
        this.message = message;
    }
    
    
    
}