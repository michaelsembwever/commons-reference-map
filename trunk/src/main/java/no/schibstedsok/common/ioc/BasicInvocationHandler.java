/* Copyright (2005-2006) Schibsted Søk AS
 *
 * BasicInvocationHandler.java
 *
 * Created on 21 February 2006, 18:33
 *
 */

package no.schibstedsok.common.ioc;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

    private final Map<Method,Map<List<Class>,Method>> METHOD_CACHE = new HashMap<Method,Map<List<Class>,Method>>();
    private final Map<Method,Map<List<Class>,BaseContext>> CONTEXT_CACHE = new HashMap<Method,Map<List<Class>,BaseContext>>();
    
   // Attributes ----------------------------------------------------

    private final List<BaseContext> contexts;

   // Static --------------------------------------------------------
    
    private static final Logger LOG = Logger.getLogger(BasicInvocationHandler.class);
    
    private static final String ERR_METHOD_NOT_IN_INTERFACE = "Unable to proxy to the contexts associated to this BasicInvocationHandler for ";
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
    public BasicInvocationHandler(final BaseContext... cxts) {
        this(Arrays.asList(cxts));

    }

    /**
     * Creates a new instance of BasicInvocationHandler.
     */
    public BasicInvocationHandler(final List<? extends BaseContext> cxts) {
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
        final List<Class> paramSignature = new ArrayList<Class>();
        if( objArr != null ){
            for (Object obj : objArr) {
                paramSignature.add(obj == null ? null : obj.getClass() );
                paramsNotNull &= obj != null;
            }
        }
        
        // This is one of the applications performance hotspots.
        //  It is a benefit to keep a cache to remember what method to use.
        final Method cachedMethod = checkCache(method, paramSignature);
        if( cachedMethod != null ){
            LOG.trace(DEBUG_CACHE_USED_FOR + method);
            return invoke(cachedMethod, getContextFromCache(method,paramSignature), objArr);
        }

        // first pass is to find an exact signature match, we can skip if any of the params were null.
        if( paramsNotNull ){            
            try{ 
                return invokeExactSignature(object, method, paramSignature, objArr);

            }  catch (NoSuchMethodException ex) {
                // handled exception
                LOG.trace(ex);
            }
        }

        return invokeSubclassedSignature(object, method, paramSignature, objArr);

    }

   // Package protected ---------------------------------------------

   // Protected -----------------------------------------------------

   // Private -------------------------------------------------------
    
    /** Look for an exact signature method match to the argument objects passed in.
     **/
    private Object invokeExactSignature(
            final Object object,
            final Method method,
            final List<Class> paramSignature,
            final Object[] objArr) throws Throwable {

        for (BaseContext cxt : contexts) {
            
            final Class cls = cxt.getClass();
            try {

                final Method m = cls.getMethod(method.getName(), paramSignature.toArray(new Class[paramSignature.size()]));
                if (m != null) {
                    if(LOG.isTraceEnabled()){
                        LOG.trace(DEBUG_FOUND
                                + DEBUG_LOOKING_IN + cls.getName()
                                + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                    }

                    addToCache(method, paramSignature, m, cxt);
                    return invoke(m, cxt, objArr);

                }
            }  catch (NoSuchMethodException ex) {
                if(LOG.isTraceEnabled()){
                    LOG.trace(DEBUG_NOT_FOUND
                            + DEBUG_LOOKING_IN + cls.getName()
                            + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                }
            }
        }
        final NoSuchMethodException e = new NoSuchMethodException(ERR_METHOD_NOT_IN_INTERFACE + method.getName());
        LOG.error("",e);
        throw e;
    }
    
    /** Look for an signature matching any superclasses to the argument objects passed in.
     **/
    private Object invokeSubclassedSignature(
            final Object object,
            final Method method,
            final List<Class> paramSignature,
            final Object[] objArr) throws Throwable {
        
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
                            LOG.debug(DEBUG_FOUND
                                + DEBUG_LOOKING_IN + cls.getName()
                                + DEBUG_LOOKING_FOR + method.getName() + toString(paramSignature));
                        }
                        
                        addToCache(method, paramSignature, m, cxt);
                        return invoke(m, cxt, objArr);
                        
                    }else{
                        if(LOG.isTraceEnabled()){
                            LOG.trace(DEBUG_NOT_FOUND
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
    
    /** A method has been found in a BaseContext.
     * Invoke it.
     **/
    private Object invoke(
            final Method method, 
            final BaseContext cxt, 
            final Object[] objArr) 
                throws IllegalAccessException, InvocationTargetException{
        
        method.setAccessible(true);
        return method.invoke(cxt, objArr);
    }
    
    /** Check the cache incase this method has already been called once.
     ***/
    private Method checkCache(final Method method, final List<Class> paramSignature){
        
        Method cachedMethod = null;
        final Map<List<Class>,Method> map = METHOD_CACHE.get(method);
        if( map != null ){
            cachedMethod = map.get(paramSignature);
        }
        return cachedMethod;
    }
    
    /** Get from the cache the BaseContext the method comes from.
     **/
    private BaseContext getContextFromCache(final Method method, final List<Class> paramSignature){
        
        BaseContext cachedContext = null;
        final Map<List<Class>,BaseContext> map = CONTEXT_CACHE.get(method);
        if( map != null ){
            cachedContext = map.get(paramSignature);
        }
        return cachedContext;
    }
    
    /** Add to the cache the methodTo and contextTo to use for calls for methodFrom and paramSignature.
     ***/
    private void addToCache(
            final Method methodFrom, 
            final List<Class> paramSignature, 
            final Method methodTo,
            final BaseContext contextTo){
        
        LOG.trace(DEBUG_ADD_TO_CACHE + methodTo);
        
        Map<List<Class>,Method> methodMap = METHOD_CACHE.get(methodFrom);
        if( methodMap == null ){
            methodMap = new HashMap<List<Class>,Method>();
            METHOD_CACHE.put(methodFrom, methodMap);
        }
        methodMap.put(paramSignature, methodTo);
        
        Map<List<Class>,BaseContext> contextMap = CONTEXT_CACHE.get(methodFrom);
        if( contextMap == null ){
            contextMap = new HashMap<List<Class>,BaseContext>();
            CONTEXT_CACHE.put(methodFrom, contextMap);
        }
        contextMap.put(paramSignature, contextTo);
        
    }
    
    /** Get a string representation of paramSignature.
     **/
    private String toString(final List<Class> paramSignature){
        
        final StringBuilder sb = new StringBuilder();
        for (Class cls : paramSignature) {
            sb.append(cls == null ? "null" : cls.getSimpleName() + ", ");
        }
        return sb.toString();
    }

   // Inner classes -------------------------------------------------

}
