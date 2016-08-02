/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package managedbean;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import entities.Account;
import entities.Gestore;
import entities.Gruppo;
import entities.Utente;
import exception.RegistrazioneNonRiuscitaException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.ejb.Remove;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.AjaxBehaviorEvent;
import javax.servlet.http.Part;
import session.facade.AccountFacadeLocal;
import session.stateless.GestoreUtentiLocal;

/**
 *
 * @author antonio
 */

public class RegistrazioneController implements Serializable{
    @EJB
    private AccountFacadeLocal accountFacade;
    
    @EJB
    private GestoreUtentiLocal gestoreUtenti;
    
    private static final String IMAGE_PATH ="/home/antonio/webapp/images/";
    private String message = "";
    private boolean username_non_disponibile = false;
    private boolean email_non_disponibile = false;
    private boolean isGestore = false;
    private String nome_utente = "";
    private String cognome_utente = "";
    private String data_utente = "";
    private String username_account = "";
    private String email_account = "";
    private String password_account = "";
    private String gruppo_account = "";
    private String vivea = "";
    private String provincia = "";
    private String numero_telefono = "";
    private Part foto_profilo;

    /**
     * Creates a new instance of RegistrazioneController
     */
    public RegistrazioneController() {
    }
    
    public String getNome_utente() {
        return nome_utente;
    }

    public void setNome_utente(String nome_utente) {
        this.nome_utente = nome_utente;
    }

    public String getCognome_utente() {
        return cognome_utente;
    }

    public void setCognome_utente(String cognome_utente) {
        this.cognome_utente = cognome_utente;
    }

    public String getData_utente() {
        return data_utente;
    }

    public void setData_utente(String data_utente) {
        this.data_utente = data_utente;
    }

    public String getUsername_account() {
        return username_account;
    }

    public void setUsername_account(String username_account) {
        this.username_account = username_account;
    }

    public String getEmail_account() {
        return email_account;
    }

    public void setEmail_account(String email_account) {
        this.email_account = email_account;
    }

    public String getPassword_account() {
        return password_account;
    }

    public void setPassword_account(String password_account) {
        
        String crypted_password = Hashing.sha256().hashString(password_account, Charsets.UTF_8).toString();
        this.password_account = crypted_password;
    }

    public String getGruppo_account() {
        return gruppo_account;
    }

    public void setGruppo_account(String gruppo_account) {
        this.gruppo_account = gruppo_account;
    }

    public boolean isUsername_non_disponibile() {
        return username_non_disponibile;
    }

    public void setUsername_non_disponibile(boolean username_non_disponibile) {
        this.username_non_disponibile = username_non_disponibile;
    }

    public boolean isEmail_non_disponibile() {
        return email_non_disponibile;
    }

    public void setEmail_non_disponibile(boolean email_non_disponibile) {
        this.email_non_disponibile = email_non_disponibile;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    
    public boolean isIsGestore() {
        return isGestore;
    }

    public void setIsGestore(boolean isGestore) {
        this.isGestore = isGestore;
    }

    public String getVivea() {
        return vivea;
    }

    public void setVivea(String vivea) {
        this.vivea = vivea;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public Part getFoto_profilo() {
        return foto_profilo;
    }

    public String getNumero_telefono() {
        return numero_telefono;
    }

    public void setNumero_telefono(String numero_telefono) {
        this.numero_telefono = numero_telefono;
    }
    
    public void setFoto_profilo(Part foto_profilo) {
        this.foto_profilo = foto_profilo;
    }
   
    public void changeIsGestore(AjaxBehaviorEvent event){
    
        setIsGestore(gruppo_account.equals(Gruppo.GESTORE));
    }
    
   
    public void verificaCredenziali(AjaxBehaviorEvent event){
        System.out.println("verifica");
        username_non_disponibile = false;
        email_non_disponibile = false;
        List<Account> accounts = accountFacade.verificaCredenziali(email_account, username_account);
        if(!accounts.isEmpty()){
            for ( Account a : accounts ){
                System.out.println("a.getEmail() = " + a.getEmail());
                System.out.println("a.getUsername() = " + a.getUsername());
                if (a.getEmail().equals(email_account))
                    email_non_disponibile = true;
                if (a.getUsername().equals(username_account))
                    username_non_disponibile = true;
            }
        }
        System.out.println("username_non_disponibile = " + username_non_disponibile);
        if (username_non_disponibile == true || email_non_disponibile == true)
              message = "Cambia i campi evienziati";
        else
              message = "";
               
    }
   
    @Remove
    public String registraUtente(){
        
        System.out.println("registrautente");
        Account account = new Account();
        account.setUsername(username_account);
        account.setPassword(password_account);
        account.setEmail(email_account);
        
        Gruppo gruppo = new Gruppo();
        gruppo.setNome_gruppo(gruppo_account);
        
        
        try{
            switch (gruppo_account) {
                case Gruppo.GIOCATORE:
                    Utente utente = new Utente();
                    utente.setNome(nome_utente);
                    utente.setCognome(cognome_utente);
                    utente.setViveACitta(vivea);
                    utente.setViveAProvincia(provincia);
                    gestoreUtenti.registraGiocatore(utente, account,gruppo);
                    if (!creaCartellaImmagini(username_account))
                        throw new RegistrazioneNonRiuscitaException("Registrazione non riuscita");
                    break;
                case Gruppo.GESTORE:
                    Gestore gestore = new Gestore();
                    gestore.setNome(nome_utente);
                    gestore.setCognome(cognome_utente);
                    gestore.setNumero_telefono(numero_telefono);
                    gestoreUtenti.registraGestore(gestore, account, gruppo);
                    break;
            }
        }catch(RegistrazioneNonRiuscitaException e){
            return "Errore";
        }
       
        return "riuscita";    
    }
    
    private boolean creaCartellaImmagini(String username){
    
        File uplatd = new File("/home/antonio/webapp/images/"+username);
        return uplatd.mkdirs();
        
    }
    
}
