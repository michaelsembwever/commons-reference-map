/*
 * Copyright (2005-2007) Schibsted Søk AS
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
