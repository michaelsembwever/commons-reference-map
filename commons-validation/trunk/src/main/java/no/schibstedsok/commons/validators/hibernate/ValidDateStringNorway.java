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
 * Validator class that validates a string against a date format with no
 * fuzzy logic that converts year 80 to 0080 etc. You can also validate
 * the date against a min and max date given by text, i.e. "01.01.1900".
 *
 * @deprecated Replaced by {@link DateValidator}, convert to use dates.
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(ValidDateStringNorwayValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Deprecated
public @interface ValidDateStringNorway {

    /** Date used to validate against a minDate. Defaults to none. */
    String minDate() default "";

    /** Date used to validate against a maxDate. Defaults to none. */
    String maxDate() default "";

    /** Error message for the validator. */
    String message() default "Illegal date. Dateformat: dd.mm.yyyy.";

}