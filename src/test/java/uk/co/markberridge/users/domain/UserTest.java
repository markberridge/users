package uk.co.markberridge.users.domain;

import static org.fest.assertions.api.Assertions.assertThat;

import org.junit.Test;

import uk.co.markberridge.users.domain.User.UserBuilder;

public class UserTest {

    @Test
    public void obscuredPassword() {
        UserBuilder user = new UserBuilder().username("user");
        assertThat(user.password("password").build().getObscuredPassword()).isEqualTo("p******d");
    }
}
