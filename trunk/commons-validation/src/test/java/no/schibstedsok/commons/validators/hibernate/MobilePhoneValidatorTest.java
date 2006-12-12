package no.schibstedsok.commons.validators.hibernate;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

import junit.framework.TestCase;

/**
 * Test valid mobile phonenumbers
 * @author ola mh <a href="mailto:ola@sesam.no">ola@sesam.no</a>
 *
 */
public class MobilePhoneValidatorTest extends TestCase {
    
    static Logger log = Logger.getLogger(MobilePhoneValidatorTest.class);
    Pattern p = Pattern.compile("^(\\+47)?([49](\\d){7}\\s*,?\\s*)+$");    
    
    /**
     * Test mobile phonenumbers
     */
    public void testPhoneNumbers() {
        MobilePhoneValidator validator = new MobilePhoneValidator();

        String mobilePrefix = "+4798203016";
        String homeNumber = "61171265";
        String homeNumberPrefix = "+4761171265";
        String mobile = "+4798203016";
        String mobileTooLong = "+4798121200000";
        String mobileTooShort = "+4798121";        
        
        assertTrue (validator.isValid(mobilePrefix));
        assertTrue (validator.isValid(mobile));
        assertFalse(validator.isValid(homeNumberPrefix));
        assertFalse(validator.isValid(homeNumber));
        assertFalse(validator.isValid(mobileTooLong));        
        assertFalse(validator.isValid(mobileTooShort));                

    }


}
