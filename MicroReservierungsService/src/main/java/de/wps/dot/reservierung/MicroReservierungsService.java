package de.wps.dot.reservierung;

import java.util.Arrays;
import java.util.List;

import de.wps.dot.filmprogramm.FilmprogrammClientProxy;
import de.wps.dot.filmprogramm.Vorführung;

public class MicroReservierungsService {
	private WebReservierungsService service;

	public static void main(String[] args) {
		if (args.length < 1) {
			System.out.println("missing command-line argument: port");
			return;
		}

		int port = Integer.parseInt(args[0]);
		new MicroReservierungsService().start(port);
	}

	public MicroReservierungsService() {
		List<Saal> säle = ladeSäle();
		List<Vorführung> vorführungen = ladeVorführungen();

		service = new WebReservierungsService(säle, vorführungen);
	}

	public void start(int port) {
		service.start(port);
	}


	public void stop() {
		service.stop();
	}

	private static List<Saal> ladeSäle() {
		return Arrays.asList(new Saal("Großer Saal", 20, 25), new Saal("Kleiner Saal", 6, 8));
	}

	private static List<Vorführung> ladeVorführungen() {
		List<Vorführung> vorführungen = null;
		while (vorführungen == null) {
			try {
				FilmprogrammClientProxy filmprogrammClient = new FilmprogrammClientProxy();
				vorführungen = filmprogrammClient.getVorführungen();
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("Kann nicht starten, da Filmprogramm-Service nicht gefunden. Retry in 1 Sekunde.");
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					e1.printStackTrace();
				}
			}
		}
		return vorführungen;
	}

}