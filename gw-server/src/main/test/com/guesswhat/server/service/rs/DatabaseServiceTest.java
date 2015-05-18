package com.guesswhat.server.service.rs;

import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.junit.Assert;
import org.junit.Test;

import com.guesswhat.server.service.rs.dto.ComposedQuestionDTO;
import com.guesswhat.server.service.rs.dto.QuestionDTO;
import com.guesswhat.server.service.rs.dto.RecordDTOListWrapper;

public class DatabaseServiceTest extends AbstractServiceTest {
	
	@Test
	public void testDatabaseVersion() {
		String databaseUrl = getDatabaseUrl();
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(databaseUrl);
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("version").request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization());
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		int initialVersion = response.readEntity(Integer.class);
		
		String answer1 = "answer1";
		String answer2 = "answer2";
		String answer3 = "answer3";
		String answer4 = "answer4";
		QuestionDTO questionDTO = new QuestionDTO(answer1, answer2, answer3, answer4, answer1);
		
		byte [] imageBytes = new byte[] {1,2,3,4,5};
		
		createTestQuestion(questionDTO, imageBytes);
		createRecord("user0", 500);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());		
		int newVersion = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion);		
		
		RecordDTOListWrapper recordBackup = downloadRecordBackup();		
		uploadRecordBackup(recordBackup);
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());
		int newVersion2 = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion2);
		Assert.assertNotEquals(newVersion, newVersion2);		
		
		List<ComposedQuestionDTO> questionBackup = downloadQuestionBackup();
		uploadQuestionBackup(questionBackup);
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());		
		int newVersion3 = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion3);
		Assert.assertNotEquals(newVersion, newVersion3);
		Assert.assertNotEquals(newVersion2, newVersion3);
		
		List<QuestionDTO> questions = findQuestions();
		Assert.assertTrue(questions.size() == 1);
		deleteQuestion(questions.get(0).getId());		
	}
	
	private void deleteQuestion(String questionId) {		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(getQuestionUrl());
		Response response = null;
		
		Builder invocationBuilder = webTarget.path("delete").path(questionId).request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getWriterAuthorization());
		
		response = invocationBuilder.delete();
		Assert.assertEquals(200, response.getStatus());
	}
	
}
