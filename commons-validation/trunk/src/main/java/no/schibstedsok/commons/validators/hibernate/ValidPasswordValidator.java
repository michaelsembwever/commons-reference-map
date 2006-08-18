/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

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
public class ValidPasswordValidator implements Validator<ValidPassword>, PropertyConstraint {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(ValidPasswordValidator.class);

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
        if (value == null || !(value instanceof String) || value.toString().trim().length() < 5) {
            return false;
        }

        final String str = ((String) value).trim();
        Character c;
        boolean containsLowercase = false;
        boolean containsUppercase = false;
        boolean containsDigit = false;

        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);

            if (Character.isDigit(c)) {
                containsDigit = true;
            }
            if (Character.isLowerCase(c)) {
                containsLowercase = true;
            }
            if (Character.isUpperCase(c)) {
                containsUppercase = true;
            }
        }

        return (containsLowercase && containsUppercase && containsDigit);
    }

    /**
     * Method you have to implement for <code>PropertyConstraint</code>.
     * Does it get invoked?
     *
     * @param property the property
     */
    public void apply(final Property property) {
        logger.debug("ValidPasswordValidator.apply: " + property.getName());
    }

}
