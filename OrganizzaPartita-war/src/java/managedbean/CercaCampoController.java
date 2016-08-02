/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import java.io.IOException;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPathExpressionException;
import org.primefaces.model.map.Marker;
import org.xml.sax.SAXException;
import session.facade.CampoFacadeLocal;
import util.AddressResolver;

/**
 *
 * @author antonio
 */
public class CercaCampoController implements Serializable{
    
    public static final int RANGE_RICERCA = 10000;
    
    @EJB
    private CampoFacadeLocal campoFacade;
    private String indirizzo = "";
    private List<Campo> campi = new ArrayList<>();
    private List<String> indirizzi = new ArrayList<>();
    private Marker current_marker;
    private Campo current_campo;
    private int numero_giocatori;
    private int tariffa_oraria;
    private String lat ="41.381542" ;
    private String lgt = "2.122893";
    
    /**"
     * Creates a new instance of CercaCampoController
     */
    public CercaCampoController() {
        
       
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }
    
    
    public CampoFacadeLocal getCampoFacade() {
        return campoFacade;
    }

    public void setCampoFacade(CampoFacadeLocal campoFacade) {
        this.campoFacade = campoFacade;
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public List<Campo> getCampi() {
        return campi;
    }

    public void setCampi(List<Campo> campi) {
        this.campi = campi;
    }

    public List<String> getIndirizzi() {
        return indirizzi;
    }

    public void setIndirizzi(List<String> indirizzi) {
        this.indirizzi = indirizzi;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLgt() {
        return lgt;
    }

    public void setLgt(String lgt) {
        this.lgt = lgt;
    }

    public Marker getCurrent_marker() {
        return current_marker;
    }

    public void setCurrent_marker(Marker current_marker) {
        this.current_marker = current_marker;
    }

    public int getNumero_giocatori() {
        return numero_giocatori;
    }

    public void setNumero_giocatori(int numero_giocatori) {
        this.numero_giocatori = numero_giocatori;
    }

    public int getTariffa_oraria() {
        return tariffa_oraria;
    }

    public void setTariffa_oraria(int tariffa_oraria) {
        this.tariffa_oraria = tariffa_oraria;
    }
    
    public void cercaIndirizzi(AjaxBehaviorEvent e) throws MalformedURLException, IOException, UnsupportedEncodingException, ParserConfigurationException, SAXException, XPathExpressionException{
        
        AddressResolver resolver = new AddressResolver(indirizzo);
        resolver.resolve();
        
        indirizzi = resolver.getFormattedAddresses();
       
    }
    
    public void updateModel(AjaxBehaviorEvent e) throws Exception{
       
        System.out.println("lat = " + lat);
        System.out.println("lgt = " + lgt);
        System.out.println("Cerca campi vicini");
        campi = campoFacade.findCampiInRange(Float.parseFloat(lat),Float.parseFloat(lgt), RANGE_RICERCA);
        System.out.println("campi.isEmpty() = " + campi.isEmpty());
        inizializzaMappa(campi);
    }
    
  
    public void updateCenter(AjaxBehaviorEvent e) throws Exception {
        
        System.out.println("updateCenter " + new Date().toString());
        System.out.println("lat = " + lat);
        System.out.println("lgt = " + lgt);
        FacesContext ctx = FacesContext.getCurrentInstance();
        MapView bean = ctx.getApplication().evaluateExpressionGet(ctx, "#{mapView}", MapView.class);
        AddressResolver resolver = new AddressResolver(indirizzo);
        resolver.resolve();
        float [] coordinate = resolver.getCoordinate(indirizzo);
        lat = String.valueOf(coordinate[0]);
        lgt = String.valueOf(coordinate[1]);
        bean.setLat(lat);
        bean.setLgt(lgt);
        bean.init();
        System.out.println("lat = " + lat);
        System.out.println("lgt = " + lgt);
       
    }
    
    public void filtra(AjaxBehaviorEvent e){
        
        List<Campo> campi_filtrati = new ArrayList<>();
        if ( tariffa_oraria == 0 && numero_giocatori == 0 )
            inizializzaMappa(campi);
        else
            if( tariffa_oraria != 0 && numero_giocatori == 0 ){
                campi_filtrati = filtraPerTariffa();
                inizializzaMappa(campi_filtrati);
            }
        else
            if( tariffa_oraria == 0 && numero_giocatori != 0 ){
            
                campi_filtrati = filtraPerNumeroGiocatori();
                inizializzaMappa(campi_filtrati);
            }
       else{
                campi_filtrati = filtraPerNumTariffa();
                inizializzaMappa(campi_filtrati);
            }
       
    }
    
    
    
    /*Metodo per l'autocomplete dell'inputtext*/
    public void completeIndirizzo(AjaxBehaviorEvent e) {
        try {
            indirizzi.clear();
            AddressResolver resolver = new AddressResolver(indirizzo);
            resolver.resolve();
            indirizzi = resolver.getFormattedAddresses();
            
            
        } catch (Exception ex) {
            FacesMessage message = new FacesMessage(FacesMessage.FACES_MESSAGES,"errore");
            org.primefaces.context.RequestContext.getCurrentInstance().showMessageInDialog(message);
        } 
        
     }
    
    public void selezionaCampo(AjaxBehaviorEvent e,String indirizzo) throws Exception{
        
        this.indirizzo = indirizzo;
        AddressResolver resolver = new AddressResolver(this.indirizzo);
        resolver.resolve();
        float [] coordinate = resolver.getCoordinate(this.indirizzo);
        lat = String.valueOf(coordinate[0]);
        lgt = String.valueOf(coordinate[1]);
        indirizzi.clear();
        
    }
     
     private void inizializzaMappa(List<Campo> nuovi_campi){
     
        FacesContext context = FacesContext.getCurrentInstance();
        MapView bean = context.getApplication().evaluateExpressionGet(context, "#{mapView}", MapView.class);
        bean.setCampi(nuovi_campi);
        bean.setLat(lat);
        bean.setLgt(lgt);
        bean.init();
         
     }

    private List<Campo> filtraPerNumeroGiocatori() {
        
        List<Campo> campi_filtrati = new ArrayList<>();
        for ( Campo c : campi )
            if ( c.getNumero_giocatori() == numero_giocatori )
                campi_filtrati.add(c);
        
        return campi_filtrati;
    }

    private List<Campo> filtraPerTariffa() {
        
        List<Campo> campi_filtrati = new ArrayList<>();
        for ( Campo c : campi )
            if ( c.getPrezzo_per_ora() <= tariffa_oraria )
                campi_filtrati.add(c);
        
        return campi_filtrati;
    }

    private List<Campo> filtraPerNumTariffa() {
        
        List<Campo> campi_filtrati = new ArrayList<>();
        for ( Campo c : campi )
            if ( c.getNumero_giocatori() == numero_giocatori && c.getPrezzo_per_ora() <= tariffa_oraria )
                campi_filtrati.add(c);
        
        return campi_filtrati;
    }
    
}
