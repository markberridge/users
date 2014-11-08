package uk.co.markberridge.users.domain;

import uk.co.markberridge.users.UsersRuntimeException;

import com.github.oxo42.stateless4j.StateMachine;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.base.Strings;

public class User {

    private final String username;
    private final String password;

    @SuppressWarnings("unused")
    private final StateMachine<State, Trigger> stateMachine;

    public User(UserBuilder builder) {
        this.username = builder.username;
        this.password = builder.password;
        this.stateMachine = buildStateMachine(builder.intialState);
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

        private String username;
        private String password = "letmein";
        private State intialState = State.ACTIVE;

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
                    .permit(Trigger.DISABLE, State.INACTIVE) //
                    .permit(Trigger.UPDATE_EMAIL, State.EMAIL_CONFIRM_REQUIRED);

            stateMachine.configure(State.INACTIVE) //
                    .permit(Trigger.ENABLE, State.ACTIVE);

        } catch (Exception e) {
            throw new UsersRuntimeException(e);
        }
        return stateMachine;
    }
}
