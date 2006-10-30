package no.schibstedsok.security.domain.authentication.onetime;

import java.util.Date;

import junit.framework.TestCase;
import no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketEntity;

/**
 * Test class for the one time ticket authentication.
 * 
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 */
public final class OneTimeAuthenticationTest extends TestCase {

    /** Manager. */
    private final OneTimeAuthenticationManagerImpl authMan = (OneTimeAuthenticationManagerImpl) new OneTimeAuthenticationManagerFactoryImpl()
            .getImpl();

    /** Valid ticket code. */
    private static final String VALID_CODE = "ADNES";

    /** Valid id. */
    private static final String VALID_ID = "person1";

    /** Valid time to live. */
    private static final int VALID_TTL = 180;

    /** Too big ticket length. */
    private static final int TOO_LONG_TICKET = 101;

    /** Valid ticket length. */
    private static final int VALID_TICKET_LENGTH = 5;

    /** Valid maximum number of tickets. */
    private static final int VALID_MAX_TICKETS = 3;


    /**
     * Test the ticket generation.
     * 
     * @throws MaxAttemptsExceededException
     *             indicates that the test failed unexpectedly
     */
    public void testCreateTicket() throws MaxAttemptsExceededException {

        /* Invalid arguments */
        try {
            authMan.createTicket(null, VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
            fail("NullPointerException should be raised: identifier is null");
        } catch (NullPointerException success) {
        }
        try {
            authMan.createTicket("", VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: identifier is empty");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, 0, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: ttl is 0");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, -1, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: ttl is -1");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, 0, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: ticket length is 0");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, -1, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: ticket length is -1");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, TOO_LONG_TICKET, VALID_MAX_TICKETS, null);
            fail("IllegalArgumentException should be raised: ticket length is more than max");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, 0, null);
            fail("IllegalArgumentException should be raised: max tickets is 0");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, -1, null);
            fail("IllegalArgumentException should be raised: max tickets is -1");
        } catch (IllegalArgumentException success) {
        }

        OneTimeTicketEntity ticket = authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, 1, null);

        assertNotNull("Code should not be null.", ticket.getCode());
        assertNotNull("Creation date should not be null.", ticket.getCreationDate());
        assertNotNull("Expiry date should not be null.", ticket.getExpiryDate());
        assertTrue("Expiry date should be greater than creation date.", ticket.getExpiryDate().compareTo(
                ticket.getCreationDate()) > 0);
        assertEquals("Identifier differs.", VALID_ID, ticket.getIdentifier());
        assertNull("Used date should be null.", ticket.getUsedDate());

        /* Too many tickets */
        try {
            authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, 1, ticket);
            fail("MaxAttemtsExceededException should be raised.");
        } catch (MaxAttemptsExceededException success) {
        }
    }

    /**
     * Test ticket validation.
     * 
     * @throws TicketTimeoutException
     *             if test ended unexpectedly
     * @throws InterruptedException
     *             if test ended unexpectedly
     * @throws MaxAttemptsExceededException
     *             if test ended unexpectedly
     */
    public void testValidateTicket() throws TicketTimeoutException, InterruptedException, MaxAttemptsExceededException {
        /* Invalid arguments */
        try {
            authMan.validateTicket(null, VALID_CODE, null);
            fail("NullPointerException should be raised, identifier is null.");
        } catch (NullPointerException success) {
        }
        try {
            authMan.validateTicket("", VALID_CODE, null);
            fail("IllegalArgumentException should be raised, identifier is empty.");
        } catch (IllegalArgumentException success) {
        }
        try {
            authMan.validateTicket(VALID_ID, null, null);
            fail("NullPointerException should be raised, code is null.");
        } catch (NullPointerException success) {
        }
        try {
            authMan.validateTicket(VALID_ID, "", null);
            fail("IllegalArgumentException should be raised, code is empty.");
        } catch (IllegalArgumentException success) {
        }

        /* Timeout */
        OneTimeTicketEntity ticket = authMan
                .createTicket(VALID_ID, 1, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
        Thread.sleep(2000);
        try {
            authMan.validateTicket(VALID_ID, ticket.getCode(), ticket);
            fail("TicketTimeoutException should be raised.");
        } catch (TicketTimeoutException success) {
        }

        /* Ticket already used */
        ticket = authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
        assertTrue("Ticket should be validated correctly.", authMan.validateTicket(VALID_ID, ticket.getCode(),
                ticket));
        ticket.setUsedDate(new Date());
        assertFalse("Ticket should not be valid, already used.", authMan.validateTicket(VALID_ID, ticket.getCode(),
                ticket));

        /* Non-existent ticket */
        assertFalse("Ticket should not be valid, it does not exist.", authMan.validateTicket(VALID_ID, "doesNotExist",
                null));

        /* Identifier and/or type does not match */
        ticket = authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
        assertFalse("Ticket is not valid, identifier differs.", authMan.validateTicket("doesNotExist",
                ticket.getCode(), ticket));
        assertTrue("Ticket should be validated correctly.", authMan.validateTicket(VALID_ID, ticket.getCode(),
                ticket));

        /* Only last unused ticket should be usable */
        ticket = authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
        OneTimeTicketEntity ticket2 = authMan.createTicket(VALID_ID, VALID_TTL, VALID_TICKET_LENGTH, VALID_MAX_TICKETS, null);
        assertFalse("The first ticket should not be valid.", authMan.validateTicket("doesNotExist", ticket.getCode(),
                ticket));
        assertTrue("The second ticket should be valid.", authMan.validateTicket(VALID_ID, ticket2.getCode(), ticket2));
    }

}
