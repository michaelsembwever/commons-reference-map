package no.schibstedsok.commons.validators.hibernate;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Locale;

import org.hibernate.validator.ValidatorClass;

@ValidatorClass(MobilePhoneValidator.class)
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MobilePhone {

    /**
     * The locale used so we can recognize the country 
     * we're in.
     * @return locale
     */
    String country () default "no";
    
    /**
     * Default message is validator.mobilephone
     * @return
     */
    String message () default "{validator.mobilephone}";
    
    
}
