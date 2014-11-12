package uk.co.markberridge.users;

import io.dropwizard.Configuration;
import io.dropwizard.configuration.ConfigurationFactory;
import io.dropwizard.configuration.ConfigurationFactoryFactory;
import io.dropwizard.configuration.DefaultConfigurationFactoryFactory;
import io.dropwizard.jackson.Jackson;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.validation.Validation;

import org.junit.rules.ExternalResource;

import com.google.common.io.Resources;

public class ConfigurationRule<T extends Configuration> extends ExternalResource {

    private final ConfigurationFactoryFactory<T> cff = new DefaultConfigurationFactoryFactory<>();

    private final Class<T> configurationClass;
    private final String configurationFileName;

    private T configuration;

    public ConfigurationRule(Class<T> forConfigurationClass, String configurationFileName) {
        this.configurationClass = forConfigurationClass;
        this.configurationFileName = configurationFileName;
    }

    @Override
    public void before() throws Throwable {
        ConfigurationFactory<T> cf = cff.create(configurationClass, Validation.buildDefaultValidatorFactory()
                                                                              .getValidator(),
                Jackson.newObjectMapper(), "dw");

        this.configuration = cf.build(getConfigurationFile(configurationFileName));
    }

    @Override
    public void after() {
        configuration = null;
    }

    public T getConfiguration() {
        return configuration;
    }

    private static File getConfigurationFile(String configurationFileName) throws URISyntaxException {

        Path pathToRootOfClassLoader = Paths.get(Resources.getResource("banner.txt").toURI());
        Path pathToConfigFile = pathToRootOfClassLoader.getParent().getParent().getParent();

        OverrideConfig config = new OverrideConfig(pathToConfigFile.toString(), configurationFileName);

        return new File(config.getName());
    }

}
