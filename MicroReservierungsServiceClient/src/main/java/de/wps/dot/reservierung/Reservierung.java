package de.wps.dot.reservierung;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Reservierung {

	// Factory vergibt eindeutige Reservierungsnummer
	private static int letzteReservierungsnummer = 0;

	public static Reservierung erzeugeReservierung(String vorführungskennung, List<Sitznummer> sitznummern) {
		return new Reservierung(vorführungskennung, sitznummern, ++letzteReservierungsnummer);
	}

	private List<Sitznummer> sitznummern;
	private String vorführungkennung;
	private int reservierungsnummer;

	public Reservierung(String vorführungskennung, List<Sitznummer> sitznummern, int reservierungsnummer) {
		this.vorführungkennung = vorführungskennung;
		this.sitznummern = sitznummern;
		this.reservierungsnummer = reservierungsnummer;
	}

	public Collection<Sitznummer> getSitznummern() {
		return Collections.unmodifiableCollection(sitznummern);
	}

	public String getVorführungsnummer() {
		return vorführungkennung;
	}

	public int getReservierungsnummer() {
		return reservierungsnummer;
	}
}
