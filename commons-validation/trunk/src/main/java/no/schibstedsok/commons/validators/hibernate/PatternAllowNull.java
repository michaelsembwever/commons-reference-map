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
 * Pattern that allows null values, but if a value is given, then it's
 * validated against a regexp pattern.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
@ValidatorClass(PatternAllowNullValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PatternAllowNull {

    /** Constant used as default flag to check if the user has added a flag. */
    int FLAG_DEFAULT = -1;

    /** Regex that is used in the validation. */
    String regex();

    /** Expression flag that is used with the pattern. */
    int flag() default FLAG_DEFAULT;

    /** Error message for the validator. */
    String message() default "Illegal value.";

}
