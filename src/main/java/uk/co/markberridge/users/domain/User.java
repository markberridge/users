package uk.co.markberridge.users.domain;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlAccessOrder;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorOrder;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.GenericGenerator;

import uk.co.markberridge.users.UsersRuntimeException;

import com.github.oxo42.stateless4j.StateMachine;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

@Entity
@Table(name = "USERS")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
@XmlAccessorOrder(XmlAccessOrder.ALPHABETICAL)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(generator = "hibernate-uuid")
    @GenericGenerator(name = "hibernate-uuid", strategy = "uuid2")
    @XmlElement
    private String id;

    @XmlElement
    private String username;

    private String password;

    @Transient
    private StateMachine<State, Trigger> stateMachine;

    @SuppressWarnings("unused")
    private User() {
        // marshaling
    }

    public User(UserBuilder builder) {
        this.id = builder.id;
        this.username = builder.username;
        this.password = builder.password;
        this.createdDate = builder.createdDate;
        this.stateMachine = buildStateMachine(builder.intialState);
    }

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getObscuredPassword() {
        Preconditions.checkNotNull(password);
        return password.substring(0, 1) + Strings.padEnd("", password.length() - 2, '*')
                + password.substring(password.length() - 1);

    }

    public static class UserBuilder {

        private String id;
        private String username;
        private String password = "letmein";
        private Date createdDate = new Date();
        private State intialState = State.ACTIVE;

        public UserBuilder from(User user) {
            this.id = user.id;
            this.username = user.username;
            this.password = user.password;
            this.createdDate = user.createdDate;
            return this;
        }

        public UserBuilder id(String id) {
            this.id = id;
            return this;
        }

        public UserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public UserBuilder password(String password) {
            this.password = password;
            return this;
        }

        public UserBuilder intialState(State intialState) {
            this.intialState = intialState;
            return this;
        }

        public User build() {
            Preconditions.checkNotNull(username, "username is null");
            Preconditions.checkNotNull(password, "password is null");
            Preconditions.checkNotNull(createdDate, "createdDate is null");
            Preconditions.checkNotNull(intialState, "intialState is null");
            return new User(this);
        }
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("username", username).add("password", password).toString();
    }

    public static enum State {
        ACTIVE, INACTIVE, EMAIL_CONFIRM_REQUIRED;
    }

    public static enum Trigger {
        CONFIRM_EMAIL, DISABLE, ENABLE, UPDATE_EMAIL;
    }

    private static StateMachine<State, Trigger> buildStateMachine(State intialState) {
        StateMachine<State, Trigger> stateMachine = new StateMachine<State, Trigger>(intialState);
        try {

            stateMachine.configure(State.EMAIL_CONFIRM_REQUIRED) //
                        .permit(Trigger.CONFIRM_EMAIL, State.ACTIVE);

            stateMachine.configure(State.ACTIVE)//
                        .permit(Trigger.DISABLE, State.INACTIVE)
                        //
                        .permit(Trigger.UPDATE_EMAIL, State.EMAIL_CONFIRM_REQUIRED);

            stateMachine.configure(State.INACTIVE) //
                        .permit(Trigger.ENABLE, State.ACTIVE);

        } catch (Exception e) {
            throw new UsersRuntimeException(e);
        }
        return stateMachine;
    }
}
