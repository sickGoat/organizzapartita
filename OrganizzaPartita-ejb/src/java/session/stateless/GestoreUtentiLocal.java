/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.stateless;

import entities.Account;
import entities.Gestore;
import entities.Gruppo;
import entities.Utente;
import exception.AccountNonValidoExcpetion;
import exception.RegistrazioneNonRiuscitaException;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface GestoreUtentiLocal {

    void registraGiocatore(Utente utente, Account account, Gruppo gruppo) throws RegistrazioneNonRiuscitaException;
    
    void registraGestore(Gestore utente, Account account , Gruppo gruppo) throws RegistrazioneNonRiuscitaException;
    
    void inviaRichiestaAmicizia(Account from,Account to) throws AccountNonValidoExcpetion;
    
    void rispondiRischiestaAmicizia(Account from , Account to , boolean accettata) throws AccountNonValidoExcpetion;
    
    void eliminaAccount(Account acc);
    
    boolean recuperaCredenziali(String email) throws AccountNonValidoExcpetion;
    
    void resetPassword(long id_account,String password) throws AccountNonValidoExcpetion;
    
}