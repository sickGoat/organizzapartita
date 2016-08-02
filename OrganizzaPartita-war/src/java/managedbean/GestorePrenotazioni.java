/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Prenotazione;
import exception.CampoNonTrovatoException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import session.facade.CampoFacadeLocal;
import session.facade.GestoreFacadeLocal;
import session.facade.PrenotazioneFacadeLocal;
import session.stateless.GestorePrenotazioneLocal;

/**
 *
 * @author antonio
 */

public class GestorePrenotazioni implements Serializable {
    @EJB
    private GestorePrenotazioneLocal gestorePrenotazione;
    @EJB
    private PrenotazioneFacadeLocal prenotazioneFacade;
    @EJB
    private CampoFacadeLocal campoFacade;
    @EJB
    private GestoreFacadeLocal gestoreFacade;
    
    private long current_campo;
    private List<Prenotazione> prenotazioni = new ArrayList<Prenotazione>();
    private Prenotazione selected_prenotazione;
    private Date date;
    private String message;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    
    /**
     * Creates a new instance of GestorePrenotazioni
     */
    public GestorePrenotazioni() {
        
    }
    
    @PostConstruct
    public void init(){
        
        String username = FacesContext.getCurrentInstance().getExternalContext()
                .getUserPrincipal().getName();
        date = new Date();
        current_campo = getRequestMap().get("id_campo") != null ? Long.valueOf(getRequestMap().get("id_campo")): 00;
        
        if ( current_campo != 00  ){
            if(campoFacade.isGestore(username, current_campo)){
                try{
                    prenotazioni = prenotazioneFacade.getPrenotazioniPerCampo(current_campo, new Date());
                }catch(CampoNonTrovatoException ex){
                    message = "Campo non trovato";
                }
            }
            else{
                    message = "Non puoi visualizzare questo campo";
                }          
        }else{
               
            message = "Nessun campo trovato";
        }
        
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    public long getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(long current_campo) {
        this.current_campo = current_campo;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PrenotazioneFacadeLocal getPrenotazioneFacade() {
        return prenotazioneFacade;
    }

    public void setPrenotazioneFacade(PrenotazioneFacadeLocal prenotazioneFacade) {
        this.prenotazioneFacade = prenotazioneFacade;
    }

    public Prenotazione getSelected_prenotazione() {
        return selected_prenotazione;
    }

    public void setSelected_prenotazione(Prenotazione selected_prenotazione) {
        this.selected_prenotazione = selected_prenotazione;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    
    
    private Map<String,String> getRequestMap(){

        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
    
    public void selectPrenotazione(AjaxBehaviorEvent e , Prenotazione p){
        selected_prenotazione = p;
    }
    
    public void deletePrenotazione(AjaxBehaviorEvent e){
    
        gestorePrenotazione.eliminaPrenotazione(selected_prenotazione,message);
        prenotazioni.remove(selected_prenotazione);
    }
    
    public void updatePrenotazioni(AjaxBehaviorEvent e,Date data) throws CampoNonTrovatoException{
    
        if(!prenotazioni.isEmpty())
            prenotazioni.clear();

        prenotazioni = prenotazioneFacade.getPrenotazioniPerCampo(current_campo, data);
   
    }
}
