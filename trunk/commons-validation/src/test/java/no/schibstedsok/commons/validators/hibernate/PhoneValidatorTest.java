/*
 * Copyright (2005-2007) Schibsted Søk AS
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
     * Test phone numbers.
     */
    public void testPhoneNO() {
        final PhoneValidator validator = new PhoneValidator();

        validator.initialize(new Phone() {
            public String message() {
                return null;
            }

            public PhoneType phoneType() {
                return PhoneType.PHONE_NO;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        assertTrue(validator.isValid("+4798203016"));
        assertTrue(validator.isValid("98203016"));
        assertTrue(validator.isValid("+4761171265"));
        assertTrue(validator.isValid("61171265"));

        assertFalse(validator.isValid("+4798121200000"));
        assertFalse(validator.isValid("+4798121"));
    }

    /**
     * Test mobile numbers.
     */
    public void testMobileNO() {
        final PhoneValidator validator = new PhoneValidator();

        validator.initialize(new Phone() {
            public String message() {
                return null;
            }

            public PhoneType phoneType() {
                return PhoneType.MOBILE_NO;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        assertTrue(validator.isValid("+4798203016"));
        assertTrue(validator.isValid("98203016"));

        assertFalse(validator.isValid("+4761171265"));
        assertFalse(validator.isValid("61171265"));
        assertFalse(validator.isValid("+4798121200000"));
        assertFalse(validator.isValid("+4798121"));
    }

}
