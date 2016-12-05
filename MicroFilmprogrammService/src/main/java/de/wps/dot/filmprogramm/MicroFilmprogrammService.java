package de.wps.dot.filmprogramm;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.agent.model.NewService;

public class MicroFilmprogrammService {

	public static void main(String[] args) {

		if (args.length < 1) {
			System.out.println("missing command-line argument: port");
			return;
		}

		int port = Integer.parseInt(args[0]);

		MicroFilmprogrammService microFilmprogrammService = new MicroFilmprogrammService();
		
		Runtime.getRuntime().addShutdownHook(new Thread(){
			@Override
			public void run() {
				microFilmprogrammService.stop();
			}			
		});

		microFilmprogrammService.start(port);
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
		
		serviceId = "filmprogramm-" + port;
		
		NewService newService = new NewService();
		newService.setId(serviceId);
		newService.setName("filmprogramm");
		newService.setPort(port);
		
		ConsulClient consulClient = new ConsulClient();
		consulClient.agentServiceRegister(newService);
	}

	public void stop() {
		ConsulClient consulClient = new ConsulClient();
		consulClient.agentServiceDeregister(serviceId);

		service.stop();
	}

	private String serviceId;
}