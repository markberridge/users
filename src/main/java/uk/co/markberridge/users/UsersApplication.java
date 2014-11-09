package uk.co.markberridge.users;

import io.dropwizard.Application;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import uk.co.markberridge.users.dao.EventDao;
import uk.co.markberridge.users.dao.UserEventDaoInMemory;
import uk.co.markberridge.users.dao.UserRepository;
import uk.co.markberridge.users.dao.UserRepositoryInMemory;
import uk.co.markberridge.users.domain.UserEvent;
import uk.co.markberridge.users.event.EventFeedGenerator;
import uk.co.markberridge.users.event.EventType;
import uk.co.markberridge.users.resource.EventResource;
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
        // bootstrap.addBundle(bundle);
    }

    @Override
    public void run(UsersConfiguration config, Environment environment) {

        // DAO
        UserRepository userRepository = new UserRepositoryInMemory();
        EventDao<UserEvent> eventDao = new UserEventDaoInMemory(userRepository);

        // Event Feed Generator
        EventFeedGenerator<UserEvent> eventGenerator = new EventFeedGenerator<>(UserEvent.class, EventType.USER,
                eventDao, config.getEventFeedConfiguration());

        // Resources
        environment.jersey().register(new UserResource(userRepository));
        environment.jersey().register(new EventResource<UserEvent>(eventGenerator));

        // Exception Mapping
        environment.jersey().register(UserAlreadyExistsException.Mapper.class);
    }
}
