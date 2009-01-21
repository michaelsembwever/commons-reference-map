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

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test valid phone numbers.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @author <a href="mailto:ola@sesam.no">Ola Sagli</a>
 * @version <tt>$Revision: $</tt>
 */
public class PhoneValidatorTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(PhoneValidatorTest.class);

    /**
     * Test phone numbers with 5 digits.
     */
    public void testPhoneNO5() {
        LOG.debug("## testPhoneNO5 ##");
        final PhoneValidator validator = getValidator(PhoneType.PHONE_NO_5);

        assertTrue(validator.isValid("+4702345"));
        assertTrue(validator.isValid("004708776"));
        assertTrue(validator.isValid("04556"));
        assertTrue(validator.isValid("0 45 56"));

        assertFalse(validator.isValid("+4734232324"));
        assertFalse(validator.isValid("+478121"));
        assertFalse(validator.isValid("04788121"));
    }

    /**
     * Test phone numbers with 8 digits.
     */
    public void testPhoneNO8() {
        LOG.debug("## testPhoneNO8 ##");
        final PhoneValidator validator = getValidator(PhoneType.PHONE_NO_8);

        assertTrue(validator.isValid("+4798203016"));
        assertTrue(validator.isValid("004798203016"));
        assertTrue(validator.isValid("98203016"));
        assertTrue(validator.isValid("+4761171265"));
        assertTrue(validator.isValid("61171265"));
        assertTrue(validator.isValid("61 17 12 65"));

        assertFalse(validator.isValid("01171265"));
        assertFalse(validator.isValid("+4798121200000"));
        assertFalse(validator.isValid("+4798121"));
        assertFalse(validator.isValid("04798203016"));
    }

    /**
     * Test phone numbers.
     */
    public void testPhoneNO() {
        LOG.debug("## testPhoneNO ##");
        final PhoneValidator validator = getValidator(PhoneType.PHONE_NO);

        assertTrue(validator.isValid("+4798203016"));
        assertTrue(validator.isValid("004798203016"));
        assertTrue(validator.isValid("98203016"));
        assertTrue(validator.isValid("+4761171265"));
        assertTrue(validator.isValid("61171265"));

        assertFalse(validator.isValid("+4798121200000"));
        assertFalse(validator.isValid("+47981217"));

        assertTrue(validator.isValid("+4702345"));
        assertTrue(validator.isValid("004708776"));
        assertTrue(validator.isValid("04556"));

        assertFalse(validator.isValid("+47342323248"));
        assertFalse(validator.isValid("+478121"));
        assertFalse(validator.isValid("04788121"));
    }

    /**
     * Test mobile numbers.
     */
    public void testMobileNO() {
        LOG.debug("## testMobileNO ##");
        final PhoneValidator validator = getValidator(PhoneType.MOBILE_NO);

        assertTrue(validator.isValid("+4798203016"));
        assertTrue(validator.isValid("004798203016"));
        assertTrue(validator.isValid("98203016"));
        assertTrue(validator.isValid(" 982 03 016 "));

        assertFalse(validator.isValid("+4761171265"));
        assertFalse(validator.isValid("61171265"));
        assertFalse(validator.isValid("+4798121200000"));
        assertFalse(validator.isValid("+4798121"));
    }

    private PhoneValidator getValidator(final PhoneType phoneType) {
        final PhoneValidator validator = new PhoneValidator();

        validator.initialize(new Phone() {
            public String message() {
                return null;
            }

            public PhoneType phoneType() {
                return phoneType;
            }

            public boolean stripBlanks() {
                return true;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        return validator;
    }

}
