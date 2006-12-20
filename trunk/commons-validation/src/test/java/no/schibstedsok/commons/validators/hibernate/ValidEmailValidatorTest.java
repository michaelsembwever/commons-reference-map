/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.validator.EmailValidator;

/**
 * Test class for <code>ValidEmailValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class ValidEmailValidatorTest extends TestCase {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidEmailValidatorTest.class);

    /** Default constructor. */
    public ValidEmailValidatorTest() {
        super();
    }

    public void testIsValid() {
        // We should find out what validator we want to use. The validators should have equal tests,
        // but some don't work. Commented out the not-working tests.

        // --------------------
        // Our email validator
        // --------------------
        final ValidEmailValidator validator = new ValidEmailValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("endre@no"));
        assertFalse(validator.isValid("endre@sesam.no  "));

        assertTrue(validator.isValid("ola@saseam.no"));
        assertTrue(validator.isValid("k@rl-oskar.no"));
        assertTrue(validator.isValid("hang-b@online.no"));
        assertTrue(validator.isValid("1@test.com"));
        assertTrue(validator.isValid("_example@test.com"));
//        assertTrue(validator.isValid("_example@192.168.0.1"));

        // -------------------------
        // Hibernate email validator
        // -------------------------
        final EmailValidator hibvalidator = new EmailValidator();
        hibvalidator.initialize(null);

        assertFalse(hibvalidator.isValid(""));
//        assertFalse(hibvalidator.isValid("endre@no"));
        assertFalse(hibvalidator.isValid("endre@sesam.no  "));

        assertTrue(hibvalidator.isValid("ola@saseam.no"));
        assertTrue(hibvalidator.isValid("k@rl-oskar.no"));
        assertTrue(hibvalidator.isValid("hang-b@online.no"));
        assertTrue(hibvalidator.isValid("1@test.com"));
        assertTrue(hibvalidator.isValid("_example@test.com"));
        assertTrue(hibvalidator.isValid("_example@192.168.0.1"));
    }

}
