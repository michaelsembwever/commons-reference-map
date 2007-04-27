/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import org.apache.log4j.Logger;
import org.hibernate.validator.Validator;

/**
 * {@inheritDoc}
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @author <a href="mailto:ola@sesam.no">Ola Sagli</a>
 * @version <tt>$Revision: $</tt>
 */
public class PhoneValidator implements Validator<Phone> {

    /** Logger for this class. */
    private static final Logger LOG = Logger.getLogger(PhoneValidator.class);

    /** Phone type to use when validating. */
    private PhoneType phoneType;

    /** Whether the validator should strip blanks before validating. */
    private boolean stripBlanks;

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final Phone parameters) {
        phoneType = parameters.phoneType();
        stripBlanks = parameters.stripBlanks();
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

        final String toValidate = stripBlanks ? stripBlanks((String) value) : (String) value;

        return phoneType.getPattern().matcher(toValidate).matches();
    }

    /**
     * Method that strip blanks from strings. This is i.e. used to handle phone numbers
     * like "22 33 44 55".
     *
     * @param value the value to remove blanks from
     * @return the value without blanks
     */
    private String stripBlanks(final String value) {
        if (value == null) {
            return null;
        }

        final StringBuilder withoutBlanks = new StringBuilder();

        for (char c : value.toCharArray()) {
            if (c != ' ') {
                withoutBlanks.append(c);
            }
        }

        return withoutBlanks.toString();
    }

}
