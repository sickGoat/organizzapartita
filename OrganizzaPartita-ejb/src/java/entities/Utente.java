/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author antonio
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id;
    
    @Basic
    protected String nome;
    
    @Basic
    protected String cognome;
    
    @Basic
    protected String viveACitta;
    
    @Basic
    protected String viveAProvincia;
    
    
    @OneToOne(mappedBy = "utente" ,cascade = {CascadeType.PERSIST,CascadeType.REMOVE} ,
            fetch = FetchType.EAGER)
    @JoinColumn(name = "id_account")
    protected Account account;
    
    public Long getId() {
        return id;
        
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCognome() {
        return cognome;
    }

    public void setCognome(String cognome) {
        this.cognome = cognome;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getViveACitta() {
        return viveACitta;
    }

    public void setViveACitta(String viveACitta) {
        this.viveACitta = viveACitta;
    }

    public String getViveAProvincia() {
        return viveAProvincia;
    }

    public void setViveAProvincia(String viveAProvincia) {
        this.viveAProvincia = viveAProvincia;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Utente other = (Utente) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
    
}