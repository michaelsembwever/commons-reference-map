package no.schibstedsok.security.domain.authentication.onetime;

import java.util.Calendar;
import java.util.Date;

import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketEntity;
import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketEntityManagerImpl;

/**
 * @see no.schibstedsok.security.domain.authentication.onetime.OneTimeAuthenticationManager
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeAuthenticationManagerImpl implements OneTimeAuthenticationManager {

    /** Maximum length of ticket code. */
    public final static int MAX_TICKET_LENGTH = 100;

    public String generateTicket(String identifier, int ttl, int ticketLength, int maxTickets)
            throws MaxAttemptsExceededException {
        // Fetch existing (id)
        // Create
        // Return created
        return null;
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
            limit.add(Calendar.HOUR, -24);
            
            if (limit.getTime().compareTo(ticket.getCreationDate()) == 1) {
                ticket.setCount(new Integer(0));
            }
        }                
        
        /* Max number of tickets */
        if (ticket.getCount().intValue() >= maxTickets) {
            throw new MaxAttemptsExceededException();
        }        

        ticket.setCount(new Integer(ticket.getCount().intValue()+1));
        ticket.setExpiryDate(expires.getTime());
        ticket.setCode(PasswordGenerator.generateAlphaNumericString(ticketLength));

        return ticket;
    }

    /**
     * @see OneTimeAuthenticationManager
     */
    public boolean authenticate(final String identifier, final String ticketCode) throws TicketTimeoutException {
        // Find ticket
        // validate
        // hvis true, lagre i databasen
        return false;
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
        return persistedTicket.getCode().equals(ticketCode) && persistedTicket.getIdentifier().equals(identifier);
    }
    
}
