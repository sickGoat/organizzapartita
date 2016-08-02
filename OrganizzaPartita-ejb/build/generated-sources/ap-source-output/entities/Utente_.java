package entities;

import entities.Account;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Utente.class)
public class Utente_ { 

    public static volatile SingularAttribute<Utente, Long> id;
    public static volatile SingularAttribute<Utente, Account> account;
    public static volatile SingularAttribute<Utente, String> nome;
    public static volatile SingularAttribute<Utente, String> cognome;
    public static volatile SingularAttribute<Utente, String> viveACitta;
    public static volatile SingularAttribute<Utente, String> viveAProvincia;

}