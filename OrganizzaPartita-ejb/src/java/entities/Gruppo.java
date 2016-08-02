/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

/**
 *
 * @author antonio
 */
@Entity
public class Gruppo implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static final String GIOCATORE = "GIOCATORE";
    public static final String GESTORE = "GESTORE";
    
   
    @Basic
    private String nome_gruppo;
    
    @Id
    @JoinColumn(name = "username", referencedColumnName = "username")
    @OneToOne(optional = false)
    private Account username;

    public Gruppo() {
    }
    
    public Gruppo(String nome_gruppo) {
        this.nome_gruppo = nome_gruppo;
    }

    public String getNome_gruppo() {
        return nome_gruppo;
    }

    public void setNome_gruppo(String nome_gruppo) {
        this.nome_gruppo = nome_gruppo;
    }

    public Account getUsername() {
        return username;
    }

    public void setUsername(Account username) {
        this.username = username;
    }
    
    
    @Override
    public int hashCode() {
        int hash = 0;
        hash += (username != null ? username.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Gruppo)) {
            return false;
        }
        Gruppo other = (Gruppo) object;
        if ((this.username == null && other.username != null) || (this.username != null && !this.username.equals(other.username))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Gruppo[ id=" + username + " ]";
    }
    
}