/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package util;

import entities.Prenotazione;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author antonio
 */
public class Scheduler implements Serializable {
    
    private Map<Date,Event> event_model;
    private Date inizio;
    private Date fine;
    private Date data;

    public Scheduler() {
    }

    public Scheduler(Date data , Date inizio , Date fine) {
        this.data = data;
        this.inizio = inizio;
        this.fine = fine;
    }

    public void createMap() {
        
        event_model = new TreeMap<>();
        Calendar c_inizio = setOrario(inizio);
        Calendar c_fine = setOrario(fine);
        while ( c_inizio.before(c_fine)){
            event_model.put(c_inizio.getTime(), null);
            c_inizio.add(Calendar.HOUR, 1);
        }
    }
    
    private Calendar setOrario(Date orario){
        
        Calendar c = Calendar.getInstance();
        Calendar c_util = Calendar.getInstance();
        c.setTime(data);
        c_util.setTime(orario);
        int ora = c_util.get(Calendar.HOUR_OF_DAY);
        c.set(Calendar.HOUR_OF_DAY, ora);
        c.set(Calendar.MINUTE, 0);
        c.set(Calendar.SECOND, 0);
        return c;
    
    }
    public Map<Date, Event> getEvent_model() {
        return event_model;
    }

    public void setEvent_model(Map<Date, Event> event_model) {
        this.event_model = event_model;
    }

    public Date getInizio() {
        return inizio;
    }

    public void setInizio(Date inizio) {
        this.inizio = inizio;
    }

    public Date getFine() {
        return fine;
    }

    public void setFine(Date fine) {
        this.fine = fine;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }
    
    
    public void addEvent(Event event){
        
        Calendar c = Calendar.getInstance();
        c.setTime(event.getInizio());
        event_model.put(c.getTime(), event);
            
    }
    
    public List<Event> getEvents(){
        
        List<Event> events = new ArrayList<>();
        for (Map.Entry entry : event_model.entrySet()) {
            Event event = (Event) entry.getValue();
            if ( event != null && !events.contains(event) )
                events.add(event);
        }
        
        return events;
    }
    
    public void setEvents(List<Event> events){
        
        for ( Event e : events ){
            Prenotazione p = (Prenotazione) e.getDescrizione();
            System.out.println("p = " + p.getCampo().getId());
            addEvent(e);
        }
    
    }
    
    
}
