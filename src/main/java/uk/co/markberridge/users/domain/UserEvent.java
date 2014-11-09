package uk.co.markberridge.users.domain;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.AttributeAccessor;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import uk.co.markberridge.users.event.Event;

import com.google.common.base.Objects;
import com.google.common.primitives.Longs;

@Entity
@XmlRootElement
@AttributeAccessor("field")
@XmlAccessorType(XmlAccessType.NONE)
@Table(name = "USER_EVENT")
public class UserEvent implements Event<UserEvent> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AttributeAccessor("property")
    @Column(name = "ATOM_EVENT_ID")
    @XmlElement
    private Long id;

    @Column(name = "CREATED_DATE", nullable = false)
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime createddate = new LocalDateTime();

    @Column(name = "TAG_URI")
    private String tagUri;

    @XmlElement
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "USERS_ID")
    private User user;

    public UserEvent(User user) {
        this();
        this.user = user;
    }

    // @MarshallingConstructor
    private UserEvent() {
        this.tagUri = "urn:uuid:" + UUID.randomUUID().toString();
    }

    @Override
    public long getId() {
        return id == null ? 0 : id.longValue();
    }

    @Override
    public String getTagUri() {
        return this.tagUri;
    }

    @Override
    public LocalDateTime getCreatedDate() {
        return createddate;
    }

    @Override
    public String getEventType() {
        return "User";
    }

    public User getUser() {
        return user;
    }

    @Override
    public int compareTo(UserEvent o) {
        return Longs.compare(o.getId(), getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(user);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        UserEvent other = (UserEvent) obj;
        return Objects.equal(user, other.user);
    }

    @Override
    public String toString() {
        return getClass().getName() + "[" + getId() + "]" + ")";
    }

}
