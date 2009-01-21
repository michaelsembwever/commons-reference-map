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
