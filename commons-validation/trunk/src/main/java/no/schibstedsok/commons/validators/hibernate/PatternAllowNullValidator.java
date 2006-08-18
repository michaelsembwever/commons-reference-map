/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

import java.util.regex.Pattern;

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
public class PatternAllowNullValidator implements Validator<PatternAllowNull>, PropertyConstraint {

    /** Logger for this class. */
    private static Logger logger = Logger.getLogger(PatternAllowNullValidator.class);

    /** Regex pattern used by this validator. */
    private Pattern pattern = null;

    /**
     * Initialize the validator.
     *
     * @param parameters the arguments sent to the validator
     */
    public void initialize(final PatternAllowNull parameters) {
        final String regex = parameters.regex();
        final int flag = parameters.flag();

        if (flag == PatternAllowNull.FLAG_DEFAULT) {
            pattern = Pattern.compile(regex);
        } else {
            pattern = Pattern.compile(regex, flag);
        }
    }

    /**
     * The validate method.
     *
     * @param value the value to validate
     * @return the validation result
     */
    public boolean isValid(final Object value) {
        if (value != null && !(value instanceof String)) {
            return false;
        }
        if (value == null || ((String) value).length() == 0) {
            return true;
        }

        return pattern.matcher((String) value).matches();
    }

    /**
     * Method you have to implement for <code>PropertyConstraint</code>.
     * Does it get invoked?
     *
     * @param property the property
     */
    public void apply(final Property property) {
        logger.debug(this.getClass().getName() + ".apply: " + property.getName());
    }

}
