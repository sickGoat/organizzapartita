/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 *
 * @author antonio
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Gestore.getByUsername" , 
                query = "SELECT g FROM Gestore g WHERE g.account.username=:username_gestore")
})
public class Gestore extends Utente implements Serializable {
    private static final long serialVersionUID = 1L;
    
    
    @OneToMany(mappedBy = "gestore_campo", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private List<Campo> campo;
    @Basic
    private String numero_telefono;

    
    public Gestore() {
    }

    public Gestore(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }

    public List<Campo> getCampo() {
        return campo;
    }

    public void setCampo(List<Campo> campo) {
        this.campo = campo;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }
    
    
    public String getNumero_telefono() {
        return numero_telefono;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 53 * hash + Objects.hashCode(this.campo);
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
        final Gestore other = (Gestore) obj;
        if (!Objects.equals(this.campo, other.campo)) {
            return false;
        }
        return true;
    }
    
}