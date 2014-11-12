package uk.co.markberridge.users;

/**
 * Used by Maven to drop and rebuild the database
 */
public class DbRecreate {

    public static void main(String... args) throws Exception {
        OverrideConfig config = new OverrideConfig("users.yml");
        new UsersApplication().run(new String[] { "recreate", config.getName() });
        new UsersApplication().run(new String[] { "seed", config.getName() });
    }
}
