package no.schibstedsok.security.domain.authentication.onetime;

/**
 * Factory implementation.
 * 
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeAuthenticationManagerFactoryImpl implements OneTimeAuthenticationManagerFactory {

    /**
     * @see OneTimeAuthenticationManagerFactory#getImpl()
     */
    public OneTimeAuthenticationManager getImpl() {
        return new OneTimeAuthenticationManagerImpl();
    }

}
