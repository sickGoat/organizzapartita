/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author antonio
 */
@Entity
@NamedQueries({
    @NamedQuery(name = "Updates.getNewUpdateUtente" , 
                query = "SELECT u from Updates u where u.richiesto=:account_richiesto AND u.stato=entities.Updates.StatoUpdate.NON_VISUALIZZATO"),
    @NamedQuery(name = "Updates.getUpdatesPerPrenotazione" , 
                query = "SELECT u FROM Updates u WHERE u.richiesta_partita.prenotazione=:prenotazione"),
    @NamedQuery(name = "Updates.getOldUpdates",
                query = "SELECT u FROM Updates u WHERE u.richiesto=:account_richiesto ORDER BY u.tempo DESC"),
    @NamedQuery(name = "Updates.deleteAll",
                query = "DELETE FROM Updates AS u WHERE u.richiesto=:account_richiesto")
})
public class Updates implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public static enum StatoUpdate { VISUALIZZATO , NON_VISUALIZZATO }
     
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne
    private Account richiesto;
    @OneToOne
    private Account mittente;
    @Basic
    private String messaggio;
    @OneToOne(mappedBy = "aggiornamento")
    private Invito richiesta_partita;
    @Temporal(TemporalType.TIMESTAMP)
    private final Date tempo;
    
    @Enumerated(EnumType.ORDINAL)
    private StatoUpdate stato;

    public Updates() {
        this.stato = StatoUpdate.NON_VISUALIZZATO;
        this.tempo = new Date();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Account getRichiesto() {
        return richiesto;
    }

    public void setRichiesto(Account richiesto) {
        this.richiesto = richiesto;
    }

    public String getMessaggio() {
        return messaggio;
    }

    public void setMessaggio(String messaggio) {
        this.messaggio = messaggio;
    }

    public StatoUpdate getStato() {
        return stato;
    }

    public void setStato(StatoUpdate stato) {
        this.stato = stato;
    }

    public Account getMittente() {
        return mittente;
    }

    public void setMittente(Account mittente) {
        this.mittente = mittente;
    }

    public Invito getRichiesta_partita() {
        return richiesta_partita;
    }

    public void setRichiesta_partita(Invito richiesta_partita) {
        this.richiesta_partita = richiesta_partita;
    }

    public Date getTimestamp() {
        return tempo;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Updates)) {
            return false;
        }
        Updates other = (Updates) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Update[ id=" + id + " ]";
    }
    
}