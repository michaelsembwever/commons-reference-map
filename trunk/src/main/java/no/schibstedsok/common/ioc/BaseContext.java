/* Copyright (2005-2006) Schibsted Søk AS
 * BaseContext.java
 *
 * Created on 21 February 2006, 18:38
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package no.schibstedsok.common.ioc;

/** The Base Context all contexts describing a class's contextual 'inversion-of-control' (dependencies) must extend.
 * This interface does not enforce any behaviour. It is only used so BasicInvocationHandler deals with contexts
 * as a known BaseContext rather than just as Objects. That is it provides some type-safety.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
public interface BaseContext {

}
