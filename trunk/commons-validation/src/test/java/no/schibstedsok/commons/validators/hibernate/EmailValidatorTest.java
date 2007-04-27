/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Annotation;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>EmailValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class EmailValidatorTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(EmailValidatorTest.class);

    /** Default constructor. */
    public EmailValidatorTest() {
        super();
    }

    public void testIsValid() {
        final EmailValidator validator = getValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("endre@"));
        assertFalse(validator.isValid("@sesam.no"));

        assertTrue(validator.isValid("endre@sesam.no"));
        assertTrue(validator.isValid("  endre@sesam.no  "));
    }

    private EmailValidator getValidator() {
        final EmailValidator validator = new EmailValidator();

        validator.initialize(new Email() {
            public String message() {
                return null;
            }

            public boolean trimValue() {
                return true;
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        return validator;
    }

}
