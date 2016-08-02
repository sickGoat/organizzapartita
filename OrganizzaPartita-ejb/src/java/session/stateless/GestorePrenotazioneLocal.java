/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.stateless;

import entities.Account;
import entities.Invito;
import entities.Prenotazione;
import exception.InvitoGiaModificatoException;
import exception.PrenotazioneNonRiuscitaException;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface GestorePrenotazioneLocal {

    void prenota(Prenotazione p , List<Account>invitati,Account prenotante) throws PrenotazioneNonRiuscitaException;        
       
    void rifiutaInvito(Invito invito) throws InvitoGiaModificatoException;
    
    void accettaInvito(Invito invito) throws InvitoGiaModificatoException;
    
    void eliminaPrenotazione(Prenotazione p , String message) ;
    
}   