/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.util.regex.Pattern;

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
public class UrlValidator implements Validator<Url>, PropertyConstraint {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(UrlValidator.class);

    /** Whether the validator should trim value before validating. */
    private boolean trimValue;

    /** Regex pattern used by this validator. */
    private Pattern pattern =
        Pattern.compile("^https?://([-\\w\\.]+)+(:\\d+)?(/~?([\\w/_\\.]*(\\?\\S+)?)?)?$");

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final Url parameters) {
        trimValue = parameters.trimValue();
    }

    /**
     * The validate method.
     *
     * @param value the value to validate
     * @return the validation result
     */
    public boolean isValid(final Object value) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof String)) {
            return false;
        }

        final String toValidate = trimValue ? ((String) value).trim() : (String) value;

        return pattern.matcher(toValidate).matches();
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
