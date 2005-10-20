package no.schibstedsok.security.persistence.authentication.onetime;

import java.util.Date;

/**
 * @hibernate.class table="authticket" mutable="true"
 * @hibernate.cache usage = "read-write"
 * 
 * @hibernate.query name =
 *                  "no.schibstedsok.admin.persistence.security.authentication.onetime.findByCode"
 *                  query = "from OneTimeTicketEntityImpl ticket where
 *                  person.code = ?"
 */
public final class OneTimeTicketEntityImpl implements OneTimeTicketEntity {

    /** Unique ticket id. */
    private Long id;

    /** Target identifier type. */
    private String identifierType;

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

    /**
     * @see OneTimeTicketEntity#getId()
     * @hibernate.id generator-class="native" not-null="true"
     */
    public Long getId() {
        return id;
    }

    /**
     * @see OneTimeTicketEntity#getIdentifierType()
     * @hibernate.property not-null="true" length="50"
     */
    public String getIdentifierType() {
        return identifierType;
    }

    /**
     * @see OneTimeTicketEntity#setIdentifierType(String)
     */
    public void setIdentifierType(final String identifierType) {
        this.identifierType = identifierType;
    }

    /**
     * @see OneTimeTicketEntity#getIdentifier()
     * @hibernate.property not-null="true" length="100"
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
     * @hibernate.property not-null="true"
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

}
