package no.schibstedsok.security.persistence.authentication.onetime;

/**
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public interface OneTimeTicketEntityManager {

    /**
     * Create a new one time ticket.
     * 
     * @return a <code>OneTimeTicketEntity</code> instance
     */
    OneTimeTicketEntity newOneTimeTicket();
}
