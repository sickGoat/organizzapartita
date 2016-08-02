/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import entities.Campo;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.HttpServletRequest;
import org.primefaces.context.RequestContext;
import session.facade.CampoFacadeLocal;

/**
 *
 * @author antonio
 */
public class GestoreCampiController {
    @EJB
    private CampoFacadeLocal campoFacade;
    
    private List<Campo> campi_gestore = new ArrayList<>();
    private Campo current_campo;
   
    /**
     * Creates a new instance of GestoreCampiController
     */
    public GestoreCampiController() {
    }
    
    @PostConstruct
    public void init(){
        
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance()
                .getExternalContext().getRequest();
        if ( request.isUserInRole("GESTORE")){
            String username = request.getUserPrincipal().getName();
            campi_gestore = campoFacade.getCampiGestore(username);
        }
        
    }

    public List<Campo> getCampi_gestore() {
        return campi_gestore;
    }

    public void setCampi_gestore(List<Campo> campi_gestore) {
        this.campi_gestore = campi_gestore;
    }

    public Campo getCurrent_campo() {
        return current_campo;
    }

    public void setCurrent_campo(Campo current_campo) {
        this.current_campo = current_campo;
    }
    
    
    public void apriFormCampo(AjaxBehaviorEvent e, Campo c){
    
        current_campo = c;
        RequestContext.getCurrentInstance().execute("campo_dettagli.show()");
    }
    
    public void salvaModifiche(AjaxBehaviorEvent e){
    
        campoFacade.edit(current_campo);
        current_campo = null;
        RequestContext.getCurrentInstance().execute("campo_dettagli.close()");
    }
    
    public void eliminaCampo(Campo c){
        System.out.println("elimino = " + c.getId());
        campoFacade.remove(c);
    
    }
}
