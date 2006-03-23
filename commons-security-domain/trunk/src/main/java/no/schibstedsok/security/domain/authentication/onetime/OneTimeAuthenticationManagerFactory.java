package no.schibstedsok.security.domain.authentication.onetime;

/**
 * Factory class for creating authentication manager.
 * 
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public interface OneTimeAuthenticationManagerFactory {

    /**
     * Get a concrete implementation.
     * 
     * @return an <code>OneTimeAuthenticationManager</code> instance
     */
    OneTimeAuthenticationManager getImpl();
}
