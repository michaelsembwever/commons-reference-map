package no.schibstedsok.commons.validators.hibernate;

import java.util.Locale;

import junit.framework.TestCase;

import org.apache.log4j.Logger;
import org.hibernate.validator.ClassValidator;
import org.hibernate.validator.InvalidValue;

public class ValidatorMessagesTest extends TestCase {

    static Logger log = Logger.getLogger(ValidatorMessagesTest.class);
    /**
     * Test with locale "no"
     */
    public void testValidation() {
        internalTestValidation("Norsk: ", new Locale("no"));
    }
    
    /**
     * Fetch resources
     * @param lang
     */
    private void internalTestValidation(String prefixLog, Locale lang) {
        Locale.setDefault(lang);
        MockPerson person = new MockPerson("61171265");
        
        ClassValidator personValidator = new ClassValidator(MockPerson.class);
        InvalidValue[] values = personValidator.getInvalidValues(person);
        
        assertEquals(1, values.length);
        
        for(InvalidValue value : values) {
            log.info("PropertyPath: " + value.getRootBean());
            log.info(prefixLog + value);
        }
    }
}
