/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import entities.Prenotazione;
import entities.Account;
import entities.Utente;
import exception.PrenotazioneNonRiuscitaException;
import java.io.File;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import org.primefaces.context.RequestContext;
import session.facade.InvitoFacadeLocal;
import session.facade.PrenotazioneFacadeLocal;
import session.stateless.GestorePrenotazioneLocal;
import util.Event;
import util.Scheduler;

/**
 *
 * @author antonio
 */
public class PrenotazioneController implements Serializable {
    
    @EJB
    private GestorePrenotazioneLocal gestorePrenotazione;
    @EJB
    private InvitoFacadeLocal invitoFacade;
    @EJB
    private PrenotazioneFacadeLocal prenotazioneFacade;
    
    private static final String IMAGE_CAMPI ="/home/antonio/webapp/images/campi";
    private String data;
    private String message;
    private Date ora_inizio;
    private String search_bar;
    private List<Account> invitati;
    private List<Account> matched_amici;
    private List<Account> amici;
    private List<Account> non_disponibili;
    private Scheduler scheduler;
    private List<Entry<Date,Event>> entries;
    private List<String> image_link = new ArrayList<>();
    private Campo current_campo;
    private HtmlPanelGroup panel;
    private final SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    
    
    /**
     * Creates a new instance of PrenotazioneController
     */
    public PrenotazioneController() {
    }
    
    
    @PostConstruct
    public void init(){
        try {
            FacesContext ctx = FacesContext.getCurrentInstance();
            UtenteSession session = ctx.getApplication().evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
            current_campo = session.getCurrent_campo();
            amici = session.getAmici();
            invitati = new ArrayList<>();
            matched_amici = new ArrayList<>();
            non_disponibili = new ArrayList<>();
            data = df.format(new Date());
            createScheduler();
            ottieniImmaginiCampo();
            
        } catch (ParseException ex) {
            Logger.getLogger(PrenotazioneController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
    
    public PrenotazioneFacadeLocal getPrenotazioneFacade() {
        return prenotazioneFacade;
    }

    public void setPrenotazioneFacade(PrenotazioneFacadeLocal prenotazioneFacade) {
        this.prenotazioneFacade = prenotazioneFacade;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Date getOra_inizio() {
        return ora_inizio;
    }

    public void setOra_inizio(Date ora_inizio) {
        this.ora_inizio = ora_inizio;
    }
    
    public List<Account> getInvitati() {
        return invitati;
    }

    public void setInvitati(List<Account> invitati) {
        this.invitati = invitati;
    }

    public List<Account> getMatched_amici() {
        return matched_amici;
    }

    public void setMatched_amici(List<Account> matched_amici) {
        this.matched_amici = matched_amici;
    }

    public List<String> getImage_link() {
        return image_link;
    }

    public void setImage_link(List<String> image_link) {
        this.image_link = image_link;
    }

    public List<Account> getAmici() {
        return amici;
    }

    public void setAmici(List<Account> amici) {
        this.amici = amici;
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }

    public Scheduler getScheduler() {
        return scheduler;
    }

    public void setScheduler(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    public List<Entry<Date, Event>> getEntries() {
        return entries;
    }

    public void setEntries(List<Entry<Date, Event>> entries) {
        this.entries = entries;
    }

     public HtmlPanelGroup getPanel() {
        return panel;
    }

    public void setPanel(HtmlPanelGroup panel) {
        this.panel = panel;
    }

    public String getSearch_bar() {
        return search_bar;
    }

    public void setSearch_bar(String search_bar) {
        this.search_bar = search_bar;
    }

    public List<Account> getNon_disponibili() {
        return non_disponibili;
    }

    public void setNon_disponibili(List<Account> non_disponibili) {
        this.non_disponibili = non_disponibili;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public void searchAmici(AjaxBehaviorEvent event){
    
        matched_amici.clear();
        for ( Account acc : amici ){
            Utente u = acc.getUtente();
            if( u.getNome().matches("(?i).*"+search_bar+".*") || u.getCognome().matches("(?i).*"+search_bar+".*") )  
                matched_amici.add(acc);
        }
    }
    
     public void invita(AjaxBehaviorEvent e, Account invitato ) throws ParseException{

       boolean disponibile = invitoFacade.verificaDisponibilita(invitato,df.parse(data), ora_inizio);
       if ( disponibile ){
           invitati.add(invitato);
       }else{
           
           FacesMessage message =new FacesMessage (invitato.getUtente().getNome()+" "
                   +invitato.getUtente().getCognome()+" non disponibile");
           RequestContext.getCurrentInstance().showMessageInDialog(message);    
       }    

     }
     
     public void prenota(AjaxBehaviorEvent e) throws ParseException{
         
         FacesContext ctx = FacesContext.getCurrentInstance();
         UtenteSession session = ctx.getApplication()
                 .evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
         Prenotazione p = new Prenotazione();
         p.setCampo(current_campo);
         p.setData_prenotazione(df.parse(data));
         p.setOrario_inizio(ora_inizio);
         p.setPrenotante(session.getCurrent_account());
         System.out.println("current_campo = " + current_campo.getId());
         System.out.println("data = " + data);
         System.out.println("ora_inizio = " + ora_inizio);
         
        try {
            gestorePrenotazione.prenota(p, invitati, session.getCurrent_account());
            RequestContext.getCurrentInstance().execute("prenotazione_riuscita_dialog.show();");
        } catch (PrenotazioneNonRiuscitaException ex) {
            
            message=  ex.getMessage();
            non_disponibili = ex.getNon_disponibili();
            RequestContext.getCurrentInstance().execute("prenotazione_exception.show();");
        }
         
     }
     
     public void aggiornaListaPrenotazioni(AjaxBehaviorEvent e) throws ParseException{
     
         createScheduler();
         System.out.println("e = " + data.toString());
     }
     
     public void setDataPrenotazione(AjaxBehaviorEvent e,Date data){
         System.out.println("data clicckata = " + data);
         ora_inizio = data;
         RequestContext.getCurrentInstance().execute("inviti_dialog.show();");
     }
     
     public void sostituisci(AjaxBehaviorEvent e){
         
         invitati.removeAll(non_disponibili);
         
     }
     
     public String abbandona(){
     
         return "index";
     }
     /*Metodi di utilità*/
     private void createScheduler() throws ParseException{
        
        Date date = df.parse(data);
        scheduler = new Scheduler(date, current_campo.getOrario_apertura() , current_campo.getOrario_chiusura());
        scheduler.createMap();
        List<Prenotazione> prenotazioni = prenotazioneFacade.getPrenotazioniPerCampo(current_campo, date);
        scheduler.setEvents(wrapPrenotazioni(prenotazioni));
        entries = new ArrayList<>(scheduler.getEvent_model().entrySet());
    }
    
    private List<Event> wrapPrenotazioni(List<Prenotazione> prenotazioni) {
        
        List<Event> eventi = new ArrayList<>();
        Calendar setter = Calendar.getInstance();
        Calendar getter  = Calendar.getInstance();
        for ( Prenotazione p : prenotazioni ){
            System.out.println("p = " + p.getData_prenotazione().toString());
            System.out.println("p = " + p.getOrario_inizio().toString());
            setter.setTime(p.getData_prenotazione());
            getter.setTime(p.getOrario_inizio());
            setter.set(Calendar.HOUR_OF_DAY, getter.get(Calendar.HOUR_OF_DAY));
            System.out.println("setter = " + setter.getTime());
            eventi.add(new Event(setter.getTime(), p));
        }

        return eventi;
    }
    
    private void ottieniImmaginiCampo(){
    
        File folder = new File(IMAGE_CAMPI+"/"+current_campo.getId());
        if (folder.exists()){
            if (folder.isDirectory()){
                image_link.addAll(Arrays.asList(folder.list()));
            }
        }  
    }
    
}
