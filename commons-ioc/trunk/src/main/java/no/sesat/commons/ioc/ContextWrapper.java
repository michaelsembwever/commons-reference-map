/* Copyright (2005-2006) Schibsted Søk AS
 *   This file is part of Sesat Commons.
 *
 *   Sesat Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sesat Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sesat Commons.  If not, see <http://www.gnu.org/licenses/>.
 *
 * ContextWrapper.java
 *
 * Created on 21 February 2006, 19:44
 *
 */

package no.sesat.commons.ioc;

import no.sesat.commons.reflect.ConcurrentProxy;


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
     * It will proxy all methods to the first applicable
     * method found in the list of Contexts.
     */
    @SuppressWarnings("unchecked")
    public static <T extends BaseContext> T wrap(
            final Class<T> context,
            final BaseContext... cxts) {

        final BasicInvocationHandler handler = new BasicInvocationHandler(cxts);
        assert handler.assertContextContract(context) : "Supplied contexts do not satisfy proxy's contract";
        return (T)ConcurrentProxy.newProxyInstance(context.getClassLoader(), new Class<?>[]{context}, handler);
    }

   // Z implementation ----------------------------------------------

   // Y overrides ---------------------------------------------------

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

   // Inner classes -------------------------------------------------

}
