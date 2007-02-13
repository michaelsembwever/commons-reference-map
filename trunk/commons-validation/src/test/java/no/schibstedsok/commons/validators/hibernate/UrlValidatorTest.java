/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>UrlValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class UrlValidatorTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(UrlValidatorTest.class);

    /** Default constructor. */
    public UrlValidatorTest() {
        super();
    }

    public void testIsValid() {
        final UrlValidator validator = new UrlValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("www.sesam.no"));
        assertFalse(validator.isValid("http://sesam.no  "));

        assertTrue(validator.isValid("http://sesam.no"));
        assertTrue(validator.isValid("https://sesam.no"));
        assertTrue(validator.isValid("http://www.it-stud.hiof.no/~oystebjo/"));
    }

}
