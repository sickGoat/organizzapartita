/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package session.stateless;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;
import entities.Account;
import entities.Amicizia;
import entities.ChangePasswordRequest;
import entities.Gestore;
import entities.Gruppo;
import entities.Updates;
import entities.Utente;
import exception.AccountNonValidoExcpetion;
import exception.RegistrazioneNonRiuscitaException;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import javax.annotation.Resource;
import javax.ejb.ApplicationException;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.naming.NamingException;
import messages.EmailMessages;
import messages.UpdateMessage;
import org.apache.taglibs.standard.tag.common.core.Util;
import session.facade.AccountFacadeLocal;
import session.facade.AmiciziaFacadeLocal;
import session.facade.ChangePasswordRequestFacadeLocal;
import session.facade.UpdatesFacadeLocal;
import session.facade.UtenteFacadeLocal;

/**
 *
 * @author antonio
 */
@Stateless
@ApplicationException(rollback = true)
public class GestoreUtenti implements GestoreUtentiLocal {
    @EJB
    private ChangePasswordRequestFacadeLocal changePasswordRequestFacade;
    @Resource(name = "mail/gmail")
    private Session mailgmail;
    @EJB
    private AmiciziaFacadeLocal amiciziaFacade;
    @EJB
    private UpdatesFacadeLocal updatesFacade;
    @EJB
    private UtenteFacadeLocal utenteFacade;
    @EJB
    private AccountFacadeLocal accountFacade;
    
    
    
    @Override
    public void registraGiocatore(Utente utente, Account account, Gruppo gruppo) throws RegistrazioneNonRiuscitaException{
        
            List<Account> accounts = accountFacade.verificaCredenziali(account.getEmail(), account.getUsername());
            if ( !accounts.isEmpty() ){
                    if (account.getUsername().equals(accounts.get(0).getUsername()))
                        throw new RegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException.USERNAME_NON_DISPONIBILE);
                    else
                        throw new RegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException.EMAIL_NON_DISPONIBILE);
           
            }   
            
            utente.setAccount(account);
            account.setUtente(utente);
            account.setGruppo(gruppo);
            gruppo.setUsername(account);
            
            utenteFacade.create(utente);
           
    }
    
    
    

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    @Override
    public void registraGestore(Gestore utente, Account account, Gruppo gruppo) throws RegistrazioneNonRiuscitaException {
            
            List<Account> accounts = accountFacade.verificaCredenziali(account.getEmail(), account.getUsername());
            if ( !accounts.isEmpty() ){
                    if (account.getUsername().equals(accounts.get(0).getUsername()))
                        throw new RegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException.USERNAME_NON_DISPONIBILE);
                    else
                        throw new RegistrazioneNonRiuscitaException(RegistrazioneNonRiuscitaException.EMAIL_NON_DISPONIBILE);
           
            }   
            
            utente.setAccount(account);
            account.setUtente(utente);
            account.setGruppo(gruppo);
            gruppo.setUsername(account);
            
            utenteFacade.create(utente);
           
    }


    @Override
    public void inviaRichiestaAmicizia(Account from, Account to) throws AccountNonValidoExcpetion {
        
        Account amico1 = accountFacade.find(from.getId());
        if ( amico1 == null )
            throw new AccountNonValidoExcpetion();
        
        Account amico2 = accountFacade.find(to.getId());
        if ( amico2 == null )
            throw new AccountNonValidoExcpetion();
        
        Amicizia amicizia = new Amicizia(amico1, amico2);
        amico1.getAmicizia1().add(amicizia);
        accountFacade.edit(amico2);
        
        
    }

    @Override
    public void rispondiRischiestaAmicizia(Account from , Account to , boolean accettata) throws AccountNonValidoExcpetion {
        
        Account amico1 = accountFacade.find(from.getId());
        if ( amico1 == null )
            throw new AccountNonValidoExcpetion();
        
        Account amico2 = accountFacade.find(to.getId());
        if ( amico2 == null )
            throw new AccountNonValidoExcpetion();
        
        Amicizia amicizia = amiciziaFacade.getAmicizia(from, to);
        
        if ( accettata ){   
            amicizia.setStato(Amicizia.StatoAmicizia.CONFERMATA);
            notifica(to,from, UpdateMessage.AMICIZIA_ACCETTAZIONE);
        }
        else{
            amiciziaFacade.remove(amicizia);
            notifica(to,from, UpdateMessage.AMICIZIA_RIFIUTO);
        }
    }
    
    @Override
    public void eliminaAccount(Account account){
        
        updatesFacade.deleteAllUpdates(account);
        accountFacade.remove(account);
    }
    
    /**
     * @param to = account destinatario
     * @param id = id dell'account mittente
     * @param amicizia = messaggio da inoltrare ( primo elemento della strina sara
     * l'id del mittente da recuperare attraverso il parsing della scritta)
     */
    private void notifica(Account from,Account to, String msg) {
        
        Updates notifica = new Updates();
        notifica.setRichiesto(to);
        notifica.setMittente(from);
        notifica.setMessaggio(msg);
        updatesFacade.create(notifica);
    
    }

    @Override
    public boolean recuperaCredenziali(String email) throws AccountNonValidoExcpetion {
        
       Account account = accountFacade.getAccountByEmail(email);
       if ( account == null )
           throw new AccountNonValidoExcpetion();
       
       UUID id_link = UUID.randomUUID();
       String urlencoded = Util.URLEncode(id_link.toString(), "UTF-8");
       String link =EmailMessages.PASSWORD_RECOVERY_LINK+urlencoded;
       String crypted_id = Hashing.sha256().hashString(urlencoded, Charsets.UTF_8).toString();
       ChangePasswordRequest change_request = new ChangePasswordRequest();
       change_request.setID(crypted_id);
       change_request.setTime_of_request(new Date());
       change_request.setID_account(account.getId());
       changePasswordRequestFacade.create(change_request);
       String body = EmailMessages.PASSWORD_RECOVERY_TEXT +"\n"+link;
       try{
           sendMail(email,EmailMessages.PASSWORD_RECOVERY_SUBJECT,body);
       }catch(NamingException | MessagingException e){
           return false;
       }
       
       return true;
    }

    private void sendMail(String email, String subject, String body) throws NamingException, MessagingException {
        MimeMessage message = new MimeMessage(mailgmail);
        message.setSubject(subject);
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email, false));
        message.setText(body);
        Transport.send(message);
    }

    @Override
    public void resetPassword(long id_account, String password) throws AccountNonValidoExcpetion {
        
        Account acc = accountFacade.find(id_account);
        if( acc == null )
            throw new AccountNonValidoExcpetion();
        acc.setPassword(Hashing.sha256().hashString(password, Charsets.UTF_8).toString());
        accountFacade.edit(acc);
        
    }
    
    
    
}