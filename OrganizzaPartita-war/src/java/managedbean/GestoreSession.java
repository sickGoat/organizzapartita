/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Gestore;
import java.security.Principal;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import session.facade.AccountFacadeLocal;
import session.facade.GestoreFacadeLocal;

/**
 *
 * @author antonio
 */
public class GestoreSession {
    
    @EJB
    private AccountFacadeLocal accountFacade;
    @EJB
    private GestoreFacadeLocal gestoreFacade;
    private Gestore current_gestore;
    private String  current_username;
    /**
     * Creates a new instance of GestoreSession
     */
    public GestoreSession() {          
    }
    
    @PostConstruct
    public void init(){
    
        Principal user = getUserPrincipal();
        current_username = user.getName();
        current_gestore = gestoreFacade.getGestore(current_username);
        
    }

    public Gestore getCurrent_gestore() {
        return current_gestore;
    }

    public void setCurrent_gestore(Gestore current_gestore) {
        this.current_gestore = current_gestore;
    }

    public String getCurrent_username() {
        return current_username;
    }

    public void setCurrent_username(String current_username) {
        this.current_username = current_username;
    }
    
    
    public String logout(){
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) ctx.getExternalContext().getRequest();
        try{
            request.getSession().invalidate();
            request.logout();
        }catch(ServletException e){
        
            return "";
        }
        
        return "/index?faces-redirect=true";
    }
    
    private Principal getUserPrincipal(){
            
        return FacesContext.getCurrentInstance()
                .getExternalContext().getUserPrincipal();
    }

    
}
