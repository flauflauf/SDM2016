package de.wps.dot.reservierung;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jetty.http.HttpStatus;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import de.wps.dot.filmprogramm.Vorführung;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.SparkBase;

public class WebReservierungsService {
	private static Gson gson = new GsonBuilder().enableComplexMapKeySerialization().setPrettyPrinting().create();
	private ReservierungsService reservierungsService;

	public WebReservierungsService(List<Saal> säle, List<Vorführung> vorführungen) {
		reservierungsService = new ReservierungsService(säle, vorführungen);
		// Sampledaten für gebuchte Sitze: reservierungsService.bucheSitze(vorführungen.get(0).getId(), Arrays.asList("A1", "A2"));
	}

	public void start(int port) {
		SparkBase.port(port);

		Spark.get("/säle", this::säle, gson::toJson);
		Spark.get("/sitzplatzbelegung/:vorstellungsid", this::sitzplatzbelegung, gson::toJson);
		Spark.get("/free_seats/:vorstellungsid", this::freeSeats, gson::toJson);
		Spark.post("/booking/:vorstellungsid", this::bookSeats, gson::toJson);

		Spark.exception(NotFoundException.class, (e, request, response) -> {
			response.status(HttpStatus.NOT_FOUND_404);
			response.body("Resource not found");
		});

		Spark.awaitInitialization();
	}

	public void stop() {
		Spark.stop();
	}

	public Object freeSeats(Request req, Response res) {
		final String vorstellungsid = req.params(":vorstellungsid");
		if (!reservierungsService.istVorstellungBekannt(vorstellungsid))
			throw new NotFoundException();

		return reservierungsService.gibFreieSitze(vorstellungsid);
	}
	
	

	public Object bookSeats(Request req, Response res) throws IOException {
		String vorstellungsid = req.params(":vorstellungsid");

		List<Sitznummer> requestedSeats = bookingToStringList(req);
		if (!reservierungsService.istVorstellungBekannt(vorstellungsid))
			throw new NotFoundException();

		
		Reservierung reservierung = reservierungsService.bucheSitze(vorstellungsid, requestedSeats);
		if (reservierung == null) {
			res.status(HttpStatus.CONFLICT_409);
			return "";
		}
		
		res.status(HttpStatus.CREATED_201);
		return reservierung;
	}
	
    private List<Sitznummer> bookingToStringList(Request request) throws IOException {
    	List<String> requestedSeats = null;
        switch (request.contentType()) {
            case "application/json":
            	requestedSeats = Arrays.asList(gson.fromJson(request.body(), String[].class));
                break;
            case "application/x-www-form-urlencoded":
            	requestedSeats = Arrays.asList(gson.fromJson(dirtyPrepareRequest(request.body(), "sitze="), String[].class));;
                break;
            default:
                Spark.halt(400, "Unsupported request content-type [" + request.contentType() + "]");
        }
        
        List<Sitznummer> result = new ArrayList<Sitznummer>();
        for(String seat : requestedSeats)
        {
        	//TODO: macht die Annahme das es nur einstellige Reihhenbezeichnungen gibt.
        	result.add(Sitznummer.von(seat.substring(0,1), Integer.parseInt(seat.substring(1,seat.length()))));
        }
        
        return result;
    }
    
    private String dirtyPrepareRequest(String requestBody, String variable)
    {
    	String result = "[";
    	
    	String[] strings = requestBody.split("&");
    	
    	for(String s : strings)
    	{
    		if(s.startsWith(variable))
    		{
    			result += s.substring(variable.length());
    			result += ", ";
    		}
    	}
    	
    	result += "]";
    	result = result.replace(", ]", "]");
    	return result;
    }
    
    public Object sitzplatzbelegung(Request req, Response res) {
    	String vorstellungsid = req.params(":vorstellungsid");
    	
    	return reservierungsService.gibSitzplatzbelegung(vorstellungsid);
    }

	public Object säle(Request req, Response res) {
		return reservierungsService.gibSäle();
	}
}