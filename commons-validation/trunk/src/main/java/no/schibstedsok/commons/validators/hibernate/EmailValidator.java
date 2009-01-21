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
