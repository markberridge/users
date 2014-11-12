package uk.co.markberridge.users;

import io.dropwizard.Bundle;
import io.dropwizard.Configuration;
import io.dropwizard.cli.ConfiguredCommand;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.db.DatabaseConfiguration;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Generics;
import net.sourceforge.argparse4j.inf.Namespace;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.googlecode.flyway.core.Flyway;
import com.googlecode.flyway.core.api.MigrationInfoService;

public abstract class FlywayBundle<T extends Configuration> implements Bundle, DatabaseConfiguration<T> {

    public static Logger log = LoggerFactory.getLogger(FlywayBundle.class); 
    
    @Override
    public final void initialize(Bootstrap<?> bootstrap) {
        Class<T> klass = Generics.getTypeParameter(getClass(), Configuration.class);
        bootstrap.addCommand(new MigrationCommand<>(this, klass));
        bootstrap.addCommand(new RecreateFromScratchCommand<>(this, klass));
        bootstrap.addCommand(new SeedCommand<>(this, klass));
    }

    @Override
    public final void run(Environment environment) {
        // nothing doing
    }

    private static class MigrationCommand<T extends Configuration> extends ConfiguredCommand<T> {

        private final DatabaseConfiguration<T> databaseConfiguration;
        private final Class<T> klass;

        public MigrationCommand(DatabaseConfiguration<T> databaseConfiguration, Class<T> klass) {
            super("migrate", "Starts the database migration. All pending migrations will be applied in order.");
            this.databaseConfiguration = databaseConfiguration;
            this.klass = klass;
        }

        @Override
        protected Class<T> getConfigurationClass() {
            return klass;
        }

        @Override
        protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
            log.info("MIGRATING DATABASE");
            DataSourceFactory dataSource = databaseConfiguration.getDataSourceFactory(configuration);
            migrate(dataSource);
        }

    }

    private static class RecreateFromScratchCommand<T extends Configuration> extends ConfiguredCommand<T> {

        private final DatabaseConfiguration<T> databaseConfiguration;
        private final Class<T> klass;

        public RecreateFromScratchCommand(DatabaseConfiguration<T> databaseConfiguration, Class<T> klass) {
            super("recreate", "Drops all objects (tables, views, procedures, triggers, ...) in the configured schemas.");
            this.databaseConfiguration = databaseConfiguration;
            this.klass = klass;
        }

        @Override
        protected Class<T> getConfigurationClass() {
            return klass;
        }

        @Override
        protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
            log.info("CLEANING DATABASE");
            DataSourceFactory dataSource = databaseConfiguration.getDataSourceFactory(configuration);

            recreateFromScratch(dataSource);
        }

    }
    
    private static class SeedCommand<T extends Configuration> extends ConfiguredCommand<T> {
        
        private final DatabaseConfiguration<T> databaseConfiguration;
        private final Class<T> klass;
        
        public SeedCommand(DatabaseConfiguration<T> databaseConfiguration, Class<T> klass) {
            super("seed", "Seed all objects (tables, views, procedures, triggers, ...) in the configured schemas.");
            this.databaseConfiguration = databaseConfiguration;
            this.klass = klass;
        }
        
        @Override
        protected Class<T> getConfigurationClass() {
            return klass;
        }
        
        @Override
        protected void run(Bootstrap<T> bootstrap, Namespace namespace, T configuration) throws Exception {
            log.info("SEEDING DATABASE WITH TEST DATA");
            DataSourceFactory dataSource = databaseConfiguration.getDataSourceFactory(configuration);
            seedWithTestData(dataSource);
        }

    }
    
    public static void migrate(DataSourceFactory dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource.getUrl(), dataSource.getUser(), dataSource.getPassword());

        MigrationInfoService info = flyway.info();

        if (info.applied().length == 0) {
            flyway.setInitDescription("base");
            flyway.setInitVersion("1.0.0");
            flyway.init();
        }
        flyway.migrate();
    }
    
    public static void seedWithTestData(DataSourceFactory dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource.getUrl(), dataSource.getUser(), dataSource.getPassword());
        flyway.setTable("schema_version_seeding");
        flyway.setLocations("db/seed");
        MigrationInfoService info = flyway.info();
        
        if (info.applied().length == 0) {
            flyway.setInitDescription("base");
            flyway.setInitOnMigrate(true);
        }
        flyway.migrate();
    }
    
    public static  void recreateFromScratch(DataSourceFactory dataSource) {
        Flyway flyway = new Flyway();
        flyway.setDataSource(dataSource.getUrl(), dataSource.getUser(), dataSource.getPassword());
        flyway.clean();
        flyway.migrate();
    }
}
