/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import java.io.IOException;
import java.io.Serializable;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import session.facade.AccountFacadeLocal;

/**
 *
 * @author antonio
 */

@ManagedBean
@RequestScoped
public class LoginController implements Serializable{
    @EJB
    private AccountFacadeLocal accountFacade;

    /**
     * Creates a new instance of LoginController
     */
    private String username="";
    private String password="";
    private String message = "Username Password errate";
    private boolean autenticazione_fallita = false;
    
    public LoginController() {
    }

    @PostConstruct
    public void init(){
    
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                        .getExternalContext().getRequest();
        try{
            if ( request.isUserInRole("GIOCATORE"))
                redirect("faces/giocatore/index.xhtml?faces-redirect=true");
            else
                if ( request.isUserInRole("GESTORE"))
                redirect("faces/gestore/index.xhtml?faces-redirect=true");
        
        }catch(IOException e){}

    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
       // String crypted = Hashing.sha256().hashString(password, Charsets.UTF_8).toString();
        this.password = password;
    }

    public boolean isAutenticazione_fallita() {
        return autenticazione_fallita;
    }

    public void setAutenticazione_fallita(boolean autenticazione_fallita) {
        this.autenticazione_fallita = autenticazione_fallita;
    }
    
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    public String login() throws IOException{
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extctx = ctx.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) extctx.getRequest();
        try{
        
            request.login(username, password);
            
            if( request.isUserInRole("GIOCATORE") ){
                UtenteSession bean = ctx.getApplication()
                        .evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
                bean.setCurrent_username(username);
                return "giocatore/index.xhtml";
            }else
                if ( request.isUserInRole("GESTORE") )
                    return "gestore/index.xhtml";
            
        }catch(ServletException e){
             
            autenticazione_fallita = true;
            
        }   
        
        return "";
    }
    
   
    private void redirect(String url) throws IOException {
        
        FacesContext.getCurrentInstance().getExternalContext().redirect(url);
    
    }
}
