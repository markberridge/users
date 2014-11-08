package uk.co.markberridge.users;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.markberridge.users.dao.UserRepository;
import uk.co.markberridge.users.dao.UserRepositoryInMemory;
import uk.co.markberridge.users.resources.UserResource;

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
        // bootstrap.addBundle(bundle);
    }

    @Override
    public void run(UsersConfiguration configuration, Environment environment) {

        // DAO
        UserRepository userRepository = new UserRepositoryInMemory();

        // Resources
        environment.jersey().register(new UserResource(userRepository));

        // Exception Mapping
        environment.jersey().register(UserAlreadyExistsException.Mapper.class);
    }
}
