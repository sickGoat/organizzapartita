package entities;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.1.v20130918-rNA", date="2014-09-10T19:38:25")
@StaticMetamodel(ChangePasswordRequest.class)
public class ChangePasswordRequest_ { 

    public static volatile SingularAttribute<ChangePasswordRequest, String> ID;
    public static volatile SingularAttribute<ChangePasswordRequest, Date> time_of_request;
    public static volatile SingularAttribute<ChangePasswordRequest, Long> ID_account;

}