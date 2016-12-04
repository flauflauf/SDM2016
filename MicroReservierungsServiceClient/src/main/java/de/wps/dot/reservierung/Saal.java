package de.wps.dot.reservierung;

import java.util.ArrayList;
import java.util.List;

public class Saal {
	private final Sitznummer[][] saalLayout;
	private String name;
	private String id;

	public Saal(String name, int reihenBreite, int reihen) {
		super();
		saalLayout = erzeugeSitze(reihen, reihenBreite);
		this.name = name;
		id = name.toLowerCase().replaceAll("ß", "ss").replaceAll(" ", "_");
	}

	public String getName() {
		return name;
	}

	public int gibReihenBreite() {
		return saalLayout[0].length;
	}

	public int gibReihen() {
		return saalLayout.length;
	}

	public String getId() {
		return id;
	}

	public Sitznummer[][] gibLayout() {
		return saalLayout;
	}
	
	public List<Sitznummer> gibSitze() {
		List<Sitznummer> result = new ArrayList<Sitznummer>();
		for(int i = 0; i < saalLayout.length; i++) {
			for(int j = 0; j < saalLayout[i].length; j++) {
				result.add(saalLayout[i][j]);
			}
		}
			
		return result;
	}

	private Sitznummer[][] erzeugeSitze(int reihen, int reihenBreite) {
		Sitznummer[][] sitze = new Sitznummer[reihen][reihenBreite];

		for (int reihe = 0; reihe < reihen; reihe++) {
			for (int sitzNummer = 1; sitzNummer <= reihenBreite; sitzNummer++) {
				sitze[reihe][sitzNummer-1] = Sitznummer.von(reihe, sitzNummer);
			}
		}
		return sitze;
	}

}
