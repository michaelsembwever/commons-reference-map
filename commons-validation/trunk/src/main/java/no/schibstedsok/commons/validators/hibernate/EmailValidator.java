/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Annotation;

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
public class EmailValidator implements Validator<Email>, PropertyConstraint {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(EmailValidator.class);

    /** Whether the validator should trim value before validating. */
    private boolean trimValue;

    /** The validator to when validating. */
    private org.hibernate.validator.EmailValidator validator;

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final Email parameters) {
        // Our parameters.
        trimValue = parameters.trimValue();

        // Init the real validator.
        validator = new org.hibernate.validator.EmailValidator();

        validator.initialize(new org.hibernate.validator.Email() {
            public String message() {
                return parameters.message();
            }

            public Class<? extends Annotation> annotationType() {
                return null;
            }
        });
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

        return validator.isValid(toValidate);
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
