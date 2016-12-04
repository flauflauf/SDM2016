package de.wps.dot.reservierung;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import javax.naming.NamingException;

import org.junit.Test;

import de.wps.dot.reservierung.DnsHelfer;

public class DnsHelferTest {

	@Test
	public void FindePortUndHostNameAusSrvEintrag_gibtDenRichtigenPortUndHostNameFürSrvEintragZurück() throws NamingException {
		// Act
		Map<String, String> ergebnis = DnsHelfer.FindePortUndHostNameAusSrvEintrag("{srv=SRV: 1 1 50000 Johanness-MBP.fritz.box.node.dc1.consul.}");
		
		// Assert
		assertThat(ergebnis.get("port"), is(equalTo("50000")));;
		assertThat(ergebnis.get("hostName"), is(equalTo("Johanness-MBP.fritz.box.node.dc1.consul.")));
	}
	
	@Test
	public void findeIpAusAEintrag_gibtDenRichtigenPortZurück() throws NamingException {
		// Act
		String ergebnis = DnsHelfer.findeIpAusAEintrag("{a=A: 127.0.0.1}");
		
		// Assert
		assertThat(ergebnis, is(equalTo("127.0.0.1")));
	}
}
