package de.wps.dot.ui;

import javax.naming.NamingException;

import org.eclipse.jetty.http.HttpStatus;

import spark.Spark;
import spark.SparkBase;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

public class WebUI {
	
	public static void main(String[] args) throws NamingException {
		publishServiceMethods(8081);
	}

	private static void publishServiceMethods(int port) throws NamingException {
		Spark.staticFileLocation("/public"); // Static files
		SparkBase.port(port);

		Controller controller = new Controller();
		Spark.get("/sitzplatzbelegung/:vorführungsid", controller::freieSitze, new ThymeleafTemplateEngine());
		Spark.get("/", controller::vorstellungen, new ThymeleafTemplateEngine());
		Spark.post("/buchung/:vorführungsid", controller::buchung, new ThymeleafTemplateEngine());

		Spark.exception(NotFoundException.class, (e, request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("Resource not found");
		});

		Spark.awaitInitialization();
	}
}