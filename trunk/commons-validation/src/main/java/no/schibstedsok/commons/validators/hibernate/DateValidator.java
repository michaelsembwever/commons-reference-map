/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * {@inheritDoc}
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class DateValidator implements Validator<Date>, PropertyConstraint {

    // TODO: Uses default timezone. How should we handle timezones?

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(DateValidator.class);

    /** The date formatter used by this validator. */
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    /** minDate used to validate against if set. */
    private java.util.Date minDate = null;

    /** maxDate used to validate against if set. */
    private java.util.Date maxDate = null;

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final Date parameters) {
        final String minDateString = parameters.minDate();

        if (minDateString != null && minDateString.length() > 0) {
            try {
                minDate = dateFormat.parse(minDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal minDate for DateValidator.");
            }
        }

        final String maxDateString = parameters.maxDate();

        if (maxDateString != null && maxDateString.length() > 0) {
            try {
                maxDate = dateFormat.parse(maxDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal maxDate for DateValidator.");
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

        if (!(value instanceof Date)) {
            return false;
        }

        final java.util.Date date = (java.util.Date) value;

        if (minDate != null && date.before(minDate)) {
            return false;
        }
        if (maxDate != null && date.after(maxDate)) {
            return false;
        }
        return true;
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
