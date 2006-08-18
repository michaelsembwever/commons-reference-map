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
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class DateFormatNorway extends SimpleDateFormat {

    /** Generated SerialUID. */
    private static final long serialVersionUID = 8452622732509229434L;

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(DateFormatNorway.class);

    /** Default constructor. */
    public DateFormatNorway() {
        super("dd.MM.yyyy");
        setTimeZone(TimeZone.getTimeZone("Europe/Oslo"));
        setLenient(false);
    }

    /**
     * Overrides <code>parse</code> to do a stronger parsing that doesn't use fuzzy
     * year logic.
     *
     * @param source the string to parse
     * @return the parsed date
     * @throws ParseException exception throws if the string is incorrect
     */
    public Date parse(final String source) throws ParseException {
        if (source == null) {
            throw new ParseException("Illegal date: null", 0);
        }

        final Date parsedDate = super.parse(source);

        if (!format(parsedDate).equals(source)) {
            throw new ParseException("Illegal date: " + source, 0);
        }
        return parsedDate;
    }

}
