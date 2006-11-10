/*
 * Copyright (2005-2006) Schibsted Søk AS
 */
package no.schibstedsok.commons.seam;

import java.util.Locale;
import javax.faces.context.FacesContext;
import org.apache.log4j.Logger;

public final class UserSupport {
    
    private static final Logger LOG = Logger.getLogger(UserSupport.class);
    
    /**
     * Simple helper class retrieving the currently logged
     * in user. 
     * 
     * If the user is not PortletRequest, return 'user'
     * (hack to so the tests run smooothly..)
     */
    public static String getRemoteUser() {

        try{
            return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
            
        }catch(NullPointerException npe){
            return null;
        }
    }
    
    public static Locale getRequestLocale(){
        
        try{
            return FacesContext.getCurrentInstance().getExternalContext().getRequestLocale();
            
        }catch(NullPointerException npe){
            return Locale.getDefault();
        }
    }

}
