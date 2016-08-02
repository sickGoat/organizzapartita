/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Amicizia;
import entities.AmiciziaPK;
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
public class AmiciziaFacade extends AbstractFacade<Amicizia> implements AmiciziaFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public AmiciziaFacade() {
        super(Amicizia.class);
    }

    @Override
    public Amicizia getAmicizia(Account from, Account to) {
        
        Query q = em.createNamedQuery("Amicizia.getAmicizia");
        q.setParameter("from", from);
        q.setParameter("to", to);
        List result = q.getResultList();
        return   result.isEmpty() ? null : (Amicizia) result.get(0);
        
    }
    
    @Override
    public void eliminaAmico(Account from,Account to){
        
        System.out.println("Amicizia facade to = " + to.getId());
        System.out.println("Amicizia facade from = " + from.getId());
        AmiciziaPK key = new AmiciziaPK(from.getId(), to.getId());
        Amicizia amicizia = em.find(Amicizia.class, key);
        System.out.println("Amicizia facade amicizia==null = " + amicizia==null);
        em.remove(amicizia);
    }
    
    @Override
    public List<Amicizia> getRichiesteAmicizia(Account account){
    
        Query q = em.createNamedQuery("Amicizia.getRichieste");
        q.setParameter("current_account", account);
        
        
        return q.getResultList();
    
    }
}