/* Copyright (2005-2007) Schibsted Søk AS
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
 * BasicInvocationHandler.java
 *
 * Created on 21 February 2006, 18:33
 *
 */

package no.sesat.commons.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.log4j.Logger;

/** InvocationHandler implementation to be used by ContextWrapper.
 * Constructed with a list of BaseContexts that are used to proxy to.
 * This is a basic implementation that proxy's between identical method signatures.
 * The order in the BaseContext list is important as the first method with the correct signature is used.
 * <br/>
 * 
 * Serialisation depends on whether all supplied contexts are themselves serialisable.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
final class BasicInvocationHandler implements InvocationHandler, java.io.Serializable {

    private final transient Map<Method,Map<List<Class<?>>,Method>> methodCache
            = new HashMap<Method,Map<List<Class<?>>,Method>>();
    private final transient Map<Method,Map<List<Class<?>>,BaseContext>> contextCache
            = new HashMap<Method,Map<List<Class<?>>,BaseContext>>();

    /** threading lock to the cache maps since they are not synchronised, 
     * and it's overkill to make them Hashtables. **/
    private final transient ReentrantReadWriteLock cacheGate = new ReentrantReadWriteLock();

   // Attributes ----------------------------------------------------

    private final List<BaseContext> contexts;

   // Static --------------------------------------------------------

    private static final Invoker FAIR_DINKUM = new FairDinkumInvoker();
    private static final Invoker CACTUS = new CactusInvoker();

    private static final Logger LOG = Logger.getLogger(BasicInvocationHandler.class);

    private static final String ERR_METHOD_NOT_IN_INTERFACE
            = "Unable to proxy to the contexts associated to this BasicInvocationHandler for ";
    private static final String ERR_METHOD_NOT_IN_EXACT_INTERFACE
            = "No exact signature to the contexts associated to this BasicInvocationHandler for ";
    private static final String DEBUG_LOOKING_FOR = " Looking for ";
    private static final String DEBUG_LOOKING_IN = "Looking in ";
    private static final String DEBUG_FOUND = "Found method while: ";
    private static final String DEBUG_NOT_FOUND = "Did not found method while: ";
    private static final String DEBUG_ADD_TO_CACHE = "Adding to cache ";
    private static final String DEBUG_CACHE_USED_FOR = "Using cache for ";

   // Constructors --------------------------------------------------

    /**
     * Creates a new instance of BasicInvocationHandler.
     */
    BasicInvocationHandler(final BaseContext... cxts) {
        this(Arrays.asList(cxts));

    }

    /**
     * Creates a new instance of BasicInvocationHandler.
     */
    BasicInvocationHandler(final List<? extends BaseContext> cxts) {
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

        boolean paramsNotNull = true;
        // construct method's parameter signature
        final List<Class<?>> paramSignature = new ArrayList<Class<?>>();
        if(objArr != null){
            for (Object obj : objArr) {
                paramSignature.add(obj == null ? null : obj.getClass());
                paramsNotNull &= obj != null;
            }
        }

        // This is one of the applications performance hotspots.
        //  It is a benefit to keep a cache to remember what method to use.
        final Method cachedMethod = checkCache(method, paramSignature);
        if(cachedMethod != null){
            LOG.trace( DEBUG_CACHE_USED_FOR + method);
            return FAIR_DINKUM.invoke(cachedMethod, getContextFromCache(method,paramSignature), objArr);
        }

        // first pass is to find an exact signature match, we can skip if any of the params were null.
        if(paramsNotNull){
            try{
                return invokeExactSignature(FAIR_DINKUM, object, method, paramSignature, objArr);

            }  catch (NoSuchMethodException ex) {
                // handled exception
                LOG.trace( ex);
            }
        }

        return invokeSubclassedSignature(FAIR_DINKUM, object, method, paramSignature, objArr);

    }

   // Package protected ---------------------------------------------

    boolean assertContextContract(final Class<? extends BaseContext> cxtClass){

        try{
            for( Method m : cxtClass.getMethods() ){
                invokeExactSignature(CACTUS, null, m, Arrays.asList(m.getParameterTypes()), null);
            }
        }catch(NoSuchMethodException nsme){
            LOG.error(nsme.getMessage(), nsme);
            return false;
        }catch(IllegalAccessException iae){
            LOG.error(iae.getMessage(), iae);
            return false;
        }catch(InvocationTargetException ite){
            LOG.error(ite.getMessage(), ite);
            return false;
        }
        return true;
    }

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------

    /** Look for an exact signature method match to the argument objects passed in.
     **/
    private Object invokeExactSignature(
            final Invoker invoker,
            final Object object,
            final Method method,
            final List<Class<?>> paramSignature,
            final Object[] objArr) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException{

        for (BaseContext cxt : contexts) {

            final Class cls = cxt.getClass();
            try {

                final Method m = cls.getMethod(method.getName(), paramSignature.toArray(new Class[paramSignature.size()]));
                if (m != null) {
                    if(LOG.isTraceEnabled()){
                        LOG.trace( DEBUG_FOUND
                                + DEBUG_LOOKING_IN + cls.getName()
                                + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                    }

                    addToCache(method, paramSignature, m, cxt);
                    return invoker.invoke(m, cxt, objArr);

                }
            }  catch (NoSuchMethodException ex) {
                if(LOG.isTraceEnabled()){
                    LOG.trace( DEBUG_NOT_FOUND
                            + DEBUG_LOOKING_IN + cls.getName()
                            + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                }
            }
        }
        throw new NoSuchMethodException(ERR_METHOD_NOT_IN_EXACT_INTERFACE + method.getName());
    }

    /** Look for an signature matching any superclasses to the argument objects passed in.
     **/
    private Object invokeSubclassedSignature(
            final Invoker invoker,
            final Object object,
            final Method method,
            final List<Class<?>> paramSignature,
            final Object[] objArr) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {

        for (BaseContext cxt : contexts) {

            final Class cls = cxt.getClass();
            for (Method m : cls.getMethods()) {

                if (m.getName().equals(method.getName())) {

                    final Class[] cArr = m.getParameterTypes();
                    boolean assignableFrom = true;
                    for (int k = 0; assignableFrom && k < cArr.length; ++k) {
                        assignableFrom = paramSignature.get(k) == null || cArr[k].isAssignableFrom(paramSignature.get(k));
                    }
                    if (assignableFrom) {
                        if(LOG.isTraceEnabled()){
                            LOG.trace( DEBUG_FOUND
                                + DEBUG_LOOKING_IN + cls.getName()
                                + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                        }

                        addToCache(method, paramSignature, m, cxt);
                        return invoker.invoke(m, cxt, objArr);

                    }else{
                        if(LOG.isTraceEnabled()){
                            LOG.trace( DEBUG_NOT_FOUND
                                + DEBUG_LOOKING_IN + cls.getName()
                                + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                        }
                    }
                }
            }
        }
        final NoSuchMethodException e = new NoSuchMethodException(ERR_METHOD_NOT_IN_INTERFACE + method.getName());
        LOG.error("",e);
        throw e;
    }



    /** Check the cache incase this method has already been called once.
     ***/
    private Method checkCache(final Method method, final List<Class<?>> paramSignature){

        try{
            cacheGate.readLock().lock();

            Method cachedMethod = null;
            final Map<List<Class<?>>,Method> map = methodCache.get(method);
            if(map != null){
                cachedMethod = map.get(paramSignature);
            }
            return cachedMethod;

        }finally{
            cacheGate.readLock().unlock();
        }
    }

    /** Get from the cache the BaseContext the method comes from.
     * Presumed that checkCache(..) has been called and was successfull.
     **/
    private BaseContext getContextFromCache(final Method method, final List<Class<?>> paramSignature){

        try{
            cacheGate.readLock().lock();

            final Map<List<Class<?>>,BaseContext> map = contextCache.get(method);
            assert map != null;

            final BaseContext cachedContext = map.get(paramSignature);
            assert cachedContext != null;

            return cachedContext;

        }finally{
            cacheGate.readLock().unlock();
        }
    }

    /** Add to the cache the methodTo and contextTo to use for calls for methodFrom and paramSignature.
     ***/
    private void addToCache(
            final Method methodFrom,
            final List<Class<?>> paramSignature,
            final Method methodTo,
            final BaseContext contextTo){

        LOG.trace( DEBUG_ADD_TO_CACHE + methodTo);

        try{
            cacheGate.writeLock().lock();

            Map<List<Class<?>>,Method> methodMap = methodCache.get(methodFrom);
            if(methodMap == null){
                methodMap = new HashMap<List<Class<?>>,Method>();
                methodCache.put(methodFrom, methodMap);
            }
            methodMap.put(paramSignature, methodTo);

            Map<List<Class<?>>,BaseContext> contextMap = contextCache.get(methodFrom);
            if(contextMap == null){
                contextMap = new HashMap<List<Class<?>>,BaseContext>();
                contextCache.put(methodFrom, contextMap);
            }
            contextMap.put(paramSignature, contextTo);

        }finally{
            cacheGate.writeLock().unlock();
        }
    }

    /** Get a string representation of paramSignature.
     **/
    private String toString(final List<Class<?>> paramSignature){

        final StringBuilder sb = new StringBuilder();
        for (Class cls : paramSignature) {
            sb.append(cls == null ? "null" : cls.getSimpleName() + ", ");
        }
        return sb.toString();
    }

   // Inner classes -------------------------------------------------

    private static interface Invoker{
        /** A method has been found in a BaseContext.
         * Invoke it.
         **/
        Object invoke(final Method method, final BaseContext cxt, final Object[] objArr)
                throws IllegalAccessException, InvocationTargetException;
    }

    private static class FairDinkumInvoker implements Invoker{
        public Object invoke(
                final Method method,
                final BaseContext cxt,
                final Object[] objArr)
                    throws IllegalAccessException, InvocationTargetException{

            method.setAccessible(true);
            return method.invoke(cxt, objArr);
        }
    }

    private static class CactusInvoker implements Invoker{
        public Object invoke(
                final Method method,
                final BaseContext cxt,
                final Object[] objArr)
                    throws IllegalAccessException, InvocationTargetException{

            return null;
        }
    }
}
