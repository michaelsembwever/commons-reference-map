package no.schibstedsok.security.persistence.authentication.onetime;

/**
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeTicketEntityManagerImpl implements OneTimeTicketEntityManager {

    /**
     * @see OneTimeTicketEntityManager#newOneTimeTicket()
     */
    public OneTimeTicketEntity newOneTimeTicket() {
        return new OneTimeTicketEntityImpl();
    }
}
