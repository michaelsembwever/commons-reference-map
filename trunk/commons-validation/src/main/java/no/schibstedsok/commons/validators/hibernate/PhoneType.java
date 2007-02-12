/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.util.regex.Pattern;

/**
 * Phone types used in phone validator.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @version <tt>$Revision: $</tt>
 */
public enum PhoneType {

    /** Norwegian general phone number. */
    PHONE_NO("^(\\+47)?((\\d){8})$"),

    /** Norwegian mobile phone number. */
    MOBILE_NO("^(\\+47)?([49](\\d){7})$");

    private final Pattern pattern;

    private PhoneType(final String pattern) {
        this.pattern = Pattern.compile(pattern);
    }

    /**
     * @return the phone type pattern.
     */
    public Pattern getPattern() {
        return pattern;
    }

}
