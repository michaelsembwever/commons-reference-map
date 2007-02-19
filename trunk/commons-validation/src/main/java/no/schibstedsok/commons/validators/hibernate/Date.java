/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.hibernate.validator.ValidatorClass;

/**
 * Validator class that validates a date against a min and/or a max date
 * given by text, i.e. "01.01.1900". The conversion from strings to
 * dates is done with default time zone. How should we handle different
 * time zones?
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(DateValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Date {

    // TODO: Uses default timezone. How should we handle timezones?

    /**
     * Date used to validate against a minDate. Defaults to none.
     */
    String minDate() default "";

    /**
     * Date used to validate against a maxDate. Defaults to none.
     */
    String maxDate() default "";

    /**
     * Default validator message.
     */
    String message() default "{validator.date}";

}
