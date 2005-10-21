package no.schibstedsok.security.persistence.authentication.onetime;

import java.util.Collection;

import no.schibstedsok.common.persistence.dal.Manager;

/**
 * @author Lars Preben S. Arnesen &lt;lars.preben.arnesen@conduct.no&gt;
 * @version $Revision$
 */
public interface OneTimeTicketPersistenceManager extends Manager {

    /**
     * Add a ticket.
     * 
     * @param ticket
     *            the ticket to persist
     * @return an instance of the persisted ticket
     */
    OneTimeTicketEntity add(OneTimeTicketEntity ticket);

    /**
     * Update an existing ticket.
     * 
     * @param ticket
     *            the ticket to update
     * @return an instance of the updated ticket
     */
    OneTimeTicketEntity update(OneTimeTicketEntity ticket);

    /**
     * Remove a ticket from the persistence store.
     * 
     * @param ticket
     *            the ticket to remove
     */
    void remove(OneTimeTicketEntity ticket);

    /**
     * Find a ticket by it's primary key.
     * 
     * @param id
     *            the id of the ticket
     * @return an instance of the ticket identified by it's primary key
     */
    OneTimeTicketEntityImpl findByPrimaryKey(Long id);

    /**
     * Find a ticket by it's unique code.
     * 
     * @param code
     *            the code of the ticket to find
     * @return an instance of the ticked with the given code
     */
    Collection findTicketByCode(String code);
}
