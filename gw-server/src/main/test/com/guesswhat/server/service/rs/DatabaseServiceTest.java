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

import com.guesswhat.server.service.rs.dto.QuestionDTO;

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
		
		QuestionDTO questionDTO = new QuestionDTO();
		questionDTO.setAnswer1("answer1");
		questionDTO.setAnswer2("answer2");
		questionDTO.setAnswer3("answer3");
		questionDTO.setAnswer4("answer4");
		questionDTO.setCorrectAnswer("answer1");
		
		byte [] imageBytes = new byte[] {1,2,3,4,5};
		
		createQuestion(questionDTO, imageBytes);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());		
		int newVersion = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion);		
		
		byte [] backup = downloadBackup();
		uploadBackup(backup);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());		
		int newVersion2 = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion2);
		Assert.assertNotEquals(newVersion, newVersion2);
		
		List<QuestionDTO> questions = findQuestions();
		Assert.assertTrue(questions.size() == 1);
		deleteQuestion(questions.get(0).getId());
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));
		Assert.assertEquals(200, response.getStatus());		
		int newVersion3 = response.readEntity(Integer.class);
		Assert.assertNotEquals(initialVersion, newVersion3);
		Assert.assertNotEquals(newVersion, newVersion3);
		Assert.assertNotEquals(newVersion2, newVersion3);
	}
}
