/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Account;
import entities.Amicizia;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 *
 * @author antonio
 */
@Stateless
public class AccountFacade extends AbstractFacade<Account> implements AccountFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
    public AccountFacade() {
        super(Account.class);
    }
    
    @Override
    public List<Account> verificaCredenziali(String email, String username ){
        
            Query q = em.createNamedQuery("Account.findByEmailANDUsername");
            q.setParameter("email", email);
            q.setParameter("username", username);
            
            return q.getResultList();
            
    }

    @Override
    public Account getAccount(String username) {

        Query q = em.createNamedQuery("Account.findByUsername");
        q.setParameter("username", username);
        Account account = (Account) q.getResultList().get(0);
        account.getUtente();
        return account;
    }

    @Override
    public List<Account> getAmici(Account a) {

        List<Amicizia> amicizie = new ArrayList<>();
        List<Account> amici = new ArrayList<>();
        Query q = em.createNamedQuery("Amicizia.getAmiciByUtente");
        q.setParameter("current_account", a);
        q.setParameter("current_account2", a);
        amicizie = q.getResultList();
        for ( Amicizia amicizia : amicizie ){
            if ( amicizia.getAccount1().equals(a) )
                amici.add(amicizia.getAccount2());
            else
                amici.add(amicizia.getAccount1());     
        }
       
        return amici;
    }
    
    @Override
    public List<Account> getPersonePotrestiConoscere(Account a){
        System.out.println("a = " + a.getUsername());
        HashMap<Account,Integer> potresti_conoscere = new HashMap<>();
        List<Account> amici_di_a = getAmici(a);
        for ( Account amico1 : amici_di_a ){
            List<Account> amici_di_amico1 = getAmici(amico1);
            for ( Account amico2 : amici_di_amico1 ){
                if(!amico2.equals(a)){
                    System.out.println("amico2.equals(a) = " + amico2.equals(a));
                    System.out.println("amico2.getUsername() = " + amico2.getUsername());
                    if ( !amici_di_a.contains(amico2)){
                        if ( potresti_conoscere.containsKey(amico2))
                              potresti_conoscere.put(amico2,potresti_conoscere.get(amico2)+1);
                        else
                              potresti_conoscere.put(amico2, new Integer(1));            
                        }
                    }
                }
            }
        
        TreeMap<Account,Integer> sorted_map = new TreeMap<>(new ValueComparator(potresti_conoscere));
        sorted_map.putAll(potresti_conoscere);
        List<Account> suggerimenti = new ArrayList<>(sorted_map.keySet());
        return suggerimenti;
    }
    
    @Override
    public List<Account> getPersoneVicine(Account a){
        
        Query q = em.createNamedQuery("Account.getPersoneVicine");
        q.setParameter("citta", a.getUtente().getViveACitta());
        q.setParameter("provincia", a.getUtente().getViveAProvincia());
        q.setParameter("username", a.getUsername());
       
        return (List<Account>) q.getResultList();
    
    }
    
    @Override
    public List<Account> getUtentiByPattern(String pattern,Account account){
        System.out.println("account = " + account.getUsername());
        System.out.println("pattern = " + pattern);
        Query q = em.createNamedQuery("Account.findUser");
        q.setParameter("ricerca_nome", "%"+pattern+"%");
        q.setParameter("ricerca_cognome","%"+ pattern+"%");
        q.setParameter("current_account", account);
        return (List<Account>) q.getResultList();
    }

    @Override
    public List<String> getAccountByUserOrEmail(String username, String email) {
        
         Query q = em.createNamedQuery("Account.findByEmailANDUsername");
         System.out.println("FACADE username = " + username);
         System.out.println("FACADE email = " + email);
         q.setParameter("email", email);
         q.setParameter("username", username);
         List<Account> accounts = q.getResultList();
         List<String> indirizzi = new ArrayList<>();
         if ( !accounts.isEmpty() )
             for ( Account a : accounts ){
                 indirizzi.add(a.getEmail());
                 System.out.println("a.getEmail() = " + a.getEmail());
             }
         return indirizzi;
    }

    @Override
    public Account getAccountByEmail(String email) {

        Query q = em.createNamedQuery("Account.getAccountByEmail");
        q.setParameter("email", email);
        
        return (Account) q.getResultList().get(0);
    }
    
    
}


 class ValueComparator implements Comparator<Account>{
        Map<Account,Integer> map;
        
        public ValueComparator(Map<Account,Integer> mappa){
            map = mappa;
        }
        
        @Override
        public int compare(Account t, Account t1) {
            if (map.get(t) <= map.get(t1))
                return -1;
            else
                return 1;
        }
 }
    
        

    