package no.schibstedsok.commons.validators.hibernate;

import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.validator.Validator;

/**
 * Check if input matches norwegian mobile phonenumbers..
 * @author ola mh sagli <a href="mailto:ola@sesam.no"> ola@sesam.no </a>
 *
 */
public class MobilePhoneValidator implements Validator<MobilePhone> {
    
    private static Logger log = Logger.getLogger(MobilePhoneValidator.class);
    
    /** Pattern for matching norwegian mobile phonenumbers... */
    private Pattern p_no_no = Pattern.compile("^(\\+47)?([49](\\d){7}\\s*,?\\s*)+$");
    
    /**
     * Initalize parameters. 
     */
    public void initialize(MobilePhone parameters) {
    }

    /**
     * Check if input matches norwegian mobile phonenumbers... 
     */
    public boolean isValid(Object input) {

        if(input == null) {
            return true;
        }
        if(!(input instanceof String)) {
            return false;
        }
        boolean matches = p_no_no.matcher((String)input).matches();
        
        log.info( input + " is mobile phone ?" + matches);
        return matches;
    }
}
