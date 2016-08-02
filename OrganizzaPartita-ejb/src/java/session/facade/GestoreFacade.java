/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Campo;
import entities.Gestore;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author antonio
 */
@Stateless
public class GestoreFacade extends AbstractFacade<Gestore> implements GestoreFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public GestoreFacade() {
        super(Gestore.class);
    }
    
    

    @Override
    public Gestore getGestore(String username) {
        
        Query q = em.createNamedQuery("Gestore.getByUsername");
        q.setParameter("username_gestore", username);
        List<Gestore> gestore = q.getResultList();
        
        return !gestore.isEmpty() ? gestore.get(0) : null;
    }
   
    
}