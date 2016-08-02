/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface AccountFacadeLocal {

    void create(Account account);

    void edit(Account account);

    void remove(Account account);

    Account find(Object id);

    List<Account> findAll();

    List<Account> findRange(int[] range);

    int count();
    
    List<Account> verificaCredenziali(String email , String username );
    
    Account getAccount(String username);
    
    List<Account> getAmici(Account a);
    
    List<Account> getPersonePotrestiConoscere(Account a);
    
    List<Account> getPersoneVicine(Account a);
    
    List<Account> getUtentiByPattern(String pattern,Account acc);
    
    List<String> getAccountByUserOrEmail(String username,String email);
    
    Account getAccountByEmail(String email);
    
}