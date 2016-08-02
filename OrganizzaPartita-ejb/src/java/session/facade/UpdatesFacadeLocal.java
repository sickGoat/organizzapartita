/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Updates;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface UpdatesFacadeLocal {

    void create(Updates updates);

    void edit(Updates updates);

    void remove(Updates updates);

    Updates find(Object id);

    List<Updates> findAll();

    List<Updates> findRange(int[] range);

    int count();
    
    public void inviaUpdates(List<Updates> aggiornamenti);
    
    public List<Updates> getNewUpdates(Account utente);
    
    public List<Updates> getOldUpdates(Account utente,int firstResult);
    
    public void deleteAllUpdates(Account richiesto);
    
}