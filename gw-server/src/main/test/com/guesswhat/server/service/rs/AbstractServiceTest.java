package com.guesswhat.server.service.rs;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Random;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Form;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.internal.util.Base64;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.runner.RunWith;

import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.ImageDTO;
import com.guesswhat.server.service.rs.dto.ImageType;
import com.guesswhat.server.service.rs.dto.QuestionDTO;
import com.guesswhat.server.service.rs.dto.RecordDTO;
import com.guesswhat.server.service.security.cfg.UserRole;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:cfg-ApplicationContext.xml")
@Ignore
public class AbstractServiceTest {
	
	private static final String SERVER_URL = "http://localhost:8888";
	
	private static final String ADMIN_LOGIN = "admin_login_3647";
	private static final String ADMIN_PASSWORD = "admin_password_3462";
	private static final String WRITER_LOGIN = "writer_login_3246";
	private static final String WRITER_PASSWORD = "writer_password_5869";
	private static final String READER_LOGIN = "reader_login_2398";
	private static final String READER_PASSWORD = "reader_password_9283";
	
	private static final Random randomGenerator = new Random();
	
	@Before
	public void setUp() throws InterruptedException {
		dropAllData();
		handshake();
	}
	
	private void handshake() throws InterruptedException {
		Client client = ClientBuilder.newClient();		
		WebTarget webTarget = client.target(getSecurityUrl());

		// admin
		Form f = new Form();
		f.param("username", ADMIN_LOGIN);
		f.param("password", ADMIN_PASSWORD);
		Builder invocationBuilder = webTarget.path("create").path("admin").request();	
		Response response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));	
		Assert.assertEquals(200, response.getStatus());

		// reader
		f = new Form();
		f.param("username", READER_LOGIN);
		f.param("password", READER_PASSWORD);
		f.param("role", UserRole.READER.toString());	
		invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));		
		Assert.assertEquals(200, response.getStatus());

		// writer
		f = new Form();
		f.param("username", WRITER_LOGIN);
		f.param("password", WRITER_PASSWORD);
		f.param("role", UserRole.WRITER.toString());
		invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getAdminAuthorization());
		response = invocationBuilder.post(Entity.entity(f, MediaType.APPLICATION_FORM_URLENCODED_TYPE));		
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected void createQuestion(QuestionDTO questionDTO, byte [] imageBytes) {
		ComposedQuestionDTO composedQuestionDTO = new ComposedQuestionDTO();
		composedQuestionDTO.setQuestionDTO(questionDTO);
		
		ImageDTO imageQuestionDTO = new ImageDTO();
		imageQuestionDTO.setLdpiImageId(imageBytes);
		imageQuestionDTO.setMdpiImageId(imageBytes);
		imageQuestionDTO.setHdpiImageId(imageBytes);
		imageQuestionDTO.setXhdpiImageId(imageBytes);
		imageQuestionDTO.setXxhdpiImageId(imageBytes);
		composedQuestionDTO.setImageQuestionDTO(imageQuestionDTO);
		
		if (randomGenerator.nextInt() % 3 == 0) {
			ImageDTO imageAnswerDTO = new ImageDTO();
			imageAnswerDTO.setLdpiImageId(imageBytes);
			imageAnswerDTO.setMdpiImageId(imageBytes);
			imageAnswerDTO.setHdpiImageId(imageBytes);
			imageAnswerDTO.setXhdpiImageId(imageBytes);
			imageAnswerDTO.setXxhdpiImageId(imageBytes);
			composedQuestionDTO.setImageAnswerDTO(imageAnswerDTO);
		}
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("create").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization());
		
		response = invocationBuilder.put(Entity.entity(composedQuestionDTO, MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected void createRecord(String userId, int points) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());
		Builder invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		
		RecordDTO recordDTO = new RecordDTO();
		recordDTO.setUserId(userId);
		recordDTO.setPoints(points);			
		Response response = invocationBuilder.put(Entity.entity(recordDTO, MediaType.APPLICATION_JSON_TYPE));		
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected int findRecordPlace(String userId) {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());

		Builder invocationBuilder = webTarget.request();		
		invocationBuilder =  webTarget.path("place").path(userId).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		Response response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		
		int userPlace = response.readEntity(Integer.class);
		
		return userPlace;
	}
	
	protected void deleteQuestion(String questionId) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("delete").path(questionId).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization());
		
		response = invocationBuilder.delete();
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected void dropAllData() {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getDatabaseUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("drop").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization());
		
		response = invocationBuilder.delete();
		Assert.assertTrue(response.getStatus() == 200 || response.getStatus() == 401);
	}
	
	protected List<QuestionDTO> findQuestions() {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("findall").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		Assert.assertEquals(200, response.getStatus());
		
		List<QuestionDTO> questions = response.readEntity(new GenericType<List<QuestionDTO>>() {});
		Assert.assertNotNull(questions);
		
		return questions;
	}
	
	protected List<RecordDTO> findTopRecords() {
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getRecordUrl());
		
		Builder invocationBuilder = webTarget.path("top").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		Response response = invocationBuilder.accept(MediaType.APPLICATION_JSON).post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		List<RecordDTO> records = response.readEntity(new GenericType<List<RecordDTO>>() {});
		Assert.assertNotNull(records);
		
		return records;
	}
	
	protected byte [] downloadBackup() {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getBackupUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("download").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization()).accept(MediaType.APPLICATION_OCTET_STREAM);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		Assert.assertEquals(200, response.getStatus());
		
		byte[] backup = response.readEntity(byte [].class);
		
		return backup;
	}
	
	protected byte [] downloadImage(String questionId, ImageType type, String path) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getImageUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("find").path(path).path(questionId).path(type.toString()).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization()).accept(MediaType.APPLICATION_OCTET_STREAM);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		Assert.assertEquals(200, response.getStatus());
		
		byte[] bytes = response.readEntity(byte [].class);
		
		return bytes;
	}
	
	protected void uploadBackup(byte [] backup) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getBackupUrl());
		Response response = null;
		
		InputStream is = new ByteArrayInputStream(backup);
		
		String sContentDisposition = "attachment; filename=\"" + "tempFile" + "\"";
		
		Builder invocationBuilder = webTarget.path("upload").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization()).header("Content-Disposition", sContentDisposition);
		
		response = invocationBuilder.post(Entity.entity(is, MediaType.APPLICATION_OCTET_STREAM));		
		Assert.assertEquals(200, response.getStatus());
	}
	
	protected static String getSecurityUrl() {
		return SERVER_URL + "/security";
	}
	
	protected static String getRecordUrl() {
		return SERVER_URL + "/records";
	}
	
	protected static String getQuestionUrl() {
		return SERVER_URL + "/questions";
	}
	
	protected static String getImageUrl() {
		return SERVER_URL + "/images";
	}
	
	protected static String getDatabaseUrl() {
		return SERVER_URL + "/database";
	}
	
	protected static String getBackupUrl() {
		return SERVER_URL + "/backup";
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
