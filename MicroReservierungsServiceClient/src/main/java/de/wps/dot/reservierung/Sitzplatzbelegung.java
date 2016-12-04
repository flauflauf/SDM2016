
package de.wps.dot.reservierung;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Sitzplatzbelegung {
	private String vorführungsId;
	private final Saal saal;
	private final Map<Sitznummer, Boolean> sitzVerbucht;
	
	public Sitzplatzbelegung(Saal saal, String vorführungsId) {
		this.saal = saal;
		this.vorführungsId = vorführungsId;
		sitzVerbucht = new HashMap<Sitznummer, Boolean>();		

		saal.gibSitze().stream().forEach(sitz -> sitzVerbucht.put(sitz, false));
	}
	
	public Saal gibSaal() {
		return saal;
	}

	public String gibVorführungsId() {
		return vorführungsId;
	}

	public void setzeVorführungsId(String vorführungsId) {
		this.vorführungsId = vorführungsId;
	}

	public boolean sitzIstFrei(Sitznummer sitznummer) {
		
		return !sitzVerbucht.get(sitznummer);
	}
	
	public Set<Sitznummer> gibFreieSitze() {
		Set<Sitznummer> result = new HashSet<Sitznummer>();
		for(Sitznummer sitz : sitzVerbucht.keySet()) {
			if(!sitzVerbucht.get(sitz)) {
				result.add(sitz);
			}
		}
		return result;
	}

	public synchronized boolean bucheSitze(List<Sitznummer> sitze) {
		if (gibFreieSitze().containsAll(sitze)) {
			sitze.stream().forEach(sitz -> sitzVerbucht.replace(sitz, true));
			return true;
		}

		return false;
	}

}
