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

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>PasswordValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class PasswordValidatorTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(PasswordValidatorTest.class);

    /** Default constructor. */
    public PasswordValidatorTest() {
        super();
    }

    public void testIsValid() {
        final PasswordValidator validator = new PasswordValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("passord"));
        assertFalse(validator.isValid("pass123"));

        assertTrue(validator.isValid("passord123"));
        assertTrue(validator.isValid("*45hello98"));
    }

}
