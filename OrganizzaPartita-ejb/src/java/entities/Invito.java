/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

/**
 *
 * @author antonio
 */
@Entity
@NamedQueries({ @NamedQuery(name = "Invito.invitiPerAccountPrenotazione" , 
        query = "SELECT i FROM Invito i WHERE i.invitato=:invitato AND i.prenotazione.data_prenotazione=:data "
                + "AND i.prenotazione.orario_inizio = :da  AND i.stato=entities.Invito.StatoInvito.CONFERMATO"),
        @NamedQuery( name = "Invito.invitoPerAccountData" , query = "SELECT i from Invito i WHERE i.prenotazione.data_prenotazione=:data AND i.prenotazione.orario_inizio=:ora_inizio AND i.stato = entities.Invito.StatoInvito.CONFERMATO AND i.invitato=:invitato"),
        @NamedQuery( name = "Invito.eliminaPerStato" , query = "DELETE FROM Invito AS i WHERE i.stato=:stato AND i.prenotazione=:prenotazione"),
        @NamedQuery( name = "Invito.eliminaInvitiInAttesaAccountData",
                     query = "DELETE  FROM Invito i WHERE i.prenotazione.data_prenotazione=:data AND i.stato <> entities.Invito.StatoInvito.CONFERMATO AND i.invitato=:invitato"),
        @NamedQuery( name = "Invito.prenConfermataData" , 
                     query = "SELECT i FROM Invito i WHERE i.prenotazione.data_prenotazione=:data AND i.prenotazione.stato=entities.Prenotazione.StatoPrenotazione.CONFERMATA AND i.invitato=:account_invitato")
                })
public class Invito implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum StatoInvito { IN_ATTESA , CONFERMATO , RIFIUTATO , IN_CODA }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @OneToOne(cascade = {CascadeType.REMOVE,CascadeType.PERSIST}, fetch = FetchType.EAGER)
    private Updates aggiornamento;
    @OneToOne
    Account invitato;
    @ManyToOne
    Prenotazione prenotazione;
    @Enumerated(EnumType.STRING)
    private StatoInvito stato;

    public Invito() {
    }
    
    public Invito(Account invitato, Prenotazione prenotazione) {
        this.invitato = invitato;
        this.prenotazione = prenotazione;
        this.stato = StatoInvito.IN_ATTESA;
    }
    
    public Invito(Account invitato, Prenotazione prenotazione, StatoInvito stato) {
        this.invitato = invitato;
        this.prenotazione = prenotazione;
        this.stato = stato;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Updates getAggiornamento() {
        return aggiornamento;
    }
    
    public void setAggiornamento(Updates aggiornamento) {
        this.aggiornamento = aggiornamento;
    }

    public Account getInvitato() {
        return invitato;
    }

    public void setInvitato(Account invitato) {
        this.invitato = invitato;
    }

    public Prenotazione getPrenotazione() {
        return prenotazione;
    }

    public void setPrenotazione(Prenotazione prenotazione) {
        this.prenotazione = prenotazione;
    }

    public StatoInvito getStato() {
        return stato;
    }

    public void setStato(StatoInvito stato) {
        this.stato = stato;
    }
    
}