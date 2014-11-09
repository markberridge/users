package uk.co.markberridge.users.provider;

import java.lang.reflect.Type;

import javax.ws.rs.core.Context;

import com.sun.jersey.spi.inject.SingletonTypeInjectableProvider;

/**
 * Provider which injects the singleton of class T from the context.
 * 
 * @param <T>
 */
public class ContextInjectableProvider<T> extends SingletonTypeInjectableProvider<Context, T> {

    public ContextInjectableProvider(Type type, T instance) {
        super(type, instance);
    }

    /**
     * a null instance is passed to the SingletonTypeInjectableProvider. This somehow(?) works by the environment
     * creating its own singleton and the provider is able to access it
     * 
     * @param type
     */
    public ContextInjectableProvider(Type type) {
        super(type, null);
    }
}
