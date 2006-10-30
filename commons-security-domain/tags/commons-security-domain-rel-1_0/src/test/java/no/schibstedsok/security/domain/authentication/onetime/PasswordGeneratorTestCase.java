package no.schibstedsok.security.domain.authentication.onetime;

import junit.framework.TestCase;

public class PasswordGeneratorTestCase extends TestCase {

	public void testGenerateNumericString() throws Exception {
		String pw = PasswordGenerator.generateNumericString(11111, 99999);
		int pwInt = Integer.parseInt(pw);
		
		assertTrue("Number too small:"+pwInt, pwInt > 11111);
		assertTrue("Number too big:"+pwInt, pwInt < 99999);
	}
	
	public void testGenerateAlphanumericString() throws Exception {
		String pw = PasswordGenerator.generateAlphaNumericString(5);
		assertEquals("Password string is not of correct size", pw.length(), 5);
	}
	
}
