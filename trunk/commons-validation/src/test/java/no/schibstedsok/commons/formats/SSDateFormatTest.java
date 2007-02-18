/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.formats;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

/**
 * Test class for <code>SSDateFormat</code>.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class SSDateFormatTest extends TestCase {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(SSDateFormatTest.class);

    /** Formatter used in tests. */
    private SSDateFormat dateFormat = new SSDateFormat("dd.MM.yyyy");

    /** Default constructor. */
    public SSDateFormatTest() {
        super();
    }

    private GregorianCalendar getCalendar() {
        final Locale localeNorway = new Locale("no", "NO");
        final TimeZone timeZoneNorway = TimeZone.getTimeZone("Europe/Oslo");
        return new GregorianCalendar(timeZoneNorway, localeNorway);
    }

    private Date getTestDate() {
        // Date: 08.06.2006
        final GregorianCalendar cal = getCalendar();
        cal.set(Calendar.YEAR, 2006);
        cal.set(Calendar.MONTH, 5);
        cal.set(Calendar.DAY_OF_MONTH, 8);
        return cal.getTime();
    }

    public void testFormat() {
        final String result = dateFormat.format(getTestDate());
        assertNotNull(result);
        assertEquals("08.06.2006", result);
    }

    public void testParseNull() {
        try {
            final Date result = dateFormat.parse(null);
            assertNull(result);
        } catch (ParseException e) {
            fail();
        }
    }

    public void testParseInvalid() {
        // Test fuzzy year handling that converts year 80 to 0080.
        try {
            dateFormat.parse("01.01.80");
            fail();
        } catch (ParseException e) {
            // This should happen...
        }

        // Test fuzzy date handling that converts i.e. 30/2 to 2/3.
        try {
            dateFormat.parse("30.02.1980");
            fail();
        } catch (ParseException e) {
            // This should happen...
        }
    }

    public void testParseValid() {
        final GregorianCalendar testCalendar = getCalendar();
        testCalendar.setTime(getTestDate());
        final GregorianCalendar resultCalendar = getCalendar();

        try {
            resultCalendar.setTime(dateFormat.parse("08.06.2006"));
        } catch (ParseException e) {
            fail();
        }

        assertEquals(testCalendar.get(Calendar.YEAR), resultCalendar.get(Calendar.YEAR));
        assertEquals(testCalendar.get(Calendar.MONTH), resultCalendar.get(Calendar.MONTH));
        assertEquals(testCalendar.get(Calendar.DAY_OF_MONTH), resultCalendar.get(Calendar.DAY_OF_MONTH));
    }

}
