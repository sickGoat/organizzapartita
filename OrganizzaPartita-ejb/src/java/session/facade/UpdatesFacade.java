/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Updates;
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
public class UpdatesFacade extends AbstractFacade<Updates> implements UpdatesFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UpdatesFacade() {
        super(Updates.class);
    }

    @Override
    public void inviaUpdates(List<Updates> aggiornamenti) {
        
        System.out.println("aggiornamenti = " + aggiornamenti.size());
        for ( Updates up : aggiornamenti ){
            System.out.println("notifico a  = " + up.getRichiesto().getUsername() );
            create(up);
        }
            
    }
    
    @Override
    public List<Updates> getNewUpdates(Account account){
        
        Query q = em.createNamedQuery("Updates.getNewUpdateUtente");
        q.setParameter("account_richiesto", account);
        return q.getResultList();
    
    }
    
    @Override
    public List<Updates> getOldUpdates(Account account,int firsresult){
        
        Query q = em.createNamedQuery("Updates.getOldUpdates");
        q.setParameter("account_richiesto", account);
        q.setFirstResult(firsresult).setMaxResults(5);
        
        return q.getResultList();
        
    }

    @Override
    public void deleteAllUpdates(Account richiesto) {
        
        Query q = em.createNamedQuery("Updates.deleteAll");
        q.setParameter("account_richiesto", richiesto);
        q.executeUpdate();
    }
    
    
}