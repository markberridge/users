package uk.co.markberridge.users;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.markberridge.users.dao.UserDao;
import uk.co.markberridge.users.dao.UserDaoInMemory;
import uk.co.markberridge.users.resource.UserResource;

public class UsersApplication extends Application<UsersConfiguration> {

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            String configFileName = new OverrideConfig("users.yml").getName();
            new UsersApplication().run(new String[] { "server", configFileName });
        } else {
            new UsersApplication().run(args);
        }
    }

    @Override
    public String getName() {
        return "users";
    }

    @Override
    public void initialize(Bootstrap<UsersConfiguration> bootstrap) {
        // TODO
    }

    @Override
    public void run(UsersConfiguration config, Environment environment) {

        // DAO
        UserDao userDao = new UserDaoInMemory();

        // Resources
        environment.jersey().register(new UserResource(userDao));

        // Exception Mapping
        environment.jersey().register(UserAlreadyExistsException.Mapper.class);
    }
}
