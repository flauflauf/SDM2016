package de.wps.dot.ui;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.wps.dot.filmprogramm.Vorführung;
import de.wps.dot.filmprogramm.FilmprogrammClientProxy;
import de.wps.dot.reservierung.Reservierung;
import de.wps.dot.reservierung.ReservierungsClientProxy;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class Controller {
	private static final ReservierungsClientProxy reservierungsClientProxy = new ReservierungsClientProxy();
	private static final FilmprogrammClientProxy filmprogrammClientProxy = new FilmprogrammClientProxy();

	public ModelAndView vorstellungen(Request req, Response res) {

		List<Vorführung> vorführungen = ladeVorführungen();

		Map<String, Object> map = new HashMap<>();
		map.put("vorführungen", vorführungen);

		// vorführungen.html die Datei findet sich in resources/templates directory
		return new ModelAndView(map, "vorführungen");
	}

	public ModelAndView buchung(Request req, Response res) {
		String vorführungsid = req.params(":vorführungsid");

		Reservierung reservierung = null;
		try {
			reservierung = reservierungsClientProxy.bucheSitze(vorführungsid, req.body(), req.contentType());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Map<String, Object> model = new HashMap<String, Object>();
		model.put("vorführungsid", vorführungsid);

		String viewName;
		if (reservierung == null) {
			viewName = "reservierung_nicht_möglich";
		} else {
			viewName = "reservierung_erfolgreich";
			model.put("reservierungsnummer", reservierung.getReservierungsnummer());
		}

		return new ModelAndView(model, viewName);
	}

	public ModelAndView freieSitze(Request req, Response res) {
		String vorführungsid = req.params(":vorführungsid");
		Map<String, Object> model = new HashMap<String, Object>();
		model.put("vorfuehrungsid", vorführungsid);

		try {
			// TODO: Internal Server Error abbilden?
			model.put("sitzplatzbelegung", reservierungsClientProxy.gibSitzplatzbelegung(vorführungsid));
		} catch (Exception e) {
			System.err.println(e.getMessage());
			throw new NotFoundException();
		}

		// sitzplan_reservierung.html die Datei findet sich in resources/templates directory
		return new ModelAndView(model, "sitzplan_reservierung");
	}

	private List<Vorführung> ladeVorführungen() {
		List<Vorführung> vorführungen;
		try {
			// Möglicherweise wird kein Service gefunden
			vorführungen = filmprogrammClientProxy.getVorführungen();
		} catch (Exception e) {
			e.printStackTrace();
			vorführungen = Arrays.asList(new Vorführung("Fehler", "", LocalDateTime.of(2000, 1, 1, 0, 0)));
		}
		return vorführungen;
	}
}
