package uk.co.markberridge.users.api;

import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.User.UserBuilder;

import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.google.common.base.Objects;

@JsonPropertyOrder({ "userId", "username", "password", "links" })
public class UserRepresentation extends Representation {

    private String username;
    private String password;

    public UserRepresentation() {
        // for Jackson
    }

    public UserRepresentation(String username, String password, Link... links) {
        super(links);
        this.username = username;
        this.password = password;
    }

    public UserRepresentation(User user, Link... links) {
        super(links);
        this.username = user.getUsername();
        this.password = user.getObscuredPassword();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public User buildNewUser() {
        return new UserBuilder().username(username)
                                .password(password)
                                .intialState(User.State.EMAIL_CONFIRM_REQUIRED)
                                .build();
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this).add("username", username).add("password", password).toString();
    }
}
