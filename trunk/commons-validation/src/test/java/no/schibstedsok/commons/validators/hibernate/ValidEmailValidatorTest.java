/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

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
        final ValidEmailValidator validator = new ValidEmailValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("endre@no"));
        assertFalse(validator.isValid("endre@sesam.no  "));

        assertTrue(validator.isValid("k@rl-oskar.no"));
        assertTrue(validator.isValid("hang-b@online.no"));
    }

}
