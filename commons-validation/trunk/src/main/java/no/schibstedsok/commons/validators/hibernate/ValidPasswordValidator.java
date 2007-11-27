/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.mapping.Property;
import org.hibernate.validator.PropertyConstraint;
import org.hibernate.validator.Validator;

/**
 * {@inheritDoc}
 *
 * @deprecated Replaced by {@link PasswordValidator}
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@Deprecated
public class ValidPasswordValidator implements Validator<ValidPassword>, PropertyConstraint {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(ValidPasswordValidator.class);

    /** The arguments sent to the validator. */
    private ValidPassword arguments;

    /**
     * Initialize the validator.
     *
     * @param initArguments the arguments sent to the validator
     */
    public void initialize(final ValidPassword initArguments) {
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

        if (!(value instanceof String) || value.toString().trim().length() < 8) {
            return false;
        }

        final String str = ((String) value).trim();
        Character c;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (!Character.isLetter(c)) {
                return true;
            }
        }

        return false;
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