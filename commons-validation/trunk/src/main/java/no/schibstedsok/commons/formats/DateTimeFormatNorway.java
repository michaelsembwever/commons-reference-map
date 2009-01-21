/*
 * Copyright (2005-2007) Schibsted Søk AS
 * This file is part of Sesat Commons.
 *
 *   Sesat Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sesat Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sesat Commons.  If not, see <http://www.gnu.org/licenses/>.
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
 * @deprecated Replaced by {@link SSDateFormat}
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@Deprecated
public class DateTimeFormatNorway extends SimpleDateFormat {

    /** Generated SerialUID. */
    private static final long serialVersionUID = 6935812679039801969L;

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(DateTimeFormatNorway.class);

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
