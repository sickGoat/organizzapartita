package entities;

import entities.Gestore;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(Campo.class)
public class Campo_ { 

    public static volatile SingularAttribute<Campo, Long> id;
    public static volatile SingularAttribute<Campo, Gestore> gestore_campo;
    public static volatile SingularAttribute<Campo, Date> orario_apertura;
    public static volatile SingularAttribute<Campo, String> indirizzo;
    public static volatile SingularAttribute<Campo, Integer> numero_giocatori;
    public static volatile SingularAttribute<Campo, Date> orario_chiusura;
    public static volatile SingularAttribute<Campo, String> longitudine;
    public static volatile SingularAttribute<Campo, String> latitudine;
    public static volatile SingularAttribute<Campo, Double> prezzo_per_ora;

}