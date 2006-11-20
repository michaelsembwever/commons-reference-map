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
public class ValidUrlValidatorTest extends TestCase {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidUrlValidatorTest.class);

    /** Default constructor. */
    public ValidUrlValidatorTest() {
        super();
    }

    public void testIsValid() {
        final ValidUrlValidator validator = new ValidUrlValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("www.sesam.no"));
        assertFalse(validator.isValid("http://sesam.no  "));

        assertTrue(validator.isValid("http://sesam.no"));
        assertTrue(validator.isValid("https://sesam.no"));
        assertTrue(validator.isValid("http://www.it-stud.hiof.no/~oystebjo/"));
    }

}
