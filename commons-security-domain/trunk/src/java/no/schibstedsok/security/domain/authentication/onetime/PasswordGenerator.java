package no.schibstedsok.security.domain.authentication.onetime;


public class PasswordGenerator {

	private static int ASCII_TABLE_INDEX_START=65; // eq. = 'A'
	private static int ASCII_TABLE_INDEX_END=90; // eq. = 'Z'
	
	public static String generateNumericString(int min, int max) {
		int randomInt = getRandomNumber(min, max);
		
		return String.valueOf(randomInt);
	}

	public static String generateAlphaNumericString(int numberOfCharacters) {			

		String pw = "";
		
		for (int j = 0; j < numberOfCharacters; j++) {
			int randomInt = getRandomNumber(ASCII_TABLE_INDEX_START, ASCII_TABLE_INDEX_END);
			char ch = (char)randomInt;
			pw += ch;
		}
		
		return pw;	
	}
	
	private static int getRandomNumber(int min, int max){
		return (int)((max-min) * Math.random()) + min;
	}

	
}
