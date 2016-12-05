package de.wps.dot.filmprogramm;

import java.lang.reflect.Type;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.naming.NamingException;
import javax.naming.directory.Attributes;
import javax.naming.directory.InitialDirContext;

import com.ecwid.consul.v1.ConsulClient;
import com.ecwid.consul.v1.Response;
import com.ecwid.consul.v1.catalog.model.CatalogService;
import com.ecwid.consul.v1.health.model.HealthService;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class FilmprogrammClientProxy {
	private static Gson gson = new Gson();

	public List<Vorführung> getVorführungen() throws UnirestException, NamingException {
		List<Vorführung> freeSeats = null;
				
//		String serviceIpAndPort = getServiceIpAndPortByREST();
		String serviceIpAndPort = getServiceIpAndPortByDNS();
		
		URI uri = URI.create("http://"+serviceIpAndPort+"/vorführungen");

		HttpResponse<String> response = Unirest.get(uri.toString()).asString();
		Type seatListType = new TypeToken<List<Vorführung>>() {
		}.getType();
		freeSeats = gson.fromJson(response.getBody(), seatListType);

		return freeSeats;
	}

	private String getServiceIpAndPortByDNS() throws NamingException {
		InitialDirContext kontext = DnsHelfer.erzeugeKontext();
		Attributes attributes =
				kontext.getAttributes(
						"filmprogramm.service.consul",
						new String[] { "SRV" });
		
		String srvEntry = attributes.get("SRV").toString();
		Map<String, String> portUndHostName = DnsHelfer.findePortUndHostNameAusSrvEintrag(srvEntry);
		String port = portUndHostName.get("port");
		String hostName = portUndHostName.get("hostName");
		
		Attributes aAttributes = kontext.getAttributes(hostName, new String[] { "A" });
		String aEntry = aAttributes.get("A").toString();
		String ip = DnsHelfer.findeIpAusAEintrag(aEntry);
		
		return ip + ":" + port;
	}

	private String getServiceIpAndPortByREST() {
		ConsulClient consulClient = new ConsulClient();
		List<HealthService> catalogServices =
				consulClient.getHealthServices("filmprogramm", true, null).getValue();
		
		if(catalogServices.isEmpty())
			; // TODO nächster Workshop ;-)
		
		int index = new Random().nextInt(catalogServices.size());
		HealthService service = catalogServices.get(index);

		String serviceIp = service.getNode().getAddress();
		int servicePort = service.getService().getPort();
		return serviceIp + ":" + servicePort;
	}
}
