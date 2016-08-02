/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Account;
import entities.Amicizia;
import entities.Campo;
import entities.Invito;
import entities.Updates;
import entities.Utente;
import exception.AccountNonValidoExcpetion;
import exception.InvitoGiaModificatoException;
import java.io.File;
import java.io.Serializable;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import session.facade.AccountFacadeLocal;
import session.facade.AmiciziaFacadeLocal;
import session.facade.UpdatesFacadeLocal;
import session.facade.UtenteFacadeLocal;
import session.stateless.GestorePrenotazioneLocal;
import session.stateless.GestoreUtentiLocal;

/**
 *
 * @author antonio
 */
public class UtenteSession implements Serializable{
    @EJB
    private AmiciziaFacadeLocal amiciziaFacade;
    @EJB
    private GestorePrenotazioneLocal gestorePrenotazione;
    @EJB
    private UpdatesFacadeLocal updatesFacade;
    @EJB
    private UtenteFacadeLocal utenteFacade;
    @EJB
    private AccountFacadeLocal accountFacade;
    @EJB
    private GestoreUtentiLocal gestoreUtenti;
    
    private String current_username;
    private Account current_account;
    private Utente current_utente;
    private List<Amicizia> richieste_amicizia = new ArrayList<>();
    private List<Account> amici = new ArrayList<>();
    private List<Updates> aggiornamenti;
    private int aggiornamenti_non_letti;
    private Campo current_campo;
    
    /**
     * Creates a new instance of UtenteSession
     */
    public UtenteSession() {
   }
        
    @PostConstruct
    public void init(){
        System.out.println("init utentesession ");
        Principal user = getUserPrincipal();
        current_username = user.getName();
        current_account = accountFacade.getAccount(current_username);
        current_utente = current_account.getUtente();
        if ( amici.isEmpty() )
            amici = accountFacade.getAmici(current_account);
        
        richieste_amicizia = amiciziaFacade.getRichiesteAmicizia(current_account);
        aggiornamenti =  updatesFacade.getNewUpdates(current_account);
        if( aggiornamenti.isEmpty() )
            aggiornamenti = updatesFacade.getOldUpdates(current_account, 0);
        setNewAggiornamentiCount();
    }
    
    public Account getCurrent_account() {
        return current_account;
    }

    public void setCurrent_account(Account current_account) {
        this.current_account = current_account;
    }
    
    public List<Account> getAmici() {
        
        return amici;
    }

    public void setAmici(List<Account> amici) {
        this.amici = amici;
    }

    public Utente getCurrent_utente() {
        return current_utente;
    }

    public void setCurrent_utente(Utente curren_utente) {
        this.current_utente = curren_utente;
    }

    public String getCurrent_username() {
        return current_username;
    }

    public void setCurrent_username(String current_username) {
        this.current_username = current_username;
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }

    public List<Updates> getAggiornamenti() {
        return aggiornamenti;
    }

    public void setAggiornamenti(List<Updates> aggiornamenti) {
        this.aggiornamenti = aggiornamenti;
    }
    
    public void updateUtente(){
    
        utenteFacade.edit(current_utente);
    }
    
    public void updateAccount(){
        
        accountFacade.edit(current_account);
    }
    
    public String eliminaAccount(){
            
        gestoreUtenti.eliminaAccount(current_account);
        return logout();
    }

    public List<Amicizia> getRichieste_amicizia() {
        return richieste_amicizia;
    }

    public void setRichieste_amicizia(List<Amicizia> richieste_amicizia) {
        this.richieste_amicizia = richieste_amicizia;
    }

    public int getAggiornamenti_non_letti() {
        return aggiornamenti_non_letti;
    }

    public void setAggiornamenti_non_letti(int aggiornamenti_non_letti) {
        this.aggiornamenti_non_letti = aggiornamenti_non_letti;
    }
    
    public boolean possiedeFoto(){

        File f = new File("/home/antonio/webapp/images/"+current_username);
        if (f.isDirectory())
            return !(f.list().length == 0);
        else
            return false;
    }
    
    
    public void inviaRichiestaAmicizia(AjaxBehaviorEvent e,Account amico) throws AccountNonValidoExcpetion{
         
        System.out.println("amico = " + amico==null);
        if ( amiciziaFacade.getAmicizia(current_account, amico) == null){
                gestoreUtenti.inviaRichiestaAmicizia(current_account, amico);
                RequestContext.getCurrentInstance().execute("showMessage('Richiesta inoltrata correttamente')");
        }
        else{
                RequestContext.getCurrentInstance().execute("showMessage('Richiesta di amicizia gia inoltrata')");
        }
    }
    
    public void rispondiRichiesta(AjaxBehaviorEvent e,Amicizia richiesta,boolean accetta) throws AccountNonValidoExcpetion{
        System.out.println(current_username+" risponede richiesta a  = " + richiesta.getAccount1().getUsername());
        gestoreUtenti.rispondiRischiestaAmicizia(richiesta.getAccount1(),current_account, accetta);
        richieste_amicizia.remove(richiesta);
        if(accetta)
            amici.add(richiesta.getAccount1());
        
    }
    
    public void rispondiInvitoPartita(AjaxBehaviorEvent e,Invito invito,Updates agg,boolean accetta){
        try {
            if(accetta)
                gestorePrenotazione.accettaInvito(invito);
            else
                gestorePrenotazione.rifiutaInvito(invito);
            
            aggiornaUpdates();
        } catch (InvitoGiaModificatoException ex) {
            RequestContext.getCurrentInstance().execute("showMessageInDialog('output_message_header','Hai gia risposto all invito non puoi farlo due volte')");
        }
        
    }
    
    public void refreshAggiornament(AjaxBehaviorEvent e){
        System.out.println(current_username+": aggiorno aggiornamenti....");
        aggiornaUpdates();
    }
    
    private void aggiornaUpdates(){
        
        List<Updates> new_aggiornamenti = updatesFacade.getNewUpdates(current_account);
        if ( !aggiornamenti.containsAll(new_aggiornamenti) ){
            aggiornamenti = new_aggiornamenti;
            setNewAggiornamentiCount();
        }
    }
    
    public void cambiaStatoAggiornamenti(AjaxBehaviorEvent e){
        
        aggiornamenti_non_letti = 0;
        for (Updates up : aggiornamenti ){
            up.setStato(Updates.StatoUpdate.VISUALIZZATO);
            updatesFacade.edit(up);
        }
    }
    
    public void eliminaAmico(AjaxBehaviorEvent e,Account a){
        System.out.println("a  is null = " + a == null);
        amiciziaFacade.eliminaAmico(current_account, a);
        amici.remove(a);
    }
    
    public void refreshRichiesteAmicizia(AjaxBehaviorEvent e){
        
        System.out.println(current_account.getUsername()+" aggiorno amici");
        richieste_amicizia = amiciziaFacade.getRichiesteAmicizia(current_account);
        amici = accountFacade.getAmici(current_account);
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

    //metodi non visibili al client
    private void setNewAggiornamentiCount() {
        
        aggiornamenti_non_letti = 0;
        for ( Updates up : aggiornamenti )
            if ( up.getStato() == Updates.StatoUpdate.NON_VISUALIZZATO )
                aggiornamenti_non_letti++;
    }
    
    
   
}
