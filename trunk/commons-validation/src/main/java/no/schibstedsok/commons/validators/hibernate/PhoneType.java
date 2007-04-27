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

    /** Norwegian phone number with 5 digits. */
    PHONE_NO_5("^(" + Constants.PREFIX_PHONE_NO + ")?" + Constants.BASE_PHONE_NO_5 + "$"),

    /** Norwegian phone number with 8 digits. */
    PHONE_NO_8("^(" + Constants.PREFIX_PHONE_NO + ")?" + Constants.BASE_PHONE_NO_8 + "$"),

    /** Norwegian general phone number. */
    PHONE_NO("^(" + Constants.PREFIX_PHONE_NO + ")?(("
        + Constants.BASE_PHONE_NO_5 + ")|(" + Constants.BASE_PHONE_NO_8 + "))$"),

    /** Norwegian mobile phone number. */
    MOBILE_NO("^(" + Constants.PREFIX_PHONE_NO + ")?" + Constants.BASE_MOBILE_NO + "$");


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

    private static class Constants {
        private static final String PREFIX_PHONE_NO = "(\\+|00)47";
        private static final String BASE_PHONE_NO_5 = "0\\d{4}";
        private static final String BASE_PHONE_NO_8 = "[123456789]\\d{7}";
        private static final String BASE_MOBILE_NO = "[49]\\d{7}";
    }

}
