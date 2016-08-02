package entities;

import entities.Account;
import entities.Invito.StatoInvito;
import entities.Prenotazione;
import entities.Updates;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Invito.class)
public class Invito_ { 

    public static volatile SingularAttribute<Invito, Long> id;
    public static volatile SingularAttribute<Invito, Account> invitato;
    public static volatile SingularAttribute<Invito, Updates> aggiornamento;
    public static volatile SingularAttribute<Invito, StatoInvito> stato;
    public static volatile SingularAttribute<Invito, Prenotazione> prenotazione;

}