/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.stateless;

import entities.Account;
import entities.Invito;
import entities.Prenotazione;
import entities.Updates;
import exception.InvitoGiaModificatoException;
import messages.ExceptionMessage;
import exception.PrenotazioneNonRiuscitaException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.ejb.ApplicationException;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Stateless;
import javax.ejb.Timeout;
import javax.ejb.Timer;
import javax.ejb.TimerService;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import messages.UpdateMessage;
import session.facade.AccountFacadeLocal;
import session.facade.CampoFacadeLocal;
import session.facade.InvitoFacadeLocal;
import session.facade.PrenotazioneFacadeLocal;
import session.facade.UpdatesFacadeLocal;

/**
 *
 * @author antonio
 */
@Stateless
@ApplicationException(rollback = true)
@Local(GestorePrenotazioneLocal.class)
public class GestorePrenotazione implements GestorePrenotazioneLocal {
    
    //tempo mantenimento prenotazione
    private final long TIMEOUT = 1000*60*10;
    
    @EJB
    private UpdatesFacadeLocal updatesFacade;
    @EJB
    private InvitoFacadeLocal invitoFacade;
    
    @EJB
    private CampoFacadeLocal campoFacade;
    @EJB
    private PrenotazioneFacadeLocal prenotazioneFacade;
    @EJB
    private AccountFacadeLocal accountFacade;
    @Resource
    TimerService timer;
    


    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void prenota(Prenotazione p, List<Account> invitati, Account prenotante) throws PrenotazioneNonRiuscitaException{
        
        if ( prenotazioneFacade.raggiuntoLimite(prenotante, p.getData_prenotazione()))
                throw new exception.PrenotazioneNonRiuscitaException(ExceptionMessage.RAGGIUNTO_LIMITE_PRENOTAZIONE);
        
        if(!prenotazioneFacade.verificaDisponibilita(p.getCampo(), p.getData_prenotazione(),
                p.getOrario_inizio())){
               throw new exception.PrenotazioneNonRiuscitaException(ExceptionMessage.CAMPO_OCCUPATO);
        }
        
        List<Invito> inviti = new ArrayList<>();
        List<Account> non_disponibili = new ArrayList<>();
        int count = 1;
        for ( Account invitato : invitati ){
            System.out.println("invitato = " + invitato.getUsername());
            if ( !invitoFacade.verificaDisponibilita(invitato, p)){   
                non_disponibili.add(invitato);
                continue;
            }
            if ( count < p.getCampo().getNumero_giocatori() ){
                Invito invito = new Invito(invitato, p, Invito.StatoInvito.IN_ATTESA);
                inviti.add(invito);
                count++;
            }else{
                Invito invito = new Invito(invitato, p, Invito.StatoInvito.IN_CODA);
                inviti.add(invito);
            }
        }
        System.out.println("inviti = " + inviti.size());
        System.out.println("p = " + p.getCampo().getNumero_giocatori());
        if( inviti.size() >= p.getCampo().getNumero_giocatori() -1){
            p.setInviti(inviti);
            System.out.println("Creo prenotazione");
            prenotazioneFacade.create(p);
            timer.createTimer(TIMEOUT, p);
            inviaRichiestePartita(prenotante,inviti);
        }else{
            System.out.println("gestorePrenotazione 99 = ");
            throw new PrenotazioneNonRiuscitaException(ExceptionMessage.INVITATI_NON_DISPONIBILI,non_disponibili);
    
        }
    }
    

    @Override
    public void rifiutaInvito(Invito invito)  throws InvitoGiaModificatoException {
        
        if (invito.getStato().equals(Invito.StatoInvito.CONFERMATO) || invito.getStato().equals(Invito.StatoInvito.RIFIUTATO))
            throw new InvitoGiaModificatoException();
        Account prenotante = invito.getPrenotazione().getPrenotante();
        invito.setStato(Invito.StatoInvito.RIFIUTATO);
        invitoFacade.edit(invito);
        List<Invito> inviti_in_coda = prenotazioneFacade.getInvitiPerStato(invito.getPrenotazione(), Invito.StatoInvito.IN_CODA);
        boolean qualcuno_disponibile = false;
        
        if (inviti_in_coda != null ){
            //prendo il primo invito in coda e lo pongo in_attesa
            for ( Invito inv : inviti_in_coda ){
                 if(invitoFacade.verificaDisponibilita(inv.getInvitato(), invito.getPrenotazione())){
                        inv.setStato(Invito.StatoInvito.IN_ATTESA);
                        invitoFacade.edit(inv);
                        inviaRichiestaPartita(prenotante, inv);
                        qualcuno_disponibile = true;
                        break;
                 }else{
                        invitoFacade.remove(inv);
                 
                 }
            }
        }
        
        if (!qualcuno_disponibile){
            //non ci sono piu inviti in coda quindi elimino la prenotazione
            inviaNotifiche(invito.getPrenotazione().getInviti(), invito.getPrenotazione(), UpdateMessage.PRENOTAZIONE_ELIMINATA);
            prenotazioneFacade.remove(invito.getPrenotazione());
        }
    }

    @Override
    public void accettaInvito(Invito invito)  throws InvitoGiaModificatoException {
        
        if ( invito.getStato() == Invito.StatoInvito.RIFIUTATO || invito.getStato() == Invito.StatoInvito.CONFERMATO )
            throw new InvitoGiaModificatoException();
        invito.setStato(Invito.StatoInvito.CONFERMATO);
        invitoFacade.eliminaInvitiNonConfermati(invito.getInvitato(), invito.getPrenotazione().getData_prenotazione());
        List<Invito> confermati = prenotazioneFacade.getInvitiPerStato(invito.getPrenotazione(),
                Invito.StatoInvito.CONFERMATO);
        
        if ( confermati != null ){
            if ( confermati.size() == invito.getPrenotazione().getCampo().getNumero_giocatori()-2 ){
                
                confermati.add(invito);
                Prenotazione prenotazione = invito.getPrenotazione();
                prenotazione.setStato(Prenotazione.StatoPrenotazione.CONFERMATA);
                prenotazioneFacade.edit(prenotazione);
                invitoFacade.eliminaInvitiInCoda(invito.getPrenotazione());
                inviaNotifiche(confermati, prenotazione, UpdateMessage.PRENOTAZIONE_CONFERMA);
            }
            else{
                inviaSingolaNotifica(invito.getInvitato(),invito.getPrenotazione().getPrenotante(), UpdateMessage.PRENOTAZIONE_ACCETTAZIONE);
            }
        }
        
        invitoFacade.edit(invito);
    }
    
    @Override
    @RolesAllowed(value = {"GESTORE"})
    public void eliminaPrenotazione(Prenotazione p,String message){
        
        prenotazioneFacade.remove(p);
        if ( message == null )
            inviaNotifiche(p.getInviti(), p, UpdateMessage.PRENOTAZIONE_ELIMINATA);
        else
            inviaNotifiche(p.getInviti(), p, UpdateMessage.PRENOTAZIONE_ELIMINATA +" "+message);
    }
    
    @Timeout
    public void verificaPrenotazione(final Timer timer){
        System.out.println("verifico prenotazione");
        Prenotazione p = (Prenotazione) timer.getInfo();
        Prenotazione p_refreshed = prenotazioneFacade.find(p.getId());
        boolean ancoraInAttesa = false;
        boolean ancoraInCoda = false;
        for ( Invito i : p_refreshed.getInviti() ){
          switch (i.getStato()){
                case IN_ATTESA:
                    ancoraInAttesa = true;
                    break;
                case CONFERMATO:
                    break;
                case RIFIUTATO:
                    break;
                case IN_CODA:
                    ancoraInCoda = true;
                default:
                    throw new AssertionError(i.getStato().name());   
            }
        }
        
        if (ancoraInAttesa){
            System.out.println("Cancello prenotazione");
            prenotazioneFacade.remove(p_refreshed);
            inviaNotifiche(p.getInviti(), p, UpdateMessage.PRENOTAZIONE_SCADENZA);
        }
        
        if (ancoraInCoda){
            System.out.println("Cancello inviti in coda");
            invitoFacade.eliminaInvitiInCoda(p_refreshed);
        }
        
    }
    
    /*Metodi di utilità*/
    
    /***
     * 
     * @param mittente
     * @param inviti
     * Metodo che invia le richieste al momento della prenotazione
     */
    private void inviaRichiestePartita(Account mittente,List<Invito> inviti){
        
        List<Updates> aggiornamenti = new ArrayList<>();
        for ( Invito invito : inviti ){
            if (invito.getStato() != Invito.StatoInvito.IN_CODA ){
                Updates up = new Updates();
                up.setRichiesta_partita(invito);
                up.setMessaggio(UpdateMessage.PRENOTAZIONE_RICHIESTA);
                up.setRichiesto(invito.getInvitato());
                up.setMittente(mittente);
                aggiornamenti.add(up);
                invito.setAggiornamento(up);
            }
        }
        System.out.println("Invio notifiche");
        updatesFacade.inviaUpdates(aggiornamenti);
    }
    /***
     * 
     * @param mittente
     * @param invito 
     * Metodo che invia una nuova richiesta al primo invito posto in coda
     */
    private void inviaRichiestaPartita(Account mittente,Invito invito){
        
        Updates up = new Updates();
        up.setRichiesta_partita(invito);
        up.setMessaggio(UpdateMessage.PRENOTAZIONE_RICHIESTA);
        up.setRichiesto(invito.getInvitato());
        up.setMittente(mittente);
        invito.setAggiornamento(up);
        invitoFacade.edit(invito);
        updatesFacade.edit(up);
    }
    /***
     * 
     * @param inviti
     * @param p
     * @param msg 
     * Metodo per notificare a tutti gli invitati la conferma della
     * prenotazione o la eliminazione, incluso il prenotante
     */
    private void inviaNotifiche(List<Invito> inviti,Prenotazione p,String msg){
        
        List<Updates> aggiornamenti = new ArrayList<>();
        String message = infoPrenotazione(p)+" "+msg;
        for ( Invito invito : inviti ){
            if (invito.getStato() != Invito.StatoInvito.IN_CODA ){
                Updates up = new Updates();
                up.setMessaggio(message);
                up.setRichiesto(invito.getInvitato());
                aggiornamenti.add(up);
            }
        }
        Updates agg_prenotante = new Updates();
        agg_prenotante.setRichiesto(p.getPrenotante());
        agg_prenotante.setMessaggio(message);
        aggiornamenti.add(agg_prenotante);
        updatesFacade.inviaUpdates(aggiornamenti);
    }
    
    /***
     * 
     * @param from
     * @param to
     * @param message 
     * Metodo che invia al prenotante una notifica riguardante
     * il rifiuto di un invito
     */
    private void inviaSingolaNotifica(Account from,Account to, String message) {
        
        Updates up = new Updates();
        up.setMittente(from);
        up.setMessaggio(message);
        up.setRichiesto(to);
        updatesFacade.create(up);
        
    }
    
    private String infoPrenotazione(Prenotazione p){
        Calendar c = Calendar.getInstance();
        c.setTime(p.getData_prenotazione());
        int data = c.get(Calendar.DATE);
        int mese = c.get(Calendar.MONTH);
        c.setTime(p.getOrario_inizio());
        int ora = c.get(Calendar.HOUR_OF_DAY);
        return "La prenotazione del: "+data +"-"+mese+" alle "+ora+":00";
    }
}
