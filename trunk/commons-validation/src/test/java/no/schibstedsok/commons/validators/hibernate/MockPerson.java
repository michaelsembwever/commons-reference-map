package no.schibstedsok.commons.validators.hibernate;

public class MockPerson {

    String mobilePhone;

    public MockPerson(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    @MobilePhone
    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
