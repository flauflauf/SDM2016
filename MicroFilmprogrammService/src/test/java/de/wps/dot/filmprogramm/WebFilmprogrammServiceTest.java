package de.wps.dot.filmprogramm;

import org.junit.Assert;
import org.junit.Test;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

public class WebFilmprogrammServiceTest {

	@Test
	public void callHTTPEndPoint() throws UnirestException {
		// Arrange
		MicroFilmprogrammService microFilmprogrammService = new MicroFilmprogrammService();
		microFilmprogrammService.start(60000);
		
		// Act
		HttpResponse<String> response =
				Unirest.get("http://localhost:60000/vorführungen").asString();
		
		// Assert
		String body = response.getBody();
		Assert.assertEquals('[', body.charAt(0));
		Assert.assertEquals(']', body.charAt(body.length() - 1));
	}

}
