/*
 * Copyright (2005-2006) Schibsted Søk AS
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
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public class ValidDateNorwayValidator implements Validator<ValidDateNorway>, PropertyConstraint {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidDateNorwayValidator.class);

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
    public void initialize(final ValidDateNorway parameters) {
        final String minDateString = parameters.minDate();

        if (minDateString != null && minDateString.length() > 0) {
            try {
                minDate = dateFormat.parse(minDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal minDate for ValidDateNorwayValidator.");
            }
        }

        final String maxDateString = parameters.maxDate();

        if (maxDateString != null && maxDateString.length() > 0) {
            try {
                maxDate = dateFormat.parse(maxDateString);
            } catch (ParseException e) {
                throw new RuntimeException("Illegal maxDate for ValidDateNorwayValidator.");
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
        if (value == null || !(value instanceof Date)) {
            return false;
        }

        final Date date = (Date) value;

        if (minDate != null && date.before(minDate)) {
            return false;
        }
        if (maxDate != null && date.after(maxDate)) {
            return false;
        }
        return true;
    }

    /**
     * Method you have to implement for <code>PropertyConstraint</code>.
     * Does it get invoked?
     *
     * @param property the property
     */
    public void apply(final Property property) {
        logger.debug(this.getClass().getName() + ".apply: " + property.getName());
    }

}
