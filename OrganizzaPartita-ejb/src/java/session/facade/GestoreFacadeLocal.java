/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Campo;
import entities.Gestore;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface GestoreFacadeLocal {

    void create(Gestore gestore);

    void edit(Gestore gestore);

    void remove(Gestore gestore);

    Gestore find(Object id);

    List<Gestore> findAll();

    List<Gestore> findRange(int[] range);

    int count();
    
    Gestore getGestore(String username);
    
    
}