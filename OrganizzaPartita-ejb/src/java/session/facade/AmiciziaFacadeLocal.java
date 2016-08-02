/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Amicizia;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface AmiciziaFacadeLocal {

    void create(Amicizia amicizia);

    void edit(Amicizia amicizia);

    void remove(Amicizia amicizia);

    Amicizia find(Object id);

    List<Amicizia> findAll();

    List<Amicizia> findRange(int[] range);

    int count();
    
    Amicizia getAmicizia(Account from,Account to) ;
    
    void eliminaAmico(Account from,Account to);
    
    List<Amicizia> getRichiesteAmicizia(Account account);
}