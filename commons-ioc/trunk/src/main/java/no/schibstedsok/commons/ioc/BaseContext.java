/* Copyright (2005-2006) Schibsted Søk AS
 *   This file is part of SESAT.
 *
 *   SESAT is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Affero General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   SESAT is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Affero General Public License for more details.
 *
 *   You should have received a copy of the GNU Affero General Public License
 *   along with SESAT.  If not, see <http://www.gnu.org/licenses/>.
 *
 * BaseContext.java
 *
 * Created on 21 February 2006, 18:38
 */

package no.schibstedsok.commons.ioc;

/** The Base Context all contexts describing a class's contextual 'inversion-of-control' (dependencies) must extend.
 * This interface does not enforce any behaviour. It is only used so BasicInvocationHandler deals with contexts
 * as a known BaseContext rather than just as Objects. That is it provides some type-safety.
 *
 * @version $Id$
 * @author <a href="mailto:mick@wever.org">Michael Semb Wever</a>
 */
public interface BaseContext {

}
