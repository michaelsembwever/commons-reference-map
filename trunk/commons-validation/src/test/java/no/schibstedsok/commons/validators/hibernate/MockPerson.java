/*
 * Copyright (2005-2007) Schibsted Søk AS
 */
package no.schibstedsok.commons.validators.hibernate;

/**
 * Mock person for validation test.
 *
 * @author <a href="mailto:endre@sesam.no">Endre Midtgård Meckelborg</a>
 * @author <a href="mailto:ola@sesam.no">Ola Sagli</a>
 * @version <tt>$Revision: $</tt>
 */
public class MockPerson {

    private String phone;

    /**
     * Constructor.
     *
     * @param phone the phone number to set.
     */
    public MockPerson(final String phone) {
        this.phone = phone;
    }

    /**
     * @return the phone number.
     */
    @Phone
    public String getPhone() {
        return phone;
    }

    /**
     * @param phone the phone number to set.
     */
    public void setPhone(final String phone) {
        this.phone = phone;
    }

}
