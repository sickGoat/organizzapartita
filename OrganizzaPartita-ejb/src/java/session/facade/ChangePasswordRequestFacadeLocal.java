/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.ChangePasswordRequest;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface ChangePasswordRequestFacadeLocal {

    void create(ChangePasswordRequest changePasswordRequest);

    void edit(ChangePasswordRequest changePasswordRequest);

    void remove(ChangePasswordRequest changePasswordRequest);

    ChangePasswordRequest find(Object id);

    List<ChangePasswordRequest> findAll();

    List<ChangePasswordRequest> findRange(int[] range);

    int count();
    
}
