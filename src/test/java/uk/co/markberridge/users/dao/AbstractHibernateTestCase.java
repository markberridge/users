package uk.co.markberridge.users.dao;

import org.junit.Before;

import uk.co.markberridge.users.DbRecreate;

public abstract class AbstractHibernateTestCase {

    private static boolean initialised = false;

    @Before
    public void setUp() {
        initialise();
    }

    public static void reset() {
        initialised = false;
    }

    protected boolean initialise() {
        if (!initialised) {
            try {
                DbRecreate.main();
            } catch (Exception e) {
                throw new AssertionError("Failed to recreate database schema", e);
            }

            initialised = true;
            return true;
        }
        return false;
    }
}
