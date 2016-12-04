package de.wps.dot.filmprogramm;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

public class MicroFilmprogrammService {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("missing command-line argument: port");
			return;
		}

		int port = Integer.parseInt(args[0]);

		new MicroFilmprogrammService().start(port);
	}

	private List<Vorführung> ladeVorführungen() {
		return Arrays.asList(//
				new Vorführung("Filmy McFilmface", "grosser_saal", LocalDateTime.now().withHour(14).withMinute(0)), //
				new Vorführung("Filmy McFilmface", "kleiner_saal", LocalDateTime.now().withHour(16).withMinute(0)), //
				new Vorführung("Filmy McFilmface", "kleiner_saal", LocalDateTime.now().withHour(19).withMinute(0)));
	}

	private WebFilmprogrammService service;

	public MicroFilmprogrammService() {
		service = new WebFilmprogrammService(ladeVorführungen());
	}

	public void start(int port) {
		service.start(port);
		// Umlaute werden beim DNS nicht unterstützt
	}

	public void stop() {
		service.stop();
	}

}