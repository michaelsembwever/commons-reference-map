package no.schibstedsok.security.domain.authentication.onetime;

/**
 * Interface for the one time authentication manager.
 * 
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public interface OneTimeAuthenticationManager {

    /**
     * Create a onetime ticket for a given identifier. The reason for the
     * identifierType is to be able to create tickets for more than one type
     * with the same id.
     * 
     * If the maximum number of tickets exists in the database an exception is
     * raised. The database should be cleared of old tickets periodically.
     * 
     * @param identifier
     *            the unique identifier for the authentication target
     * @param ttl
     *            time to live in seconds
     * @param ticketLength
     *            number of characters in the ticket
     * @param maxTickets
     *            the maximum number of tickets to be generated for a given
     *            target
     * @return a <code>String</code> containing the generated ticket
     * @throws MaxAttemptsExceededException
     *             if the maximum number of tickets for a
     *             identifier/identifierType exists
     */
    String generateTicket(String identifier, int ttl, int ticketLength, int maxTickets)
            throws MaxAttemptsExceededException;

    /**
     * Validates a one time ticket. A ticket that has been used or has timed out
     * cannot be used.
     * 
     * @param identifier
     *            the unique identifier for the authentication target (typically
     *            a primary key)
     * @param ticketCode
     *            the ticket code to validate
     * @return true if the ticket is valid, else false
     * @throws TicketTimeoutException
     *             if the ticket has timed out
     */
    boolean authenticate(String identifier, String ticketCode) throws TicketTimeoutException;

}
