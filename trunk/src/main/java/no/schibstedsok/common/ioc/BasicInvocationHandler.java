/* Copyright (2005-2006) Schibsted Søk AS
 *
 * BasicInvocationHandler.java
 *
 * Created on 21 February 2006, 18:33
 *
 */

package no.schibstedsok.common.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import org.apache.log4j.Logger;

/** InvocationHandler implementation to be used by ContextWrapper.
 * Constructed with a list of BaseContexts that are used to proxy to.
 * This is a basic implementation that proxy's between identical method signatures.
 * The order in the BaseContext list is important as the first method with the correct signature is used.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
final class BasicInvocationHandler implements InvocationHandler {


   // Attributes ----------------------------------------------------

    private static final Logger LOG = Logger.getLogger(BasicInvocationHandler.class);

    private final List/*<BaseContext>*/ contexts;

    private static final String ERR_METHOD_NOT_IN_INTERFACE = "Unable to proxy to the contexts associated to this BasicInvocationHandler for ";
    private static final String DEBUG_LOOKING_FOR = " Looking for ";
    private static final String DEBUG_LOOKING_IN = "Looking in ";
    private static final String DEBUG_FOUND = "Found method while: ";
    private static final String DEBUG_NOT_FOUND = "Did not found method while: ";

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

    /**
     * Creates a new instance of BasicInvocationHandler.
     */
    public BasicInvocationHandler(final BaseContext[] cxts) {
        this(Arrays.asList(cxts));

    }

    /**
     * Creates a new instance of BasicInvocationHandler.
     */
    public BasicInvocationHandler(final List/*<? extends BaseContext>*/ cxts) {
        contexts = Collections.unmodifiableList(cxts);

    }

   // Public --------------------------------------------------------

   // InvocationHandler implementation ----------------------------------------------


    /** {@inheritDoc}
     */
    public Object invoke(
            final Object object,
            final Method method,
            final Object[] objArr) throws Throwable {

        // construct method's parameter signature
        final Class[] paramSignature = new Class[objArr == null ? 0 : objArr.length];
        final StringBuffer sb = new StringBuffer();
        for (int i = 0; i < paramSignature.length; ++i) {
            paramSignature[i] = objArr[i].getClass();
            sb.append("\n" + objArr[i].getClass().getName());
        }

        // first pass is to find an exact signature match
        Iterator it = contexts.iterator();
        while (it.hasNext()) {
            final BaseContext cxt = (BaseContext) it.next();


            try  {

                final Method m = cxt.getClass().getMethod(method.getName(), paramSignature);
                if (m != null) {
                    LOG.debug(DEBUG_FOUND
                            + DEBUG_LOOKING_IN + cxt.getClass().getName()
                            + DEBUG_LOOKING_FOR + method.getName() + sb);
                    try  {
                        m.setAccessible(true);
                        return m.invoke(cxt, objArr);
                    }  finally  {
                        m.setAccessible(false);
                    }
                }
            }  catch (NoSuchMethodException ex) {
                LOG.debug(DEBUG_NOT_FOUND
                        + DEBUG_LOOKING_IN + cxt.getClass().getName()
                        + DEBUG_LOOKING_FOR + method.getName() + sb);
            }
        }

        // second pass is look for superclasses to the parameter types
        it = contexts.iterator();
        while (it.hasNext()) {
            final BaseContext cxt = (BaseContext) it.next();

            LOG.debug(DEBUG_LOOKING_IN + cxt.getClass().getName() + DEBUG_LOOKING_FOR + method.getName() + sb);

            final Method[] methods = cxt.getClass().getMethods();
            for (int j = 0; j < methods.length; ++j) {

                final Method m = methods[j];
                if (m.getName().equals(method.getName())) {

                    final Class[] cArr = m.getParameterTypes();
                    boolean assignableFrom = true;
                    for (int k = 0; assignableFrom && k < cArr.length; ++k) {
                        assignableFrom = cArr[k].isAssignableFrom(paramSignature[k]);
                    }
                    if (assignableFrom) {
                        LOG.debug(DEBUG_FOUND);
                        try  {
                            m.setAccessible(true);
                            return m.invoke(cxt, objArr);
                        }  finally  {
                            m.setAccessible(false);
                        }
                    }
                }
            }
        }

        final String errMsg = ERR_METHOD_NOT_IN_INTERFACE + method.getName();
        LOG.error(errMsg);
        throw new NoSuchMethodException(errMsg);
    }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------

}
