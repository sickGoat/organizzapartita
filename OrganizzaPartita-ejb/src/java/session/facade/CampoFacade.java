/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Campo;
import entities.Gestore;
import exception.AccountNonValidoExcpetion;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author antonio
 */
@Stateless
public class CampoFacade extends AbstractFacade<Campo> implements CampoFacadeLocal {
    @EJB
    private AccountFacadeLocal accountFacade;
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CampoFacade() {
        super(Campo.class);
    }

    @Override
    public List<Campo> findCampiInRange(float lat, float lgt ,int meters_range) {
        
        List<Campo> campi = null;
        
        float delta_lat = (float) (lat + (180/ Math.PI )*(meters_range/6378137));
        float delta_lgt = (float) (lgt +  (180/ Math.PI )*(meters_range/6378137));
        
        String upLat = String.valueOf(lat+delta_lat);
        String downLat = String.valueOf(lat-delta_lat);
        String eastLgt = String.valueOf(lgt-delta_lgt);
        String westLgt = String.valueOf(lgt+delta_lgt);
        
        Query q = em.createNamedQuery("Campo.findByCoordinate");
        q.setParameter("upLtd", upLat.substring(0, upLat.length()-3)+"%");
        q.setParameter("downLtd", downLat.substring(0, downLat.length()-3)+"%");
        q.setParameter("eastLgt", eastLgt.substring(0, eastLgt.length()-3)+"%");
        q.setParameter("westLgt", westLgt.substring(0, westLgt.length()-3)+"%");
        
        campi = q.getResultList();
        return campi;
        
    }
    
    @Override
    public List<Campo> getCampiGestore(String username){
        
        Query q = em.createNamedQuery("Campo.getCampiByGestore");
        q.setParameter("username_gestore", username);
        
        return q.getResultList();
    }
    
    @Override
    public boolean isGestore(String username,Long id_campo){
        
        Account gestore = accountFacade.getAccount(username);
        Query q = em.createNamedQuery("Campo.isGestore");
        q.setParameter("account_gestore", gestore);
        q.setParameter("id_campo", id_campo);
        
        return !(q.getResultList().isEmpty());
    
    }

    @Override
    public long aggiungiCampo(String username_gestore, Campo campo) throws AccountNonValidoExcpetion {
        
        Account gestore = accountFacade.getAccount(username_gestore);
        if ( gestore == null )
            throw  new AccountNonValidoExcpetion();
        campo.setGestore_campo((Gestore) gestore.getUtente());
        em.persist(campo);
        em.flush();
        return campo.getId();
    }
}