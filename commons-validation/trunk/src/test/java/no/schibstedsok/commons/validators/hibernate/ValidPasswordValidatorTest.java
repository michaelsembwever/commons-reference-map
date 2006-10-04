/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>ValidPasswordValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class ValidPasswordValidatorTest extends TestCase {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidPasswordValidatorTest.class);

    /** Default constructor. */
    public ValidPasswordValidatorTest() {
        super();
    }

    public void testIsValid() {
        final ValidPasswordValidator validator = new ValidPasswordValidator();

        assertFalse(validator.isValid(null));
        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("   Endre12   "));
        assertFalse(validator.isValid("EndreMeckelborg"));
        assertTrue(validator.isValid("12Endre34"));
        assertTrue(validator.isValid("endremeck."));
    }

}
