/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Campo;
import entities.Invito;
import entities.Invito.StatoInvito;
import entities.Prenotazione;
import exception.CampoNonTrovatoException;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface PrenotazioneFacadeLocal {

    void create(Prenotazione prenotazione);

    void edit(Prenotazione prenotazione);

    void remove(Prenotazione prenotazione);
    
    void refresh(Prenotazione prenotazione);

    Prenotazione find(Object id);

    List<Prenotazione> findAll();

    List<Prenotazione> findRange(int[] range);

    int count();
    
    public boolean verificaDisponibilita(Campo campo, Date data ,Date orario_inizio );
    
    public List<Invito> getInvitiPerStato(Prenotazione prenotazione, StatoInvito stato);
    
    public List<Prenotazione> getPrenotazioniPerCampo(Campo campo, Date data);

    public List<Prenotazione> getPrenotazioniPerCampo(Long campo_id, Date data) throws CampoNonTrovatoException;
    
    public Prenotazione getPrenotazionePerAccountData(Account prenotante,Date data);
    
    public boolean raggiuntoLimite(Account prenotante,Date data);
}