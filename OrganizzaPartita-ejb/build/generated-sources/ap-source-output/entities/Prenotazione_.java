package entities;

import entities.Account;
import entities.Campo;
import entities.Invito;
import entities.Prenotazione.StatoPrenotazione;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Prenotazione.class)
public class Prenotazione_ { 

    public static volatile SingularAttribute<Prenotazione, Long> id;
    public static volatile SingularAttribute<Prenotazione, Account> prenotante;
    public static volatile SingularAttribute<Prenotazione, StatoPrenotazione> stato;
    public static volatile SingularAttribute<Prenotazione, Campo> campo;
    public static volatile ListAttribute<Prenotazione, Invito> inviti;
    public static volatile SingularAttribute<Prenotazione, Date> data_prenotazione;
    public static volatile SingularAttribute<Prenotazione, Date> orario_inizio;

}