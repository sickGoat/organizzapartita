/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import exception.AccountNonValidoExcpetion;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.event.AjaxBehaviorEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import session.facade.AccountFacadeLocal;
import session.stateless.GestoreUtentiLocal;

/**
 *
 * @author antonio
 */
public class RecuperaCredenziali {
    @EJB
    private GestoreUtentiLocal gestoreUtenti;
    
    @EJB
    private AccountFacadeLocal accountFacade;
    
    private String username = "";
    private String email = "";
    private String message = "";
    private List<String> possibili_indirizzi = new ArrayList<String>();
    
    
    /**
     * Creates a new instance of RecuperaCredenziali
     */
    public RecuperaCredenziali() {
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<String> getPossibili_indirizzi() {
        return possibili_indirizzi;
    }

    public void setPossibili_indirizzi(List<String> possibili_indirizzi) {
        this.possibili_indirizzi = possibili_indirizzi;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void checkuser(AjaxBehaviorEvent e){
        System.out.println("username = " + username);
        System.out.println("email = " + email);
        
       possibili_indirizzi = accountFacade.getAccountByUserOrEmail(username, email);
       message = possibili_indirizzi.isEmpty() ? "Nessun account trovato" : "";
    }
    
    public void sendEmail(AjaxBehaviorEvent e,String indirizzo){
        
        possibili_indirizzi.clear();
        boolean successo = true;
        try {
             successo = gestoreUtenti.recuperaCredenziali(indirizzo);
        } catch (AccountNonValidoExcpetion ex) {
             message = "OPS! qualcosa è andato storto";
        }
        
        if(successo)
            message = "Controlla la tua casella di posta elettronica!";
    }

}
