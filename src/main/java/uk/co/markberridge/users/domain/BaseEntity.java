package uk.co.markberridge.users.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@MappedSuperclass
public class BaseEntity implements Serializable {
    
    // String createdBy;
    @Temporal(TemporalType.TIMESTAMP)
    Date createdDate;

    // String updatedBy;
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedDate;
}