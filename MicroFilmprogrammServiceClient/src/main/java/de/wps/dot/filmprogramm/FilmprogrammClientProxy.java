package de.wps.dot.filmprogramm;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Random;

import javax.naming.NamingException;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FilmprogrammClientProxy {
	private static Gson gson = new Gson();

	public List<Vorführung> getVorführungen() throws UnirestException, NamingException {
		List<Vorführung> freeSeats = null;
				
		String serviceIp = getServiceAddressByREST();
		
		URI uri = URI.create("http://"+serviceIp+":50000/vorführungen");

		HttpResponse<String> response = Unirest.get(uri.toString()).asString();
		Type seatListType = new TypeToken<List<Vorführung>>() {
		}.getType();
		freeSeats = gson.fromJson(response.getBody(), seatListType);

		return freeSeats;
	}

	private String getServiceAddressByREST() {
		ConsulClient consulClient = new ConsulClient();
		List<CatalogService> catalogServices = consulClient.getCatalogService("filmprogramm", null).getValue();
		
		if(catalogServices.isEmpty())
			; // TODO nächster Workshop ;-)
		
		int index = new Random().nextInt(catalogServices.size());
		CatalogService service = catalogServices.get(index);

		String serviceIp = service.getAddress();
		return serviceIp;
	}
}
