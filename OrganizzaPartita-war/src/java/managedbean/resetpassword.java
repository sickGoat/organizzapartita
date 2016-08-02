/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import entities.ChangePasswordRequest;
import exception.AccountNonValidoExcpetion;
import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import session.facade.ChangePasswordRequestFacadeLocal;
import session.stateless.GestoreUtentiLocal;

/**
 *
 * @author antonio
 */
public class resetpassword {
    @EJB
    private ChangePasswordRequestFacadeLocal changePasswordRequestFacade;
    @EJB
    private GestoreUtentiLocal gestoreUtenti;
    
    private ChangePasswordRequest request;
    private String password;
    private String message ;
    private boolean set_button_abilitato = true;
    /**
     * Creates a new instance of resetpassword
     */
    public resetpassword() {
    }
    @PostConstruct
    public void init(){
        
        Map<String, String> requestMap = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String id_request = (String) (requestMap.get("id_request") != null ? requestMap.get("id_request") : "");
        request = changePasswordRequestFacade.find(Hashing.sha256()
                .hashString(id_request, Charsets.UTF_8).toString());
        if ( passati30Minuti(request.getTime_of_request(),new Date())){
            message = "Link scaduto, ripetere procedura di recupero";
            set_button_abilitato = false;
        }
    }
    
    private boolean passati30Minuti(Date request,Date current){
        
        Calendar past = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        past.setTime(request);
        past.add(Calendar.MINUTE, 30);
        now.setTime(current);
        System.out.println("now = " + now.getTime().toString());
        System.out.println("past = " + past.getTime().toString());
        return past.getTime().before(now.getTime());     
    }
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSet_button_abilitato() {
        return set_button_abilitato;
    }

    public void setSet_button_abilitato(boolean set_button_abilitato) {
        this.set_button_abilitato = set_button_abilitato;
    }
    
    
    
    public void resetPassword(){
        System.out.println("password = " + password);
        try {
            gestoreUtenti.resetPassword(request.getID_account(), password);
            
        } catch (AccountNonValidoExcpetion ex) {
            message = "Qualcosa è andato storto ripeti la procedura"; 
        }
        
        message = "Password aggiornata correttamente";
        set_button_abilitato = false;
    }
    
}
