/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.formats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * Date formatter that handles Norwegian time zone and doesn't use
 * fuzzy logic that converts year 80 to 0080 etc.
 *
 * Uses the format: dd.MM.yyyy HH:mm
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class DateTimeFormatNorway extends SimpleDateFormat {

    /** Generated SerialUID. */
    private static final long serialVersionUID = 6935812679039801969L;

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(DateTimeFormatNorway.class);

    /** Default constructor. */
    public DateTimeFormatNorway() {
        super("dd.MM.yyyy HH:mm");
        setTimeZone(TimeZone.getTimeZone("Europe/Oslo"));
        setLenient(false);
    }

    /**
     * Overrides <code>parse</code> to do a stronger parsing that doesn't use fuzzy
     * hour logic.
     *
     * @param source the string to parse
     * @return the parsed date
     * @throws ParseException exception throws if the string is incorrect
     */
    public Date parse(final String source) throws ParseException {
        if (source == null) {
            throw new ParseException("Illegal date/time: null", 0);
        }

        final Date parsedDate = super.parse(source);

        if (!format(parsedDate).equals(source)) {
            throw new ParseException("Illegal date/time: " + source, 0);
        }
        return parsedDate;
    }

}
