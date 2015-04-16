package com.guesswhat.server.services.rs;

import java.nio.charset.Charset;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Assert;
import org.junit.Before;

import com.guesswhat.server.services.security.cfg.UserRole;

public class AbstractServiceTest {
	
	private static final String SERVER_URL = "http://localhost:8888";
	
	private static final String ADMIN_LOGIN = "admin_login_3647";
	private static final String ADMIN_PASSWORD = "admin_password_3462";
	private static final String WRITER_LOGIN = "writer_login_3246";
	private static final String WRITER_PASSWORD = "writer_password_5869";
	private static final String READER_LOGIN = "reader_login_2398";
	private static final String READER_PASSWORD = "reader_password_9283";

	@Before
	public void init() {
		String securityUrl = getSecurityUrl();
		Client client = ClientBuilder.newClient();		

		// admin
		Form f = new Form();
		f.param("username", ADMIN_LOGIN);
		f.param("password", ADMIN_PASSWORD);
		Builder invocationBuilder = client.target(securityUrl + "/create/admin").request();	
		Response response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));		
		Assert.assertEquals(200, response.getStatus());

		// reader
		invocationBuilder = client.target(securityUrl + "/delete/" + READER_LOGIN).request();
		response = invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization()).delete();
		Assert.assertEquals(200, response.getStatus());
		
		f = new Form();
		f.param("username", READER_LOGIN);
		f.param("password", READER_PASSWORD);
		f.param("role", UserRole.READER.toString());		
		invocationBuilder = client.target(securityUrl + "/create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));		
		Assert.assertEquals(200, response.getStatus());

		// writer
		invocationBuilder = client.target(securityUrl + "/delete/" + WRITER_LOGIN).request();
		response = invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization()).delete();
		Assert.assertEquals(200, response.getStatus());
		
		f = new Form();
		f.param("username", WRITER_LOGIN);
		f.param("password", WRITER_PASSWORD);
		f.param("role", UserRole.WRITER.toString());
		invocationBuilder = client.target(securityUrl + "/create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));		
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected static String getSecurityUrl() {
		return SERVER_URL + "/security";
	}
	
	protected static String getRecordUrl() {
		return SERVER_URL + "/records";
	}
	
	protected static String getAdminAuthorization() {
		return getAuthorization(ADMIN_LOGIN, ADMIN_PASSWORD);
	}
	
	protected static String getReaderAuthorization() {
		return getAuthorization(READER_LOGIN, READER_PASSWORD);
	}
	
	protected static String getWriterAuthorization() {
		return getAuthorization(WRITER_LOGIN, WRITER_PASSWORD);
	}
	
	private static String getAuthorization(String username, String password) {
		return "Basic " + new String(Base64.encode(String.valueOf(username + ":" + password).getBytes()), Charset.forName("ASCII"));
	}
	
}
