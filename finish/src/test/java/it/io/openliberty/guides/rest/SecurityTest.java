package it.io.openlibert.guides.rest;

import static org.junit.Assert.*;
import java.io.*;
import java.util.*;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.apache.cxf.jaxrs.provider.jsrjsonp.JsrJsonpProvider;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.logging.ConsoleHandler;

import java.io.StringReader;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.json.JsonArray;
import javax.ws.rs.core.Form;


public class SecurityTest {
	private static String httpPort;
	private static String baseHttpUrl;
	private static String loginUrl;

	private Client client;

	private final String SYSTEM_PROPERTIES = "system/properties";
	private final String APP_NAME = "LibertyProject";

	private final String ADMIN_NAME = "bob:bobpwd";

	private final String USER_NAME = "alice:alicepwd";

	private final String INCORRECT_NAME = "carl:carlpwd";

	@BeforeClass
	public static void oneTimeSetup(){

		httpPort = "9080";
		baseHttpUrl = "http://localhost:" + httpPort + "/" + "LibertyProject";
		System.out.println("BaseHttpUrl is : ");
		System.out.println(baseHttpUrl);
	}

	@Before
	public void setup(){
		client = ClientBuilder.newClient();
	}

	@After 
	public void teardown() {
		client.close();
	}

	@Test
	public void testSuite(){
		this.testCorrectAdmin();
		this.testCorrectUser();
		this.testIncorrectUser();
	}
	public void testCorrectAdmin(){
		int expectedResponseStatus = 200;
		int actualResponseStatus = logIn(ADMIN_NAME);
		assertEquals(expectedResponseStatus, actualResponseStatus);
	}

	public void testCorrectUser(){
		int expectedResponseStatus = 403;
		int actualResponseStatus = logIn(USER_NAME);
		assertEquals(expectedResponseStatus, actualResponseStatus);
	}

	public void testIncorrectUser(){
		int expectedResponseStatus = 401;
		int actualResponseStatus = logIn(INCORRECT_NAME);
		assertEquals(expectedResponseStatus, actualResponseStatus);
	}


		public void printPage(Response response){

					System.out.println("Response status of page I am printing:");
					System.out.println(response.getStatus());
				 BufferedReader br = new BufferedReader(new InputStreamReader((InputStream) response.getEntity()));
	    List<String> result = new ArrayList<String>();
	    System.out.println("REACHED before the try");
	    try {
	    	String input;
	    	System.out.println("inside try");
	    	while ((input = br.readLine()) != null){
	    		System.out.println(input);

	    		result.add(input);
	    	}
	    	br.close();
	    } catch (IOException e){
	    	e.printStackTrace();
	    	fail();
	    }
		}
		public int logIn(String usernameAndPassword){
			String authHeader = "Basic "
	        + java.util.Base64.getEncoder()
	                          .encodeToString(usernameAndPassword.getBytes());

	  Response response = client.target("http://localhost:9080/LibertyProject/System/properties")
	                                     .request(MediaType.APPLICATION_JSON)
	                                     .header("Authorization",
	                                         authHeader)
	                                     .get();

    int loginResponseValue = response.getStatus();
    System.out.println("Printing the LibertyProject page:");

    response.close();

    return loginResponseValue;

		}

}