package uk.co.markberridge.users.dao;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Date;

import org.hibernate.EmptyInterceptor;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AuditTrailInterceptor extends EmptyInterceptor {
    
    private static final Logger LOG = LoggerFactory.getLogger(AuditTrailInterceptor.class);

    @Override
    public boolean onFlushDirty(final Object entity, final Serializable id, final Object[] currentState,
                                final Object[] previousState, final String[] propertyNames, final Type[] types) {
        setValue(currentState, propertyNames, "updatedDate", new Date());
        return true;
    }

    @Override
    public boolean onSave(final Object entity, final Serializable id, final Object[] state,
                          final String[] propertyNames, final Type[] types) {
        setValue(state, propertyNames, "createdDate", new Date());
        return true;
    }

    private void setValue(final Object[] currentState, final String[] propertyNames, final String propertyToSet,
                          final Object value) {
        final int index = Arrays.asList(propertyNames).indexOf(propertyToSet);
        if (index >= 0) {
            LOG.trace("Setting property {}", propertyToSet);
            currentState[index] = value;
        } else {
            LOG.trace("Didn't find property {}", propertyToSet);
        }
    }
}
