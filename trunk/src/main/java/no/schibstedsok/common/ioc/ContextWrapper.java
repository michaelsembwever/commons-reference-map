/* Copyright (2005-2006) Schibsted Søk AS
 *
 * ContextWrapper.java
 *
 * Created on 21 February 2006, 19:44
 *
 */

package no.schibstedsok.common.ioc;

import java.lang.reflect.Proxy;

/**
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
public final class ContextWrapper {

    /**
     * Creates a new instance of ContextWrapper
     */
    private ContextWrapper() {
    }

    public static BaseContext wrap(
            final Class/*<? extends BaseContext>*/ context,
            final BaseContext[] cxts) {

        final BasicInvocationHandler handler = new BasicInvocationHandler(cxts);
        return (BaseContext) Proxy.newProxyInstance(context.getClassLoader(), new Class[]{context}, handler);
    }

}
