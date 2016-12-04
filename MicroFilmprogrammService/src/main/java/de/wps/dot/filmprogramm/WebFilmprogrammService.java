package de.wps.dot.filmprogramm;

import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;

import spark.Request;
import spark.Response;
import spark.Spark;
import spark.SparkBase;

public class WebFilmprogrammService {
	private static Gson gson = new Gson();

	private FilmprogrammService filmprogrammService;

	public WebFilmprogrammService(List<Vorführung> vorführungen) {
		filmprogrammService = new FilmprogrammService(vorführungen);
	}

	public void start(int port) {
		SparkBase.port(port);

		Spark.get("/vorführungen", this::vorführungen, gson::toJson);

		Spark.exception(Exception.class, (e, request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("Resource not found");
		});

		// Abwarten, bis Spark gestartet ist
		Spark.awaitInitialization();
	}

	public void stop() {
		Spark.stop();
	}

	private Object vorführungen(Request req, Response res) {
		return filmprogrammService.gibVorführungen();
	}

}