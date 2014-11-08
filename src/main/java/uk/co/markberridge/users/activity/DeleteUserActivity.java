package uk.co.markberridge.users.activity;

import uk.co.markberridge.users.dao.UserRepository;

public class DeleteUserActivity {

    private final UserRepository userRepository;

    public DeleteUserActivity(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void delete(String userId) {
        userRepository.deleteUser(userId);
    }
}
