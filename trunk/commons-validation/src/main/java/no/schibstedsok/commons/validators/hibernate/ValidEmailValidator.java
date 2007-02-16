/*
 * Copyright (2005-2006) Schibsted Søk AS
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
 * @deprecated Use the hibernate annotation instead
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@Deprecated
public class ValidEmailValidator implements Validator<ValidEmail>, PropertyConstraint {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidEmailValidator.class);

    /** The arguments sent to the validator. */
    private ValidEmail arguments;

    /** Regex pattern used by this validator. */
    private Pattern pattern =
        Pattern.compile("^[\\w]([\\w\\.-]*[\\w])?@[\\w][\\w\\.-]*[\\w]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$");

    /**
     * Initialize the validator.
     *
     * @param initArguments the arguments sent to the validator
     */
    public void initialize(final ValidEmail initArguments) {
        this.arguments = initArguments;
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

        return pattern.matcher((String) value).matches();
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
