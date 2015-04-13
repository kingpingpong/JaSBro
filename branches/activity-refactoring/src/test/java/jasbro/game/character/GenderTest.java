package jasbro.game.character;

import static org.junit.Assert.*;

import org.junit.Test;

public class GenderTest {

	// Example test to make sure Gradle testing works as expected
	
	@Test
	public void isFemaleTest() {
		assertTrue(Gender.FEMALE.isFemale());
		assertTrue(Gender.FUTA.isFemale());
		
		assertFalse(Gender.MALE.isFemale());
	}
}
