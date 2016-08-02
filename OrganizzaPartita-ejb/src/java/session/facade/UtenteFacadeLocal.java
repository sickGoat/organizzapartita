/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Utente;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface UtenteFacadeLocal {

    void create(Utente utente);

    void edit(Utente utente);

    void remove(Utente utente);

    Utente find(Object id);

    List<Utente> findAll();

    List<Utente> findRange(int[] range);

    int count();
    
}