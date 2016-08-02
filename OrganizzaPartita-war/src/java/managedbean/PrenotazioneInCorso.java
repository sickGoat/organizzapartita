/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import session.facade.PrenotazioneFacadeLocal;
import entities.Account;
import entities.Invito;
import entities.Prenotazione;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import session.facade.InvitoFacadeLocal;
/**
 *
 * @author antonio
 */
public class PrenotazioneInCorso {
    @EJB
    private InvitoFacadeLocal invitoFacade;
    
    @EJB
    private PrenotazioneFacadeLocal prenotazioneFacade;
    
    private Prenotazione prenotazione;
    private Prenotazione partita_confermata;
    private Account current_account;
    private List<Invito> inviti = new ArrayList<>();
    private Date data;
    private String message;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    /**
     * Creates a new instance of PrenotazioneInCorso
     */
    public PrenotazioneInCorso() {
           
    }
    
    @PostConstruct
    public void init(){
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        UtenteSession bean = ctx.getApplication()
                .evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
        data = new Date();
        current_account = bean.getCurrent_account();
        prenotazione = prenotazioneFacade.getPrenotazionePerAccountData(current_account, new Date());
        if ( prenotazione != null ){
            inviti = prenotazione.getInviti();
            if (prenotazione.getStato() == Prenotazione.StatoPrenotazione.CONFERMATA)
                partita_confermata = prenotazione;
        }
        else{
            Invito invito = invitoFacade.invitoPrenotazioneConfermata(current_account, data);
            if (invito != null )
                partita_confermata = invito.getPrenotazione();
        }
        
        setMessage();
        
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        
        this.message = message;
    }
    
    public void setMessage(){
        
        message = prenotazione == null ? "Nessuna prenotazione per questa data" :prenotazione.getStato() == Prenotazione.StatoPrenotazione.CONFERMATA ? "Prenotazione confermata":"";
        
    }
    
    public Account getCurrent_account() {
        return current_account;
    }

    public void setCurrent_account(Account current_account) {
        this.current_account = current_account;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public List<Invito> getInviti() {
        return inviti;
    }

    public void setInviti(List<Invito> inviti) {
        this.inviti = inviti;
    }

    public Prenotazione getPartita_confermata() {
        return partita_confermata;
    }

    public void setPartita_confermata(Prenotazione partita_confermata) {
        this.partita_confermata = partita_confermata;
    }
    
    public void changeData() throws ParseException{
        
        System.out.println("data = " + data);
        prenotazione = prenotazioneFacade.getPrenotazionePerAccountData(current_account, data);
        inviti = prenotazione == null ? null : prenotazione.getInviti();
        resetPartitaConfermata();
        setMessage();
    }
    
    private void resetPartitaConfermata(){
        partita_confermata = null;
        if (prenotazione != null ){
        if ( prenotazione.getStato().equals(Prenotazione.StatoPrenotazione.CONFERMATA))
                partita_confermata = prenotazione;
        }
        else{
                Invito invito = invitoFacade.invitoPrenotazioneConfermata(current_account, data);
                if (invito != null )
                    partita_confermata = invito.getPrenotazione();
                System.out.println("nessuna partita = ");
        }
    
    }
}
