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
 * Validator class that validates a url by using a regexp pattern.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(UrlValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Url {

    /**
     * Whether the validator should trim value before validating.
     */
    boolean trimValue() default false;

    /**
     * Default validator message.
     */
    String message() default "{validator.url}";

}
