package uk.co.markberridge.users.dao;

import io.dropwizard.db.DataSourceFactory;

import org.hibernate.Session;
import org.hibernate.SessionException;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.ImprovedNamingStrategy;
import org.hibernate.dialect.MySQL5InnoDBDialect;
import org.hibernate.service.ServiceRegistry;
import org.junit.rules.ExternalResource;

import uk.co.markberridge.users.ConfigurationRule;
import uk.co.markberridge.users.UsersConfiguration;

public class HibernateRule extends ExternalResource {

//    private final ConfigurationRule<UsersConfiguration> configurationRule;
    //    private final Class<?>[] entities;
    //    private SessionFactory sessionFactory;
    //
    //    public HibernateRule(Class<?>... entities) {
    //        this.configurationRule = new ConfigurationRule<>(UsersConfiguration.class, "users.yml");
    //        this.entities = entities;
    //    }
    //
    //    @Override
    //    protected void before() throws Throwable {
    //        configurationRule.before();
    //
    //        DataSourceFactory factory = configurationRule.getConfiguration().getApplicationDataSourceFactory();
    //
    //        Configuration config = new Configuration();
    //        config.setProperty("hibernate.connection.url", factory.getUrl());
    //        config.setProperty("hibernate.connection.username", factory.getUser());
    //        config.setProperty("hibernate.connection.password", factory.getPassword());
    //        config.setProperty("hibernate.connection.driver_class", factory.getDriverClass());
    //        config.setProperty("hibernate.current_session_context_class", "thread");
    //        config.setProperty("hibernate.show_sql", "true");
    //        config.setNamingStrategy(ImprovedNamingStrategy.INSTANCE);
    //        config.setInterceptor(new AuditTrailInterceptor());
    //        config.setProperty("hibernate.dialect", MySQL5InnoDBDialect.class.getName());
    //
    //        for (int i = 0; i < entities.length; i++) {
    //            config.addAnnotatedClass(entities[i]);
    //        }
    //
    //        ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder().applySettings(config.getProperties())
    //                .build();
    //
    //        sessionFactory = config.buildSessionFactory(serviceRegistry);
    //    }
    //
    //    @Override
    //    protected void after() {
    //        closeSession();
    //        configurationRule.after();
    //    }
    //
    //    public SessionFactory getSessionFactory() {
    //        return sessionFactory;
    //    }
    //
    //    public Session getSession() {
    //        try {
    //            return sessionFactory.getCurrentSession();
    //        } catch (SessionException se) {
    //            return sessionFactory.openSession();
    //        }
    //    }
    //
    //    public void beginTransaction() {
    //        getSession().getTransaction().begin();
    //    }
    //
    //    public void commit() {
    //        getSession().getTransaction().commit();
    //    }
    //
    //    public void rollback() {
    //        getSession().getTransaction().rollback();
    //    }
    //
    //    public void closeSession() {
    //        getSession().close();
    //    }
    //
    //    public void commitAndCloseSession() {
    //        commit();
    //        getSession().close();
    //    }
}
