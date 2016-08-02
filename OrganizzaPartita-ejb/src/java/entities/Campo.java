/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author antonio
 */
@Entity
@NamedQueries({@NamedQuery(name = "Campo.findByCoordinate" , 
                           query = "Select c From Campo c where (c.latitudine LIKE :upLtd OR c.latitudine LIKE :downLtd) AND (c.longitudine LIKE :eastLgt OR c.longitudine LIKE :westLgt)"),
               @NamedQuery(name = "Campo.getCampiByGestore" , 
                           query = "SELECT c FROM Campo c WHERE c.gestore_campo.account.username=:username_gestore"),
               @NamedQuery(name = "Campo.isGestore" , 
                           query = "SELECT c FROM Campo c WHERE c.id=:id_campo AND c.gestore_campo.account=:account_gestore")
})
public class Campo implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Basic
    private String indirizzo;
    @Basic
    private String latitudine;
    @Basic
    private String longitudine;
    @Basic
    private double prezzo_per_ora;
    @Basic
    private int numero_giocatori;
    @Temporal(TemporalType.TIME)
    private Date orario_apertura;
    @Temporal(TemporalType.TIME)
    private Date orario_chiusura;
    @ManyToOne
    private Gestore gestore_campo;
    @OneToMany(mappedBy = "campo" ,cascade = CascadeType.REMOVE )
    private List<Prenotazione> prenotazioni;

    public Campo() {
    }

    public Campo(String indirizzo, double prezzo_per_ora, String numero_telefono, String mail_gestore, int numero_giocatori) {
        this.indirizzo = indirizzo;
        this.prezzo_per_ora = prezzo_per_ora;
        this.numero_giocatori = numero_giocatori;
    }
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getIndirizzo() {
        return indirizzo;
    }

    public void setIndirizzo(String indirizzo) {
        this.indirizzo = indirizzo;
    }

    public double getPrezzo_per_ora() {
        return prezzo_per_ora;
    }

    public void setPrezzo_per_ora(double prezzo_per_ora) {
        this.prezzo_per_ora = prezzo_per_ora;
    }

    public int getNumero_giocatori() {
        return numero_giocatori;
    }

    public void setNumero_giocatori(int numero_giocatori) {
        this.numero_giocatori = numero_giocatori;
    }

    public Gestore getGestore_campo() {
        return gestore_campo;
    }

    public void setGestore_campo(Gestore gestore_campo) {
        this.gestore_campo = gestore_campo;
    }

    public String getLatitudine() {
        return latitudine;
    }

    public void setLatitudine(String latitudine) {
        this.latitudine = latitudine;
    }

    public String getLongitudine() {
        return longitudine;
    }

    public void setLongitudine(String longitudine) {
        this.longitudine = longitudine;
    }

    public Date getOrario_apertura() {
        return orario_apertura;
    }

    public void setOrario_apertura(Date orario_apertura) {
        this.orario_apertura = orario_apertura;
    }

    public Date getOrario_chiusura() {
        return orario_chiusura;
    }

    public void setOrario_chiusura(Date orario_chiusura) {
        this.orario_chiusura = orario_chiusura;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Objects.hashCode(this.id);
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
        final Campo other = (Campo) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }
    
}