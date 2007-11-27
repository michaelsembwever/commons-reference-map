/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

/**
 * Test validation messages.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @author <a href="mailto:ola@sesam.no">Ola Sagli</a>
 * @version <tt>$Revision: $</tt>
 */
public class ValidatorMessagesTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(ValidatorMessagesTest.class);

    /**
     * Test with locale "no".
     */
    public void testValidation() {
        internalTestValidation("Norsk: ", new Locale("no"));
    }

    /**
     * Fetch resources
     * @param lang
     */
    private void internalTestValidation(final String prefixLog, final Locale locale) {
        Locale.setDefault(locale);
        final MockPerson person = new MockPerson("611712657");

        final ClassValidator personValidator = new ClassValidator(MockPerson.class);
        final InvalidValue[] values = personValidator.getInvalidValues(person);

        assertEquals(1, values.length);

        for (InvalidValue value : values) {
            LOG.info("PropertyPath: " + value.getRootBean());
            LOG.info(prefixLog + value);
        }
    }

}
