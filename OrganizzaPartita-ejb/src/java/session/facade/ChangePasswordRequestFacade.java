/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.ChangePasswordRequest;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author antonio
 */
@Stateless
public class ChangePasswordRequestFacade extends AbstractFacade<ChangePasswordRequest> implements ChangePasswordRequestFacadeLocal {
    @PersistenceContext(unitName = "organizzapartitaAPP-ejbPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public ChangePasswordRequestFacade() {
        super(ChangePasswordRequest.class);
    }
    
}   