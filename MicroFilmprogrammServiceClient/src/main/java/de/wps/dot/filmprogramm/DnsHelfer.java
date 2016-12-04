package de.wps.dot.filmprogramm;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.directory.InitialDirContext;

public class DnsHelfer {

	public static InitialDirContext erzeugeKontext() throws NamingException{
		Hashtable<String, Object> env = new Hashtable<>();
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.dns.DnsContextFactory");
		env.put(Context.PROVIDER_URL, "dns://localhost:8600");
		
		return new InitialDirContext(env);
	}
	
	public static Map<String, String> findePortUndHostNameAusSrvEintrag(String srvEintrag) throws NamingException {
		String port = srvEintrag.split(" ")[3];
		String hostName = srvEintrag.split(" ")[4];
		
		Map<String, String> ergebnis = new HashMap<String, String>();
		ergebnis.put("port", port);
		ergebnis.put("hostName", hostName.substring(0, hostName.length()-1));
		return ergebnis;
	}
	
	public static String findeIpAusAEintrag(String aEintrag){
		return aEintrag.split(" ")[1];
	}

}
