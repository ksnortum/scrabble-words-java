package net.snortum.scrabblewords.controller;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import net.snortum.scrabblewords.model.InputData;

public class ValidatorTest {

	@Test
	public void testValidateNoDot() {
		InputData data = new InputData.Builder( "abc" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}
	
	@Test
	public void testValidateOneDot() {
		InputData data = new InputData.Builder( "ab.c" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}
	
	@Test
	public void testValidatorTwoDots() {
		InputData data = new InputData.Builder( "a.b.c" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 0 );
	}
	
	@Test
	public void testValidatorMoreThanTwoDots() {
		InputData data = new InputData.Builder( "a.b.c." ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.NO_MORE_THAN_TWO_DOTS );
	}
	
	@Test(expected=IllegalArgumentException.class)
	public void testNullDataValidate() {
		new Validator( null );
	}

	@Test
	public void testValidatorTooFewLettersError() {
		InputData data = new InputData.Builder( "" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.TOO_FEW_LETTERS );
	}
	
	@Test
	public void testValidatorNotLettersOrDotError() {
		InputData data = new InputData.Builder( "abc5e" ).build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.LETTERS_OR_DOTS );
	}
	
	@Test
	public void testValidatorContainsError() {
		InputData data = new InputData.Builder( "abc" )
				.contains( "abcdefghijklmnopqrstuvwxyz" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.CONTAINS_TOO_LONG );
	}
	
	@Test
	public void testValidatorStartsWithNonletters() {
		InputData data = new InputData.Builder( "abc" )
				.startsWith( "a5" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.STARTSWITH_NONLETTERS );
	}
	
	@Test
	public void testValidatorEndsWithNonletters() {
		InputData data = new InputData.Builder( "abc" )
				.endsWith( "a5" )
				.build();
		Validator validator = new Validator( data );
		List<String> message = validator.validate();
		assertTrue( message.size() == 1 );
		assertEquals( message.get( 0 ), Validator.ENDSWITH_NONLETTERS );
	}
	
	@Test
	public void testNumOfLettersCanBeBlank() {
		InputData data = new InputData.Builder("abc")
				.numOfLetters(" ")
				.build();
		Validator validator = new Validator(data);
		List<String> message = validator.validate();
		assertTrue(message.size() == 0);
	}
	
	@Test
	public void testNumOfLettersIsNumeric() {
		InputData data = new InputData.Builder("abc")
				.crosswordMode(true)
				.numOfLetters("X")
				.build();
		Validator validator = new Validator(data);
		List<String> message = validator.validate();
		assertTrue(message.size() == 1);
		assertEquals(message.get(0), Validator.INVALID_NUMBER);
	}
	
	@Test
	public void testNumOfLettersLessThanTwenty() {
		InputData data = new InputData.Builder("abc")
				.crosswordMode(true)
				.numOfLetters("21")
				.build();
		Validator validator = new Validator(data);
		List<String> message = validator.validate();
		assertTrue(message.size() == 1);
		assertEquals(message.get(0), Validator.TOO_MANY_NUM_OF_LETTERS);
	}
}
