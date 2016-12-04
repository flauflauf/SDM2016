package de.wps.dot.reservierung;

import static org.junit.Assert.*;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.wps.dot.reservierung.Saal;
import de.wps.dot.reservierung.Sitznummer;
import de.wps.dot.reservierung.Sitzplatzbelegung;

public class SitzplatzbelegungTest {

	private Sitzplatzbelegung sitzplatzbelegungImTest;

	@Before
	public void setup() {
		sitzplatzbelegungImTest = new Sitzplatzbelegung(new Saal("Saal", 2, 3), "v1");
	}

	@Test
	public void gibFreieSitze_leer() {

		// Act
		Set<Sitznummer> freieSitze = sitzplatzbelegungImTest.gibFreieSitze();

		// Assert
		assertThat(freieSitze.size(), is(equalTo(6)));
		assertThat(freieSitze, containsInAnyOrder(Sitznummer.von("A", 1), Sitznummer.von("A", 2), Sitznummer.von("B", 1), Sitznummer.von("B", 2), Sitznummer.von("C", 1), Sitznummer.von("C" ,2)));
	}
	
	@Test
	public void gibVorführungsId_istV1(){
		assertThat(sitzplatzbelegungImTest.gibVorführungsId(), is(equalTo("v1")));
	}

	@Test
	public void bucheSitze_einSitz() throws Exception {
		// Arrange
		List<Sitznummer> sitze = Arrays.asList(Sitznummer.von("A", 1));

		// Act
		boolean result = sitzplatzbelegungImTest.bucheSitze(sitze);

		// Assert
		assertThat(sitzplatzbelegungImTest.gibFreieSitze(), containsInAnyOrder(Sitznummer.von("A", 2), Sitznummer.von("B", 1), Sitznummer.von("B", 2), Sitznummer.von("C", 1), Sitznummer.von("C" ,2)));
		assertThat(result, is(true));
	}

	@Test
	public void bucheSitze_zweiSitze() throws Exception {
		// Arrange
		List<Sitznummer> sitze = Arrays.asList(Sitznummer.von("A", 1), Sitznummer.von("B", 1));

		// Act
		boolean result = sitzplatzbelegungImTest.bucheSitze(sitze);

		// Assert
		assertThat(sitzplatzbelegungImTest.gibFreieSitze(), containsInAnyOrder(Sitznummer.von("A", 2), Sitznummer.von("B", 2), Sitznummer.von("C", 1), Sitznummer.von("C", 2)));
		assertThat(result, is(true));
	}

	@Test
	public void bucheSitze_SitzeNichtVerfügbar() throws Exception {
		// Arrange
		List<Sitznummer> sitze = Arrays.asList(Sitznummer.von("A", 1), Sitznummer.von("B", 1));
		sitzplatzbelegungImTest.bucheSitze(sitze);

		// Act
		boolean result = sitzplatzbelegungImTest.bucheSitze(Arrays.asList(Sitznummer.von("A", 1), Sitznummer.von("C", 1)));

		// Assert
		assertThat(sitzplatzbelegungImTest.gibFreieSitze(), containsInAnyOrder(Sitznummer.von("A", 2), Sitznummer.von("B", 2), Sitznummer.von("C", 1), Sitznummer.von("C", 2)));
		assertThat(result, is(false));
	}

}
