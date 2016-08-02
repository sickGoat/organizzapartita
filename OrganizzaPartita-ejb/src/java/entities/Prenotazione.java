/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author antonio
 */
@Entity
@Table(name = "PRENOTAZIONE", uniqueConstraints =@UniqueConstraint(columnNames = {"prenotante_id", "data"}) )
@NamedQueries({
    @NamedQuery(name = "Prenotazione.VerificaDisponibilita" ,
                query = "SELECT p FROM Prenotazione"
                      + " p WHERE p.data_prenotazione =:data AND p.campo =:campo AND p.orario_inizio = :da"),
    @NamedQuery(name = "Prenotazione.getInvitiByStato" , 
                query = "SELECT inv FROM Prenotazione p JOIN p.inviti inv WHERE p.id = :idPrenotazione AND inv.stato = :stato"),
    @NamedQuery( name= "Prenotazione.getPrenotazioniPerCampo",
                 query = "Select p From Prenotazione p where p.campo=:campo AND p.data_prenotazione=:data"),
    @NamedQuery( name = "Prenotazion.getPrenotazionePerAccountData" , 
                 query = "SELECT p FROM Prenotazione p WHERE p.prenotante=:prenotante AND p.data_prenotazione=:data")
}
)
public class Prenotazione implements Serializable {
    private static final long serialVersionUID = 1L;
    
    public enum StatoPrenotazione { IN_ATTESA , CONFERMATA }
    
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "prenotante_id")
    private Account prenotante;
    @ManyToOne(fetch = FetchType.EAGER)
    private Campo campo;
    @Temporal(TemporalType.DATE)
    @Column
    private Date data_prenotazione;
    @Temporal(TemporalType.TIME )
    private Date orario_inizio;
    @Enumerated(EnumType.STRING)
    private StatoPrenotazione stato;
    @OneToMany(mappedBy = "prenotazione" ,cascade = {CascadeType.REMOVE , CascadeType.PERSIST} ,fetch = FetchType.EAGER)
    List<Invito> inviti;
    @Transient
    private int inviti_accettati;

    
    public Prenotazione() {
        this.stato = StatoPrenotazione.IN_ATTESA;
        this.inviti_accettati = 0;
    }

    public Prenotazione(Account prenotante, Campo campo, Date data_prenotazione, Date orario_inizio, Date orario_fine) {
        this.prenotante = prenotante;
        this.campo = campo;
        this.data_prenotazione = data_prenotazione;
        this.orario_inizio = orario_inizio;
        this.inviti_accettati = 0;
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    
    public Account getPrenotante() {
        return prenotante;
        
    }

    public void setPrenotante(Account prenotante) {
        this.prenotante = prenotante;
    }

    public Date getData_prenotazione() {
        return data_prenotazione;
    }

    public void setData_prenotazione(Date data_prenotazione) {
        this.data_prenotazione = data_prenotazione;
    }

    public Date getOrario_inizio() {
        return orario_inizio;
    }

    public void setOrario_inizio(Date orario_inizio) {
        this.orario_inizio = orario_inizio;
    }    
    
    public Campo getCampo() {
        return campo;
    }

    public void setCampo(Campo campo) {
        this.campo = campo;
    }

   
    public StatoPrenotazione getStato() {
        return stato;
    }

    public void setStato(StatoPrenotazione stato) {
        this.stato = stato;
    }

    public List<Invito> getInviti() {
         return inviti;
    }

    public void setInviti(List<Invito> inviti) {
        this.inviti = inviti;
    }

    public int getInviti_accettati() {
        
        inviti_accettati = 0;
        for ( Invito i : inviti )
            if( i.getStato()== Invito.StatoInvito.CONFERMATO )
                    inviti_accettati++;
         
        return inviti_accettati;
    }

    public void setInviti_accettati(int inviti_accettati) {
        this.inviti_accettati = inviti_accettati;
    }

    
   
}