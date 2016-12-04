package de.wps.dot.filmprogramm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Vorführung {
	private String id;
	private String filmtitel;
	private String saalId;
	private LocalDateTime beginn;

	public Vorführung(String filmtitel, String saalId, LocalDateTime beginn) {
		this.filmtitel = filmtitel;
		this.saalId = saalId;
		this.beginn = beginn;
		this.id = String.join("_", formatiereAlsID(filmtitel), saalId, formatiereAlsID(beginn));
	}

	private String formatiereAlsID(LocalDateTime begin) {
		return begin.format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH'h'"));
	}

	private String formatiereAlsID(String filmTitel) {
		return filmTitel.toLowerCase().replaceAll("ß", "ss").replaceAll(" ", "_");
	}

	public String getId() {
		return id;
	}

	public String getFilmtitel() {
		return filmtitel;
	}

	public String getSaalId() {
		return saalId;
	}

	public LocalDateTime getBegin() {
		return beginn;
	}

}
