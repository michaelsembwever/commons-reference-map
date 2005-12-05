package no.schibstedsok.security.persistence.authentication.onetime;

import java.util.Date;

/**
 * @hibernate.class table="authticket" mutable="true"
 * @hibernate.cache usage = "transactional"
 * @hibernate.query name =
 *                  "no.schibstedsok.security.persistence.authentication.onetime.OneTimeTicketPersistenceManager.findByIdentifier"
 *                  query = "from OneTimeTicketEntityImpl ticket where
 *                  ticket.identifier = ?"
 */
public class OneTimeTicketEntityImpl implements OneTimeTicketEntity {

    /** Unique ticket id. */
    private Long id;

    /** Target identifier. */
    private String identifier;

    /** Ticket expiry date. */
    private Date expiryDate;

    /** Ticket creation date. */
    private Date creationDate;

    /** Ticket used date. */
    private Date usedDate;

    /** Ticket identifier. */
    private String code;

    /** Ticket counter. */
    private Integer count;

    /**
     * @see OneTimeTicketEntity#getId()
     * @hibernate.id generator-class="native" not-null="true"
     */
    public Long getId() {
        return id;
    }

    /**
     * Sets the id field.
     * 
     * @param id
     *            the id to set
     */
    void setId(final Long id) {
        this.id = id;
    }

    /**
     * @see OneTimeTicketEntity#getIdentifier()
     * @hibernate.property not-null="true" length="100" unique="true"
     */
    public String getIdentifier() {
        return identifier;
    }

    /**
     * @see OneTimeTicketEntity#setIdentifier(String)
     */
    public void setIdentifier(final String identifier) {
        this.identifier = identifier;
    }

    /**
     * @see OneTimeTicketEntity#getExpiryDate()
     * @hibernate.property not-null="true"
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * @see OneTimeTicketEntity#setExpiryDate(Date)
     */
    public void setExpiryDate(final Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * @see OneTimeTicketEntity#getCreationDate()
     * @hibernate.property not-null="true"
     */
    public Date getCreationDate() {
        return creationDate;
    }

    /**
     * @see OneTimeTicketEntity#setCreationDate(Date)
     */
    public void setCreationDate(final Date creationDate) {
        this.creationDate = creationDate;
    }

    /**
     * @see OneTimeTicketEntity#getUsedDate()
     * @hibernate.property
     */
    public Date getUsedDate() {
        return usedDate;
    }

    /**
     * @see OneTimeTicketEntity#setUsedDate(Date)
     */
    public void setUsedDate(final Date usedDate) {
        this.usedDate = usedDate;
    }

    /**
     * @see OneTimeTicketEntity#getCode()
     * @hibernate.property not-null="true" length="100"
     */
    public String getCode() {
        return code;
    }

    /**
     * @see OneTimeTicketEntity#setCode(String)
     */
    public void setCode(final String code) {
        this.code = code;
    }

    /**
     * @see OneTimeTicketEntity#getCount()
     * @hibernate.property
     */
    public Integer getCount() {
        return count;
    }

    /**
     * @see OneTimeTicketEntity#setCount(Integer)
     */
    public void setCount(final Integer count) {
        this.count = count;
    }
}
