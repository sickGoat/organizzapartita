package entities;

import entities.Account;
import entities.Invito;
import entities.Updates.StatoUpdate;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Updates.class)
public class Updates_ { 

    public static volatile SingularAttribute<Updates, Long> id;
    public static volatile SingularAttribute<Updates, StatoUpdate> stato;
    public static volatile SingularAttribute<Updates, Invito> richiesta_partita;
    public static volatile SingularAttribute<Updates, Date> tempo;
    public static volatile SingularAttribute<Updates, Account> mittente;
    public static volatile SingularAttribute<Updates, Account> richiesto;
    public static volatile SingularAttribute<Updates, String> messaggio;

}