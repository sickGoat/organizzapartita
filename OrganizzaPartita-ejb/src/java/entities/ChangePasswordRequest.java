/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package entities;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author antonio
 */
@Entity
public class ChangePasswordRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(length = 255)
    private String ID;
    @Temporal(TemporalType.TIMESTAMP)
    private Date time_of_request;
    @Basic
    private long ID_account;

    public ChangePasswordRequest() {
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public Date getTime_of_request() {
        return time_of_request;
    }

    public void setTime_of_request(Date time_of_request) {
        this.time_of_request = time_of_request;
    }

    public long getID_account() {
        return ID_account;
    }

    public void setID_account(long ID_account) {
        this.ID_account = ID_account;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 89 * hash + Objects.hashCode(this.ID);
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
        final ChangePasswordRequest other = (ChangePasswordRequest) obj;
        if (!Objects.equals(this.ID, other.ID)) {
            return false;
        }
        return true;
    }
    
    
}