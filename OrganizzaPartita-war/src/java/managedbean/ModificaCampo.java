/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import session.facade.CampoFacadeLocal;

/**
 *
 * @author antonio
 */
public class ModificaCampo implements Serializable{
    
    @EJB
    private CampoFacadeLocal campoFacade;
    
    private static final String IMAGES_PATH = "/home/antonio/webapp/images/campi";
    private Campo current_campo;
    private long id_campo;
    private String orario_apertura;
    private String orario_chiusura;
    private List<String> images_link = new ArrayList<String>();
    private SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
    
    /**
     * Creates a new instance of ModificaCampo
     */
    public ModificaCampo() {
    }
    
    @PostConstruct
    public void init(){
        
        String username = FacesContext.getCurrentInstance()
                    .getExternalContext().getUserPrincipal().getName();
        System.out.println("username = " + username);
        id_campo = getRequestMap().get("id_campo") != null ? Long.valueOf(getRequestMap().get("id_campo")):00;
        System.out.println("id_campo = " + id_campo);
        if ( id_campo != 00  ){
            if( campoFacade.isGestore(username, id_campo) ){
                    current_campo = campoFacade.find(id_campo);
                    images_link = ottieniLinks(id_campo);
                    orario_apertura = df.format(current_campo.getOrario_apertura());
                    orario_chiusura = df.format(current_campo.getOrario_chiusura());
            }else
                ;//reindirizza e mostra messagio
       }else{
           ; //reindirizza e motra messaggio
        }
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }

    public long getId_campo() {
        return id_campo;
    }

    public void setId_campo(long id_campo) {
        this.id_campo = id_campo;
    }

    public List<String> getImages_link() {
        return images_link;
    }

    public void setImages_link(List<String> images_link) {
        this.images_link = images_link;
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
    
    
    public void eliminaImmagine(AjaxBehaviorEvent e,String filename) throws IOException{
        
        System.out.println("filename = " + filename);
        File f = new File(IMAGES_PATH+"/"+filename);
        if ( f.exists() ){
                f.delete();
                images_link.remove(filename);
        }
    }
    
    public String salvaModifiche(){
        try {
            current_campo.setOrario_apertura(df.parse(orario_apertura));
            current_campo.setOrario_chiusura(df.parse(orario_chiusura));
            campoFacade.edit(current_campo);
            FacesContext ctx = FacesContext.getCurrentInstance();
            uploadFIle bean = ctx.getApplication().evaluateExpressionGet(ctx, "#{uploadFIle}",uploadFIle.class);
            bean.uploadAllFiles(Long.toString(id_campo));
        } catch (Exception ex) {
            return "errore";
        }
            return "index";
    
    }
    
    private static Map<String,String> getRequestMap(){

        return FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
    }
    
    private List<String> ottieniLinks(long id){
        
        List<String> links = new ArrayList<>();
        File dir_campo = new File(IMAGES_PATH+"/"+id);
        if ( dir_campo.exists() )
            if (dir_campo.isDirectory())
               for( String filename : dir_campo.list() )
                        links.add(id+"/"+filename);
        return links;
    }       
}
