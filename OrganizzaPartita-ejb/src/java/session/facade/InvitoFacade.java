/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Invito;
import entities.Invito.StatoInvito;
import entities.Prenotazione;
import java.util.Calendar;
import java.util.Date;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TemporalType;

/**
 *
 * @author antonio
 */
@Stateless
public class InvitoFacade extends AbstractFacade<Invito> implements InvitoFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public InvitoFacade() {
        super(Invito.class);
    }
    
     @Override
    public boolean verificaDisponibilita(Account invitato, Prenotazione prenotazione) {
        
        Query q = em.createNamedQuery("Invito.invitiPerAccountPrenotazione");
        q.setParameter("invitato", invitato);
        q.setParameter("data", prenotazione.getData_prenotazione());
        q.setParameter("da", prenotazione.getOrario_inizio());
        
        return ( q.getResultList().isEmpty());       
    }

    @Override
    public boolean verificaDisponibilita(Account invitato,Date data, Date orario) {
        
        Query q = em.createNamedQuery("Invito.invitoPerAccountData");
        q.setParameter("invitato", invitato);
        q.setParameter("data", data,TemporalType.DATE);
        q.setParameter("ora_inizio", orario,TemporalType.TIME);
        Calendar c =Calendar.getInstance();
        System.out.println("data = " + data.toString());
        System.out.println("inizio = " + orario.toString());
        return q.getResultList().isEmpty();
    }
    
    @Override
    public void eliminaInvitiInCoda(Prenotazione p ){
        
        Query q = em.createNamedQuery("Invito.eliminaPerStato");
        q.setParameter("prenotazione", p);
        q.setParameter("stato", StatoInvito.IN_CODA);
        q.executeUpdate();
        
    }

    @Override
    public void eliminaInvitiNonConfermati(Account invitato, Date data) {
        
        Query q = em.createNamedQuery("Invito.eliminaInvitiInAttesaAccountData");
        q.setParameter("invitato", invitato);
        q.setParameter("data", data,TemporalType.DATE);
        q.executeUpdate();
    }

    @Override
    public Invito invitoPrenotazioneConfermata(Account invitato, Date data) {
        
        Query q = em.createNamedQuery("Invito.prenConfermataData");
        q.setParameter("data", data);
        q.setParameter("account_invitato", invitato);
        
        return (Invito) (q.getResultList().isEmpty() ? null : q.getResultList().get(0));
    }
    
}