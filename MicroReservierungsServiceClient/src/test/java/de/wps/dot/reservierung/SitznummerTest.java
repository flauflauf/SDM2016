package de.wps.dot.reservierung;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import de.wps.dot.reservierung.Sitznummer;

public class SitznummerTest {

	@Test
	public void testGibName_ersterSitz() {
		// Arrange
		Sitznummer sitznummer = Sitznummer.von(0, 1);
		
		// Act + Arrange
		assertEquals("A1", sitznummer.gibName());
	}

	@Test
	public void testGibName_ersterSitzMitBuchstabeA() {
		// Arrange
		Sitznummer sitznummer = Sitznummer.von("A", 1);
		
		// Act + Arrange
		assertEquals("A1", sitznummer.gibName());
	}
	
	@Test
	public void testGibName_ersterSitzMitBuchstabeZ() {
		// Arrange
		Sitznummer sitznummer = Sitznummer.von("Z", 1);
		
		// Act + Arrange
		assertEquals("Z1", sitznummer.gibName());
	}
	
	@Test
	public void testGibName_letzteReihe() {
		// Arrange
		Sitznummer sitznummer = Sitznummer.von(25, 12);
		
		// Act + Arrange
		assertEquals("Z12", sitznummer.gibName());
	}
	
	@Test
	public void erzeugeSitznummer(){
		Sitznummer sitznummer = Sitznummer.von("C",	2);
		
		assertThat(sitznummer.gibSitzReihe(), is(2));
	}
}
