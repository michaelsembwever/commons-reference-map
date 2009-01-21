/*
 * Copyright (2005-2007) Schibsted Søk AS
 * This file is part of Sesat Commons.
 *
 *   Sesat Commons is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Sesat Commons is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with Sesat Commons.  If not, see <http://www.gnu.org/licenses/>.
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
