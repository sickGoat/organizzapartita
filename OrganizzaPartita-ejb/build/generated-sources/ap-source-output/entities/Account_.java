package entities;

import entities.Amicizia;
import entities.Gruppo;
import entities.Prenotazione;
import entities.Updates;
import entities.Utente;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Account.class)
public class Account_ { 

    public static volatile SingularAttribute<Account, Long> id;
    public static volatile ListAttribute<Account, Amicizia> amicizia2;
    public static volatile ListAttribute<Account, Amicizia> amicizia1;
    public static volatile ListAttribute<Account, Prenotazione> prenotazioni;
    public static volatile SingularAttribute<Account, String> username;
    public static volatile ListAttribute<Account, Updates> updates;
    public static volatile SingularAttribute<Account, String> email;
    public static volatile SingularAttribute<Account, Gruppo> gruppo;
    public static volatile SingularAttribute<Account, Utente> utente;
    public static volatile SingularAttribute<Account, String> password;

}