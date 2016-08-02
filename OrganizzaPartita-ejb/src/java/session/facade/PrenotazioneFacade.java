/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Campo;
import entities.Invito;
import entities.Prenotazione;
import exception.CampoNonTrovatoException;
import java.util.Date;
import java.util.List;
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
public class PrenotazioneFacade extends AbstractFacade<Prenotazione> implements PrenotazioneFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;
   

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public PrenotazioneFacade() {
        super(Prenotazione.class);
    }
    
     @Override
    public boolean verificaDisponibilita(Campo campo, Date data ,Date orario_inizio ){
           
            Query q = em.createNamedQuery("Prenotazione.VerificaDisponibilita");
            System.out.println("data = " + data.toString());
            System.out.println("orario_inizio = " + orario_inizio.toString());
            q.setParameter("campo", campo);
            q.setParameter("data", data,TemporalType.DATE);
            q.setParameter("da", orario_inizio,TemporalType.TIME);
            /*Se la lista è vuota il campo è disponibile*/
            return (q.getResultList().isEmpty());
    
    }

    @Override
    public List<Invito> getInvitiPerStato(Prenotazione prenotazione, Invito.StatoInvito stato) {
        
        Query q = em.createNamedQuery("Prenotazione.getInvitiByStato");
        q.setParameter("idPrenotazione", prenotazione.getId());
        q.setParameter("stato", stato );
        List<Invito> inviti = q.getResultList();
        return inviti;
    }

    @Override
    public List<Prenotazione> getPrenotazioniPerCampo(Campo campo, Date data) {
               
        Query q = em.createNamedQuery("Prenotazione.getPrenotazioniPerCampo");
        q.setParameter("campo", campo);
        q.setParameter("data", data);
        List<Prenotazione> prenotazioni = q.getResultList();
        return prenotazioni;
    }
    
   @Override
   public List<Prenotazione> getPrenotazioniPerCampo(Long id_campo, Date data) throws CampoNonTrovatoException{
        
        Campo campo = em.find(Campo.class,id_campo);
        if ( campo == null )
            throw new CampoNonTrovatoException();
        
        Query q = em.createNamedQuery("Prenotazione.getPrenotazioniPerCampo");
        q.setParameter("campo", campo);
        q.setParameter("data", data,TemporalType.DATE);
        List<Prenotazione> prenotazioni = q.getResultList();
        
        return prenotazioni;  
   }

    @Override
    public Prenotazione getPrenotazionePerAccountData(Account prenotante, Date data) {
        
        Query q = em.createNamedQuery("Prenotazion.getPrenotazionePerAccountData");
        q.setParameter("prenotante", prenotante);
        q.setParameter("data", data,TemporalType.DATE);
        
        return (Prenotazione) (q.getResultList().isEmpty() ? null : q.getResultList().get(0));
    
    }

    @Override
    public boolean raggiuntoLimite(Account prenotante, Date data) {
        
        
        Query q = em.createNamedQuery("Prenotazion.getPrenotazionePerAccountData");
        q.setParameter("prenotante", prenotante);
        q.setParameter("data", data,TemporalType.DATE);
        
        return !q.getResultList().isEmpty();
    }
}