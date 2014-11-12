package uk.co.markberridge.users.activity;

import uk.co.markberridge.users.dao.UserDao;

public class DeleteUserActivity {

    private final UserDao dao;

    public DeleteUserActivity(UserDao dao) {
        this.dao = dao;
    }

    public void delete(String userId) {
        dao.deleteUserByUsername(userId);
    }
}
