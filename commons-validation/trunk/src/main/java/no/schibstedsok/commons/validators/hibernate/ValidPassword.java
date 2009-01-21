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

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Validator class that validates a string agains the Schibsted Sok password rules.
 *
 * @deprecated Replaced by {@link Password}
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(ValidPasswordValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface ValidPassword {

    // TODO: Change this to use resource bundle?

    /** Norwegian message for illegal password. */
    String INVALID_PASSWORD_MESSAGE_NO =
        "Ikke gyldig passord (må være minst 8 tegn og inneholde minst et spesialtegn)";

    /** English message for illegal password. */
    String INVALID_PASSWORD_MESSAGE_EN =
        "Not a legal password (must be at least 8 characters long and have at least one special character)";

    /** Error message for the validator. */
    String message() default INVALID_PASSWORD_MESSAGE_NO;

}
