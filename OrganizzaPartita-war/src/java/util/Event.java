/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 *
 * @author antonio
 */
public class Event implements Serializable{
    
    private Date inizio;
    private Object descrizione;

  
    public Event() {
    }

    public Event(Date inizio, Object descrizione) {
        this.inizio = inizio;
        this.descrizione = descrizione;
    }

     public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Object getDescrizione() {
        return descrizione;
    }

    public void setDescrizione(Object descrizione) {
        this.descrizione = descrizione;
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 59 * hash + Objects.hashCode(this.inizio);
        hash = 59 * hash + Objects.hashCode(this.descrizione);
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
        final Event other = (Event) obj;
        if (!Objects.equals(this.inizio, other.inizio)) {
            return false;
        }
        
        if (!Objects.equals(this.descrizione, other.descrizione)) {
            return false;
        }
        return true;
    }
   
    
}