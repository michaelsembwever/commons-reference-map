package no.schibstedsok.security.domain.authentication.onetime;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import no.schibstedsok.common.persistence.dal.ServiceLocatorFactory;
import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketEntity;
import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketEntityManagerImpl;
import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketPersistenceManager;

/**
 * @see no.schibstedsok.security.domain.authentication.onetime.OneTimeAuthenticationManager
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeAuthenticationManagerImpl implements OneTimeAuthenticationManager {

    /** Maximum length of ticket code. */
    public final static int MAX_TICKET_LENGTH = 100;
    
    /** Number of hours before the ticket counter is reset. */
    private final static int TTL_CYCLE_PERIOD = -24;

    /** Ticket persistence manager. */
    private OneTimeTicketPersistenceManager ticketMgr;

    /**
     * Constructor.
     */
    public OneTimeAuthenticationManagerImpl() {
        ticketMgr = (OneTimeTicketPersistenceManager) ServiceLocatorFactory.getInstance().getDomainObjectManager(
                OneTimeTicketPersistenceManager.class);
    }

    public String generateTicket(String identifier, int ttl, int ticketLength, int maxTickets)
            throws MaxAttemptsExceededException {
        
        if (identifier.equals("")) {
            throw new IllegalArgumentException("Identifier must be a non-empty string.");
        }
        
        // Fetch existing (id)
        OneTimeTicketEntity existingTicket = null;
        Collection result = ticketMgr.findByIdentifier(identifier);
        if(result.iterator().hasNext()){
        	existingTicket = (OneTimeTicketEntity) ticketMgr.findByIdentifier(identifier).iterator().next();
        }
        // Create
        OneTimeTicketEntity updatedTicket = createTicket(identifier, ttl, ticketLength, maxTickets, existingTicket);
        // Save
        ticketMgr.update(updatedTicket);        
        // Return created
        return updatedTicket.getCode();
    }

    OneTimeTicketEntity createTicket(final String identifier, final int ttl, final int ticketLength,
            final int maxTickets, OneTimeTicketEntity persistedTicket) throws MaxAttemptsExceededException {

        /* Validate arguments */
        if (identifier.equals("")) {
            throw new IllegalArgumentException("Identifier must be a non-empty string.");
        }
        if (ttl < 1) {
            throw new IllegalArgumentException("Time to live must be >= 1.");
        }
        if (ticketLength < 1) {
            throw new IllegalArgumentException("Ticket length must be >= 1.");
        }
        if (maxTickets < 1) {
            throw new IllegalArgumentException("Maximum number of tickets must be >= 1.");
        }
        if (ticketLength > MAX_TICKET_LENGTH) {
            throw new IllegalArgumentException("Maximum ticket length is: " + MAX_TICKET_LENGTH);
        }

        Calendar now = Calendar.getInstance();
        Calendar expires = (Calendar) now.clone();
        expires.add(Calendar.SECOND, ttl);
        OneTimeTicketEntity ticket;

        if (persistedTicket == null) {
            ticket = new OneTimeTicketEntityManagerImpl().newOneTimeTicket();
            ticket.setIdentifier(identifier);
            ticket.setCreationDate(now.getTime());
            ticket.setCount(new Integer(0));
        } else {
            ticket = persistedTicket;

            Calendar limit = (Calendar) now.clone();
            limit.add(Calendar.HOUR, TTL_CYCLE_PERIOD);

            if (limit.getTime().compareTo(ticket.getCreationDate()) == 1) {
                ticket.setCount(new Integer(0));
                ticket.setCreationDate(now.getTime());
            }
        }

        /* Max number of tickets */
        if (ticket.getCount().intValue() >= maxTickets) {
            throw new MaxAttemptsExceededException();
        }

        ticket.setCount(new Integer(ticket.getCount().intValue() + 1));
        ticket.setExpiryDate(expires.getTime());
        ticket.setCode(PasswordGenerator.generateAlphaNumericString(ticketLength));
        ticket.setUsedDate(null);

        return ticket;
    }

    /**
     * @see OneTimeAuthenticationManager
     */
    public boolean authenticate(final String identifier, final String ticketCode) throws TicketTimeoutException {
        // Find ticket
        OneTimeTicketEntity existingTicket = null;
        Collection result = ticketMgr.findByIdentifier(identifier);
        if(result.iterator().hasNext()){
        	existingTicket = (OneTimeTicketEntity) ticketMgr.findByIdentifier(identifier).iterator().next();
        }
        // validate
        if (validateTicket(identifier, ticketCode, existingTicket)) {
            ticketMgr.update(existingTicket);
            return true;
        } else {
            return false;
        }
        // hvis true, lagre i databasen
    }

    boolean validateTicket(final String identifier, final String ticketCode, OneTimeTicketEntity persistedTicket)
            throws TicketTimeoutException {

        /* Validate arguments */
        if (identifier.equals("")) {
            throw new IllegalArgumentException("Identifier must be a non-empty string.");
        }
        if (ticketCode.equals("")) {
            throw new IllegalArgumentException("Ticket code must be a non-empty string.");
        }

        /* Ticket existence */
        if (persistedTicket == null) {
            return false;
        }

        /* Unused */
        if (persistedTicket.getUsedDate() != null) {
            return false;
        }

        /* Expired */
        if (new Date().compareTo(persistedTicket.getExpiryDate()) == 1) {
            throw new TicketTimeoutException();
        }

        /* Validate code and id */
        if (persistedTicket.getCode().equals(ticketCode) && persistedTicket.getIdentifier().equals(identifier)) {
            persistedTicket.setUsedDate(new Date());
            return true;
        } else {
            return false;
        }
    }

}
