/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import exception.AccountNonValidoExcpetion;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
import session.facade.CampoFacade;
import session.facade.CampoFacadeLocal;
import util.AddressResolver;

/**
 *
 * @author antonio
 */
public class AggiungiCampo implements Serializable{
    @EJB
    private CampoFacadeLocal campoFacade;
    
    private String indirizzo;
    private int numero_giocatori;
    private String orario_apertura;
    private String orario_chiusura;
    private double tariffa;
    private Part [] parts;
    private List<String> indirizzi = new ArrayList<>();
    private String lat;
    private String lgt;
    
    
    /**
     * Creates a new instance of AggiungiCampo
     */
    public AggiungiCampo() {
    }

    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public int getNumero_giocatori() {
        return numero_giocatori;
    }

    public void setNumero_giocatori(int numero_giocatori) {
        this.numero_giocatori = numero_giocatori;
    }

    public String getOrario_apertura() {
        return orario_apertura;
    }

    public void setOrario_apertura(String orario_apertura) {
        this.orario_apertura = orario_apertura;
    }

    public String getOrario_chiusura() {
        return orario_chiusura;
    }

    public void setOrario_chiusura(String orario_chiusura) {
        this.orario_chiusura = orario_chiusura;
    }

    public double getTariffa() {
        return tariffa;
    }

    public void setTariffa(double tariffa) {
        this.tariffa = tariffa;
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

    public List<String> getIndirizzi() {
        return indirizzi;
    }

    public void setIndirizzi(List<String> indirizzi) {
        this.indirizzi = indirizzi;
    }
    
    public void onIndirizziChange(AjaxBehaviorEvent e){
        try {
            
            indirizzi.clear();
            AddressResolver resolver = new AddressResolver(indirizzo);
            resolver.resolve();
            indirizzi = resolver.getFormattedAddresses();
            System.out.println("indirizzi = " + indirizzi.size());
        } catch (Exception ex) {
            Logger.getLogger(AggiungiCampo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void selectIndirizzo(AjaxBehaviorEvent e, String selezionato) throws Exception{
    
        indirizzo = selezionato;
        indirizzi.clear();
        AddressResolver resolver = new AddressResolver(indirizzo);
        resolver.resolve();
        float [] coordinate = resolver.getCoordinate(indirizzo);
        lat = String.valueOf(coordinate[0]);
        lgt = String.valueOf(coordinate[1]);
        
        System.out.println("selezionato = " + selezionato);
    
    }
    
    public String aggiungiCampo() throws ParseException, IOException{
        
        String username = FacesContext.getCurrentInstance().getExternalContext()
                .getUserPrincipal().getName();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        Campo campo = new Campo();
        campo.setIndirizzo(indirizzo);
        campo.setLatitudine(lat);
        campo.setLongitudine(lgt);
        campo.setOrario_apertura(df.parse(orario_apertura));
        campo.setOrario_chiusura(df.parse(orario_chiusura));
        campo.setNumero_giocatori(numero_giocatori);
        campo.setPrezzo_per_ora(tariffa);
        try{
            
            long id_campo = campoFacade.aggiungiCampo(username,campo);
            FacesContext ctx = FacesContext.getCurrentInstance();
            uploadFIle bean = ctx.getApplication().evaluateExpressionGet(ctx, "#{uploadFIle}",uploadFIle.class);
            bean.uploadAllFiles(Long.toString(id_campo));
            
        }catch(AccountNonValidoExcpetion e){
            
        }
        return "index";
    }
    
    
}
