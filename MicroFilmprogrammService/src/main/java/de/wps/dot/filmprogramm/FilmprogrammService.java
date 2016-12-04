package de.wps.dot.filmprogramm;

import java.util.List;

public class FilmprogrammService {

	private List<Vorführung> vorführungen;

	public FilmprogrammService(List<Vorführung> vorführungen) {
		this.vorführungen = vorführungen;
	}

	public List<Vorführung> gibVorführungen() {
		return vorführungen;
	}

}