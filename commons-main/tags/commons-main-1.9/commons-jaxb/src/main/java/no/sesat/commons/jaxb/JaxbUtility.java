/*
 * Copyright (2009) Schibsted ASA
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
 */
package no.sesat.commons.jaxb;

import java.io.StringReader;
import java.io.StringWriter;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import org.apache.log4j.Logger;

/** Small and simple wrappers to typical jaxb operations.
 *
 * @version $Id$
 */
public final class JaxbUtility {

    // Constants -----------------------------------------------------

    private static final Logger LOG = Logger.getLogger(JaxbUtility.class);

    // Attributes ----------------------------------------------------
    // Static --------------------------------------------------------

    public static String marshal(final Object obj, final Class... classes) throws JAXBException {

        final StringWriter out = new StringWriter();

        JAXBContext.newInstance(classes).createMarshaller().marshal(obj, out);

        return out.toString();
    }

    public static <T extends Object> T unmarshal(final String xml, final Class<T> clazz) throws JAXBException{

        return (T)JAXBContext
                .newInstance(clazz)
                .createUnmarshaller()
                .unmarshal(new StringReader(xml));
    }

    // Constructors --------------------------------------------------

    private JaxbUtility(){}

    // Public --------------------------------------------------------
    // Package protected ---------------------------------------------
    // Protected -----------------------------------------------------
    // Private -------------------------------------------------------
    // Inner classes  ------------------------------------------------
}
