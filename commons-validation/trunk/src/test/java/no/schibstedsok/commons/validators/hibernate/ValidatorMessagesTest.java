/*
 * Copyright (2005-2007) Schibsted Søk AS
 * This file is part of Sesat Commons.
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
package no.schibstedsok.commons.validators.hibernate;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * Test validation messages.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @author <a href="mailto:ola@sesam.no">Ola Sagli</a>
 * @version <tt>$Revision: $</tt>
 */
public class ValidatorMessagesTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(ValidatorMessagesTest.class);

    /**
     * Test with locale "no".
     */
    public void testValidation() {
        internalTestValidation("Norsk: ", new Locale("no"));
    }

    /**
     * Fetch resources
     * @param lang
     */
    private void internalTestValidation(final String prefixLog, final Locale locale) {
        Locale.setDefault(locale);
        final MockPerson person = new MockPerson("611712657");

        final ClassValidator personValidator = new ClassValidator(MockPerson.class);
        final InvalidValue[] values = personValidator.getInvalidValues(person);

        assertEquals(1, values.length);

        for (InvalidValue value : values) {
            LOG.info("PropertyPath: " + value.getRootBean());
            LOG.info(prefixLog + value);
        }
    }

}
