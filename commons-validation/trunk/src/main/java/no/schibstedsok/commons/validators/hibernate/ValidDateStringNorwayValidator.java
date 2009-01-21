/*
 * Copyright (2005-2006) Schibsted Søk AS
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
package no.schibstedsok.commons.validators.hibernate;

import java.text.ParseException;
import java.util.Date;

import no.schibstedsok.commons.formats.DateFormatNorway;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * {@inheritDoc}
 *
 * @deprecated Replaced by {@link DateValidator}, convert to use dates.
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@Deprecated
public class ValidDateStringNorwayValidator implements Validator<ValidDateStringNorway>, PropertyConstraint {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidDateStringNorwayValidator.class);

    /** The date formatter used by this validator. */
    private DateFormatNorway dateFormat = new DateFormatNorway();

    /** minDate used to validate against if set. */
    private Date minDate = null;

    /** maxDate used to validate against if set. */
    private Date maxDate = null;

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final ValidDateStringNorway parameters) {
        final String minDateString = parameters.minDate();

        if (minDateString != null && minDateString.length() > 0) {
            try {
                minDate = dateFormat.parse(minDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal minDate for ValidDateStringNorwayValidator.");
            }
        }

        final String maxDateString = parameters.maxDate();

        if (maxDateString != null && maxDateString.length() > 0) {
            try {
                maxDate = dateFormat.parse(maxDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal maxDate for ValidDateStringNorwayValidator.");
            }
        }
    }

    /**
     * The validate method.
     *
     * @param value the value to validate
     * @return the validation result
     */
    public boolean isValid(final Object value) {
        // Null is valid. Use @NotNull to prevent this in the validation.
        if (value == null) {
            return true;
        }

        if (!(value instanceof String)) {
            return false;
        }

        try {
            final Date parsedDate = dateFormat.parse((String) value);

            if (parsedDate == null) {
                return false;
            }
            if (minDate != null && parsedDate.before(minDate)) {
                return false;
            }
            if (maxDate != null && parsedDate.after(maxDate)) {
                return false;
            }
            return true;
        } catch (ParseException e) {
            return false;
        }
    }

    /**
     * Method that is invoked when the validator is applied to an attribute.
     *
     * @param property the property
     */
    public void apply(final Property property) {
        // Does nothing...
    }

}
