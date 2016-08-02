/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.facade;

import entities.Campo;
import exception.AccountNonValidoExcpetion;
import java.util.List;
import javax.ejb.Local;

/**
 *
 * @author antonio
 */
@Local
public interface CampoFacadeLocal {

    void create(Campo campo);

    void edit(Campo campo);

    void remove(Campo campo);
    
    void refresh(Campo campo);

    Campo find(Object id);

    List<Campo> findAll();

    List<Campo> findRange(int[] range);

    int count();
        
    List<Campo> findCampiInRange(float lat, float lgt ,int meters);
    
    List<Campo> getCampiGestore(String username_gestore);
    
    boolean isGestore(String username,Long id_campo);
    
    long aggiungiCampo(String username_gestore,Campo campo) throws AccountNonValidoExcpetion;
}