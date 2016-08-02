/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

/**
 *
 * @author antonio
 */
@Entity
@IdClass(AmiciziaPK.class)
@NamedQueries({
@NamedQuery(name = "Amicizia.getAmiciByUtente", 
            query = "Select a from Amicizia a where (a.account1=:current_account OR a.account2=:current_account2) AND a.stato = entities.Amicizia.StatoAmicizia.CONFERMATA"),
@NamedQuery(name= "Amicizia.getAmicizia" ,
            query = "Select a from Amicizia a where a.account1=:from and a.account2=:to "),
@NamedQuery(name = "Amicizia.getRichieste",
            query = "Select a from Amicizia a where a.account2=:current_account AND a.stato = entities.Amicizia.StatoAmicizia.IN_ATTESA")
})
public class Amicizia implements Serializable {
    
    public static enum StatoAmicizia {
        CONFERMATA, IN_ATTESA
    }
    
    
    @Id
    @ManyToOne
    @JoinColumn(name = "friendA" ,referencedColumnName = "ID")
    private Account account1;
    @Id
    @ManyToOne
    @JoinColumn(name = "friendTo" , referencedColumnName = "ID")
    private Account account2;
    @Column(name = "stato")
    @Enumerated(EnumType.STRING)
    private StatoAmicizia stato;
    
    public Amicizia() {
        this.stato = StatoAmicizia.IN_ATTESA;
    }

    public Amicizia(Account account1, Account account2) {
        this.account1 = account1;
        this.account2 = account2;
        this.stato = StatoAmicizia.IN_ATTESA;
    }

    public Account getAccount1() {
        return account1;
    }

    public void setAccount1(Account account1) {
        this.account1 = account1;
    }

    public Account getAccount2() {
        return account2;
    }

    public void setAccount2(Account account2) {
        this.account2 = account2;
    }

    public StatoAmicizia getStato() {
        return stato;
    }

    public void setStato(StatoAmicizia stato) {
        this.stato = stato;
    }

      
}