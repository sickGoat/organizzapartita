/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Invito;
import entities.Prenotazione;
import java.util.Date;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface InvitoFacadeLocal {

    void create(Invito invito);

    void edit(Invito invito);

    void remove(Invito invito);

    Invito find(Object id);

    List<Invito> findAll();

    List<Invito> findRange(int[] range);

    int count();
    
    public boolean verificaDisponibilita(Account invitato, Prenotazione prenotazione) ;
    
    public boolean verificaDisponibilita(Account invitato , Date giorno , Date orario );
    
    public void eliminaInvitiInCoda(Prenotazione p);
    
    public void eliminaInvitiNonConfermati(Account invitato, Date data);
    
    public Invito invitoPrenotazioneConfermata(Account invitato,Date data);
}