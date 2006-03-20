/* Copyright (2005-2006) Schibsted Søk AS
 *
 * ContextWrapper.java
 *
 * Created on 21 February 2006, 19:44
 *
 */

package no.schibstedsok.common.ioc;

import java.lang.reflect.Proxy;

/** Utility class to create Proxy wrappers to a single defined context class from a list of context subclasses.
 *  Will fail if the list of contexts does not provide the complete implementation to the required proxy'd context.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
public final class ContextWrapper {

    // Attributes ----------------------------------------------------

    // Static --------------------------------------------------------

    // Constructors --------------------------------------------------

    /**
     * Creates a new instance of ContextWrapper.
     */
    private ContextWrapper() {
    }

    // Public --------------------------------------------------------

    /** Returns a proxy class implementing the context.
     * It will proxy all methods to the first identical
     * method found in the list of Contexts.
     */
    public static <T extends BaseContext> T wrap(
            final Class<T> context,
            final BaseContext... cxts) {

        final BasicInvocationHandler handler = new BasicInvocationHandler(cxts);
        return (T)Proxy.newProxyInstance(context.getClassLoader(), new Class[]{context}, handler);
    }

   // Z implementation ----------------------------------------------

   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------

}
