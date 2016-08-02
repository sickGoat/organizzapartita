/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

/**
 *
 * @author antonio
 */
@Entity
@Table(name = "account",uniqueConstraints = @UniqueConstraint(columnNames = {"email","username"}))
@NamedQueries({
    @NamedQuery(name = "Account.findByEmailANDUsername", 
                query = "SELECT a FROM Account a WHERE a.email=:email OR a.username=:username"),
    @NamedQuery(name = "Account.findByUsername" , 
                query = "Select a from Account a Where a.username=:username"),
    @NamedQuery(name = "Account.getPersoneVicine", 
                query = "Select a from Account a where a.username <>:username AND (a.utente.viveACitta=:citta OR a.utente.viveAProvincia=:provincia)"),
    @NamedQuery(name = "Account.findUser" , 
                query = "Select a from Account a where  a <>:current_account AND a.gruppo.nome_gruppo='GIOCATORE' AND ( a.utente.nome LIKE :ricerca_nome OR a.utente.cognome LIKE :ricerca_cognome )"),
    @NamedQuery(name = "Account.getAccountByEmail" ,
                query = "SELECT a FROM Account a WHERE a.email=:email")
})
public class Account implements Serializable {
    
   
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private static final long serialVersionUID = 1L;
    @Column(name = "username",unique = true , nullable = false )
    private String username;
    @Column(name = "password")
    private String password;
    @Basic
    protected String email;
    @OneToMany(mappedBy = "account1" , cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE} )
    private List<Amicizia> amicizia1;
    @OneToMany(mappedBy = "account2" , cascade = {CascadeType.PERSIST,CascadeType.REMOVE,CascadeType.MERGE} )
    private List<Amicizia> amicizia2;
    @OneToOne(fetch = FetchType.EAGER , cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Utente utente;
    @OneToMany(mappedBy = "richiesto" ,fetch = FetchType.LAZY ,targetEntity = Updates.class)
    private List<Updates> updates;
    @OneToOne(mappedBy = "username", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
    private Gruppo gruppo;
    @OneToMany(mappedBy = "prenotante" ,cascade = {CascadeType.REMOVE, CascadeType.PERSIST, CascadeType.MERGE})
    private List<Prenotazione> prenotazioni;
      

    public Account() {
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Gruppo getGruppo() {
        return gruppo;
    }

    public void setGruppo(Gruppo gruppo) {
        this.gruppo = gruppo;
    }
    
    
    public List<Amicizia> getAmicizia1() {
        return amicizia1;
    }

    public void setAmicizia1(List<Amicizia> amicizia1) {
        this.amicizia1 = amicizia1;
    }

    public List<Amicizia> getAmicizia2() {
        return amicizia2;
    }

    public void setAmicizia2(List<Amicizia> amicizia2) {
        this.amicizia2 = amicizia2;
    }

    public Utente getUtente() {
        return utente;
    }

    public void setUtente(Utente utente) {
        this.utente = utente;
    }
    
    public List<Updates> getUpdates() {
        return updates;
    }

    public void setUpdates(List<Updates> updates) {
        this.updates = updates;
    }

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioni;
    }

    public void setPrenotazioni(List<Prenotazione> prenotazioni) {
        this.prenotazioni = prenotazioni;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 23 * hash + Objects.hashCode(this.id);
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
        final Account other = (Account) obj;
        
        return this.id.equals(other.getId());
    }
    
    
}