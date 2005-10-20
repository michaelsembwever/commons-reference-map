package no.schibstedsok.security.domain.authentication.onetime;


/**
 * @see no.schibstedsok.security.domain.authentication.onetime.OneTimeAuthenticationManager
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeAuthenticationManagerImpl implements OneTimeAuthenticationManager {

    /**
     * @see OneTimeAuthenticationManager
     */
    public String generateTicket(final String identifierType, final String identifier, final int ttl,
            final int ticketLength, final int maxTickets) throws MaxAttemtsExceededException {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * @see OneTimeAuthenticationManager
     */
    public boolean validateTicket(final String identifierType, final String identifier, final String ticket)
            throws TicketTimeoutException {
        // TODO Auto-generated method stub
        return false;
    }
}
