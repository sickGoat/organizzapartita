/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import org.primefaces.event.map.OverlaySelectEvent;
import org.primefaces.model.map.DefaultMapModel;
import org.primefaces.model.map.LatLng;
import org.primefaces.model.map.MapModel;
import org.primefaces.model.map.Marker;

/**
 *
 * @author antonio
 */
public class MapView implements Serializable{
    
    private static final String PATH_LINK = "/home/antonio/webapp/images/campi";
    private MapModel advancedModel;
    private String lat = "36.890257";
    private String lgt = "30.707417";
    private List<Campo> campi;
    private Marker marker;
    private Campo current_campo;
    
    public MapView(){}
    
    public MapView(List<Campo> campi){
        this.campi = campi;
        
    }
    
    @PostConstruct
    public void init() {
        System.out.println("init mapview");
        advancedModel = new DefaultMapModel();
        advancedModel.addOverlay(new Marker(new LatLng(Float.parseFloat(lat), Float.parseFloat(lgt)),"Tu sei qui",
                "","http://maps.google.com/mapfiles/ms/micons/blue-dot.png"));
        if (campi != null){   
         int index = 0;
        for ( Campo c : campi ){
            System.out.println("c = " + c.getId());
            Marker m = new Marker(new LatLng(Float.parseFloat(c.getLatitudine()),Float.parseFloat(c.getLongitudine())),""+index, c);
            advancedModel.addOverlay(m);
            index++;
        }   
          
     } //Icons and Data
        
    }
  
    public MapModel getAdvancedModel() {
        return advancedModel;
    }
      
    public void onMarkerSelect(OverlaySelectEvent event) {
        marker = (Marker) event.getOverlay();
        changeCurrentCampo(marker.getTitle());
        
    }
    
    private void changeCurrentCampo(String index){
        
        current_campo = campi.get(Integer.parseInt(index));
        
    }
    
    public Marker getMarker() {
        return marker;
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

    public List<Campo> getCampi() {
        return campi;
    }

    public void setCampi(List<Campo> campi) {
        this.campi = campi;
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }
    
    public String viewDettagliCampo(){
        
        FacesContext ctx = FacesContext.getCurrentInstance();
        UtenteSession session = ctx.getApplication().evaluateExpressionGet(ctx, "#{utenteSession}", UtenteSession.class);
        session.setCurrent_campo(current_campo);
        
        return "prenotazione";
    }
    
}
