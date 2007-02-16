/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Validator class that validates an email address by using a regexp pattern.
 *
 * @deprecated Use the hibernate annotation instead
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(ValidEmailValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface ValidEmail {

    /** Error message for the validator. */
    String message() default "Not a valid email address.";

}
