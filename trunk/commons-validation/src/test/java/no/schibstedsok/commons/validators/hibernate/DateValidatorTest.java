/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Annotation;
import java.util.Calendar;
import java.util.GregorianCalendar;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>PasswordValidator</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class DateValidatorTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(DateValidatorTest.class);

    /** Default constructor. */
    public DateValidatorTest() {
        super();
    }

    public void testIsValid() {
        final DateValidator validator = getTestDateValidator();
        assertTrue(validator.isValid(null));

        assertFalse(validator.isValid(getTestDate(1, 1, 1800)));
        assertFalse(validator.isValid(getTestDate(1, 1, 1990)));

        assertTrue(validator.isValid(getTestDate(1, 1, 1900)));
        assertTrue(validator.isValid(getTestDate(1, 1, 1945)));
    }

    private DateValidator getTestDateValidator() {
        final DateValidator validator = new DateValidator();

        validator.initialize(new Date() {
            public String message() {
                return null;
            }

            public String minDate() {
                return "01.01.1900";
            }

            public String maxDate() {
                return "31.12.1980";
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });

        return validator;
    }

    private java.util.Date getTestDate(final int day, final int month, final int year) {
        final Calendar calendar = new GregorianCalendar();
        calendar.set(Calendar.DAY_OF_MONTH, day);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
