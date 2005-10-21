package no.schibstedsok.security.persistence.authentication.onetime;

import java.util.Date;

/**
 * @author Lars Preben S&oslash;rsdahl &lt;lars.preben@conduct.no&gt;
 * @version $Revision$
 * @mock
 */
public interface OneTimeTicketEntity {

    /**
     * Returns the id field.
     * 
     * @return the id
     */
    Long getId();

    /**
     * Returns the identifier.
     * 
     * @return the identifier
     */
    String getIdentifier();

    /**
     * Sets the identifier field.
     * 
     * @param identifier
     *            the identifier
     */
    void setIdentifier(String identifier);

    /**
     * Returns the expiry date field.
     * 
     * @return the expiry date
     */
    Date getExpiryDate();

    /**
     * Sets the expiry date field.
     * 
     * @param expiryDate
     *            the expiry date
     */
    void setExpiryDate(Date expiryDate);

    /**
     * Returns the creation date field.
     * 
     * @return the creation date
     */
    Date getCreationDate();

    /**
     * Sets the creation date field.
     * 
     * @param creationDate
     *            the creation date
     */
    void setCreationDate(Date creationDate);

    /**
     * Returns the used date field.
     * 
     * @return the date the ticked was used
     */
    Date getUsedDate();

    /**
     * Sets the used date field.
     * 
     * @param usedDate
     *            the used date
     */
    void setUsedDate(Date usedDate);

    /**
     * Returns the ticket code field.
     * 
     * @return the code
     */
    String getCode();

    /**
     * Sets the ticket code field.
     * 
     * @param code
     *            the code
     */
    void setCode(String code);

    /**
     * Returns the ticket counter field.
     * 
     * @return
     */
    Integer getCount();

    /**
     * Sets the ticket count field.
     * 
     * @param count
     *            the count value
     */
    void setCount(Integer count);

}
