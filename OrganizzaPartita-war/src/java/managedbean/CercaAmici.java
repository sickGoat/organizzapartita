/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import java.util.List;
import entities.Account;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import session.facade.AccountFacadeLocal;

/**
 *
 * @author antonio
 */
public class CercaAmici implements Serializable {
    
    @EJB
    private AccountFacadeLocal accountFacade;
    private List<Account> potresti_conoscere = new ArrayList<>();
    private String search;
    private List<Account> matched_persone = new ArrayList<>();
    private Account acc;
    /**
     * Creates a new instance of CercaAmici
     */
    public CercaAmici() {
    }
    
    @PostConstruct
    public void init(){
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        UtenteSession session = ctx.getApplication().evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
        acc = session.getCurrent_account();
        potresti_conoscere = accountFacade.getPersonePotrestiConoscere(acc);
        
    }
    
    public List<Account> getPotresti_conoscere() {
        return potresti_conoscere;
    }

    public void setPotresti_conoscere(List<Account> potresti_conoscere) {
        this.potresti_conoscere = potresti_conoscere;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public Account getAcc() {
        return acc;
    }

    public void setAcc(Account acc) {
        this.acc = acc;
    }
    
    public List<Account> getMatched_persone() {
        return matched_persone;
    }

    public void setMatched_persone(List<Account> matched_persone) {
        this.matched_persone = matched_persone;
    }
    
    public void cercaUtente(AjaxBehaviorEvent e){
    
        if ( !matched_persone.isEmpty() )
                matched_persone.clear();
        
        if ( !search.isEmpty() )
            matched_persone = accountFacade.getUtentiByPattern(search,acc);
            
    }
    
}   
