/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * @author antonio
 */
public class AmiciziaPK implements Serializable {
    
    
    private Long account1;
    private Long account2;
    

    public AmiciziaPK() {
    }

    public AmiciziaPK(Long account1, Long account2) {
        this.account1 = account1;
        this.account2 = account2;
    }

    public Long getAccount1() {
        return account1;
    }

    public void setAccount1(Long account1) {
        this.account1 = account1;
    }

    public Long getAccount2() {
        return account2;
    }

    public void setAccount2(Long account2) {
        this.account2 = account2;
    }

    
    
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 37 * hash + Objects.hashCode(this.account1);
        hash = 37 * hash + Objects.hashCode(this.account2);
        
        return hash;
    }
    
    /* 
      Due chiavi primarie sono uguali se:
      due se la esiste gia la relazione:
        friend1 and friend2
        friend2 and friend1
    */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AmiciziaPK other = (AmiciziaPK) obj;
        
        if ( Objects.equals(this.account1, other.account1) && 
                Objects.equals(this.account2, other.account2))
            return true;
        
        return ( Objects.equals(this.account1, other.account2) &&
                Objects.equals(this.account2, other.account1));
           
    }
    
    
    
    
}