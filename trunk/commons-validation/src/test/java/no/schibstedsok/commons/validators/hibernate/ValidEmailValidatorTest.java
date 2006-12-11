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
        final ValidEmailValidator validator = new ValidEmailValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(""));
        assertFalse(validator.isValid("endre@no"));
        assertFalse(validator.isValid("endre@sesam.no  "));

        assertTrue(validator.isValid("k@rl-oskar.no"));
        assertTrue(validator.isValid("hang-b@online.no"));
        //assertTrue(validator.isValid("ola marius hoff sagli <ola@sesam.no>"));
        //assertTrue(validator.isValid("issues-return-103-jeff=infohazard.org@subetha.tigris.org"));        
        assertTrue(validator.isValid("_example@192.168.0.1"));                
        EmailValidator hibvalidator = new EmailValidator();
        hibvalidator.initialize(null);
        
        assertTrue(hibvalidator.isValid("ola@saseam.no"));
        assertTrue(hibvalidator.isValid("k@rl-oskar.no"));
        assertTrue(hibvalidator.isValid("hang-b@online.no"));
        assertTrue(hibvalidator.isValid("1@test.com"));
        assertTrue(hibvalidator.isValid("_example@test.com"));        
        assertTrue(hibvalidator.isValid("_example@192.168.0.1"));                
        
        
    }

}
