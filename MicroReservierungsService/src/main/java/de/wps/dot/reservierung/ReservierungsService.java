package de.wps.dot.reservierung;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import de.wps.dot.filmprogramm.Vorführung;

public class ReservierungsService {
	private List<Saal> säle;
	private Map<String, Sitzplatzbelegung> saalpläne;
	private final List<Reservierung> reservierungen;

	public ReservierungsService(List<Saal> säle, List<Vorführung> vorführungen) {
		this.säle = säle;
		reservierungen = new ArrayList<Reservierung>();
		saalpläne = new HashMap<>(säle.size());

		for (Vorführung vorführung : vorführungen) {
			Optional<Saal> optionalSaal = findeSaal(säle, vorführung.getSaalId());
			if (!optionalSaal.isPresent())
				continue;
			Saal saal = optionalSaal.get();
			saalpläne.put(vorführung.getId(), new Sitzplatzbelegung(saal, vorführung.getId()));
		}
	}

	private Optional<Saal> findeSaal(List<Saal> säle, String saalId) {
		return säle.stream().filter(saal -> saal.getId().equals(saalId)).findFirst();
	}

	public Set<Sitznummer> gibFreieSitze(String vorstellungsid) {
		assert istVorstellungBekannt(vorstellungsid);

		Sitzplatzbelegung sitzplatzbelegung = saalpläne.get(vorstellungsid);

		return sitzplatzbelegung.gibFreieSitze();
	}

	public Sitzplatzbelegung gibSitzplatzbelegung(String vorstellungsid) {
		assert istVorstellungBekannt(vorstellungsid);

		return saalpläne.get(vorstellungsid);
	}

	public boolean istVorstellungBekannt(String vorstellungsid) {
		return saalpläne.containsKey(vorstellungsid);
	}

	public Reservierung bucheSitze(String vorstellungsid, List<Sitznummer> requestedSeats) {
		assert istVorstellungBekannt(vorstellungsid);

		Sitzplatzbelegung sitzplatzbelegung = saalpläne.get(vorstellungsid);
		if (sitzplatzbelegung.bucheSitze(requestedSeats)) {
			Reservierung reservierung = Reservierung.erzeugeReservierung(vorstellungsid, requestedSeats);
			reservierungen.add(reservierung);
			return reservierung ;
		}
		return null;
	}

	public List<Saal> gibSäle() {
		return säle;
	}
}