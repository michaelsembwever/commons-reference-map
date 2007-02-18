/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.formats;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.log4j.Logger;

/**
 * Date formatter that doesn't use fuzzy logic that converts input, ie
 * year 80 to 0080.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class SSDateFormat extends SimpleDateFormat {

    /** Generated SerialUID. */
    private static final long serialVersionUID = -5035067156517552315L;

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(SSDateFormat.class);

    /**
     * Default constructor should not be used.
     */
    private SSDateFormat() {
        super();
    }

    /**
     * Constructor that uses default time zone.
     *
     * @param pattern the pattern to use for the date format
     */
    public SSDateFormat(final String pattern) {
        super(pattern);
        setTimeZone(TimeZone.getDefault());
        setLenient(false);
    }

    /**
     * Constructor where you set the timezone to use.
     *
     * @param pattern the pattern to use for the date format
     * @param timeZone the time zone to use
     */
    public SSDateFormat(final String pattern, final TimeZone timeZone) {
        super(pattern);
        setTimeZone(timeZone);
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
            return null;
        }

        final Date parsedDate = super.parse(source);

        if (!format(parsedDate).equals(source)) {
            throw new ParseException("Illegal date/time: " + source, 0);
        }
        return parsedDate;
    }

}
