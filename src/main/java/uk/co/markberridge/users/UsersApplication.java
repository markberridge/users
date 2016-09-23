package uk.co.markberridge.users;

import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

import org.hibernate.cfg.ImprovedNamingStrategy;

import uk.co.markberridge.users.dao.AuditTrailInterceptor;
import uk.co.markberridge.users.dao.EventDao;
import uk.co.markberridge.users.dao.UserDao;
import uk.co.markberridge.users.dao.UserDaoHibernate;
import uk.co.markberridge.users.dao.UserEventDaoHibernate;
import uk.co.markberridge.users.domain.User;
import uk.co.markberridge.users.domain.UserEvent;
import uk.co.markberridge.users.event.EventFeedGenerator;
import uk.co.markberridge.users.event.EventType;
import uk.co.markberridge.users.resource.EventResource;
import uk.co.markberridge.users.resource.UserResource;

public class UsersApplication extends Application<UsersConfiguration> {

    private static final FlywayBundle<UsersConfiguration> flyway = new FlywayBundle<UsersConfiguration>() {
        @Override
        public DataSourceFactory getDataSourceFactory(UsersConfiguration configuration) {
            return configuration.getMigrationDataSourceFactory();
        }
    };

    public static final Class<?>[] PERSISTENT_CLASSES = new Class[] { User.class, UserEvent.class };
    public static final HibernateBundle<UsersConfiguration> hibernate = new HibernateBundle<UsersConfiguration>(
            PERSISTENT_CLASSES[0], PERSISTENT_CLASSES) {

        @Override
        public DataSourceFactory getDataSourceFactory(UsersConfiguration configuration) {
            return configuration.getApplicationDataSourceFactory();
        }

        @Override
        protected void configure(org.hibernate.cfg.Configuration config) {
            // couldn't work out how this can be set in yml, so set here instead
            config.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
            config.setInterceptor(new AuditTrailInterceptor());
        }
    };

    public static void main(String[] args) throws Exception {
        if (args.length == 0) {
            String configFileName = new OverrideConfig("users.yml").getName();
            new UsersApplication().run(new String[] { "migrate", configFileName });
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
        bootstrap.addBundle(flyway);
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(UsersConfiguration config, Environment environment) {

        // DAO
        UserDao userDao = new UserDaoHibernate(hibernate.getSessionFactory());
        EventDao<UserEvent> eventDao = new UserEventDaoHibernate(hibernate.getSessionFactory());

        // Event Feed Generator
        EventFeedGenerator<UserEvent> eventGenerator = new EventFeedGenerator<>(UserEvent.class, EventType.USER,
                eventDao, config.getEventFeedConfiguration());

        // Resources
        environment.jersey().register(new UserResource(userDao));
        environment.jersey().register(new EventResource<>(eventGenerator));

        // Exception Mapping
        environment.jersey().register(UserAlreadyExistsException.Mapper.class);
    }
}
