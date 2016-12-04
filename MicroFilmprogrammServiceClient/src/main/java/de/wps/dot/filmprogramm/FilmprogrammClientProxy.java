package de.wps.dot.filmprogramm;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;

import javax.naming.NamingException;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FilmprogrammClientProxy {
	private static Gson gson = new Gson();

	public List<Vorführung> getVorführungen() throws UnirestException, NamingException {
		List<Vorführung> freeSeats = null;

		URI uri = URI.create("http://localhost:50000/vorführungen");

		HttpResponse<String> response = Unirest.get(uri.toString()).asString();
		Type seatListType = new TypeToken<List<Vorführung>>() {
		}.getType();
		freeSeats = gson.fromJson(response.getBody(), seatListType);

		return freeSeats;
	}
}
