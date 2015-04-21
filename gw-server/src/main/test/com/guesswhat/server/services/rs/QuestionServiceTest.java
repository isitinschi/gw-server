package com.guesswhat.server.services.rs;

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

import com.guesswhat.server.services.rs.dto.ImageType;
import com.guesswhat.server.services.rs.dto.QuestionDTO;

public class QuestionServiceTest extends AbstractServiceTest {
	
	private static final int QUESTION_COUNT = 10;
	
	@Test
	public void testQuestions() {
		List<QuestionDTO> questions = findQuestions();		
		Assert.assertTrue(questions.size() == QUESTION_COUNT);
		
		for (int i = 0; i < QUESTION_COUNT; ++i) {
			QuestionDTO questionDTO = questions.get(i);
			
			Assert.assertTrue(questionDTO.getAnswer1().contains("answer1-"));
			Assert.assertTrue(questionDTO.getAnswer2().contains("answer2-"));
			Assert.assertTrue(questionDTO.getAnswer3().contains("answer3-"));
			Assert.assertTrue(questionDTO.getAnswer4().contains("answer4-"));
			Assert.assertTrue(questionDTO.getCorrectAnswer().contains("answer1-"));
			
			testQuestionImages(questionDTO.getId());
		}
	}
	
	public void testQuestionImages(String questionId) {
		for (ImageType type : ImageType.values()) {
			testImage(questionId, type, "question");
			testImage(questionId, type, "answer");
		}		
	}

	private void testImage(String questionId, ImageType type, String path) {
		String url = getImageUrl() + "/find/" + path + "/" + questionId + "/" + type;
		
		Client client = ClientBuilder.newClient();
		WebTarget webTarget = client.target(url);
		Response response = null;
		
		Builder invocationBuilder = webTarget.request();
		invocationBuilder.header(HttpHeaders.AUTHORIZATION, getReaderAuthorization()).accept(MediaType.APPLICATION_OCTET_STREAM);
		
		response = invocationBuilder.post(Entity.entity("", MediaType.APPLICATION_JSON_TYPE));		
		Assert.assertEquals(200, response.getStatus());
		
		byte[] bytes = response.readEntity(byte [].class);
		
		if (path.equals("answer") && (bytes == null || bytes.length == 0)) {
			return; // sometimes, there is no answer ;-)
		}
		
		Assert.assertNotNull(bytes);
		Assert.assertTrue(bytes.length == 5);
		for (int i = 1; i <= 5; ++i) {
			Assert.assertEquals(i, (int) Integer.valueOf(bytes[i - 1]));
		}
	}
	
	public void createQuestions() {
		for (int i = 0; i < QUESTION_COUNT; ++i) {
			QuestionDTO questionDTO = new QuestionDTO();
			questionDTO.setAnswer1("answer1-" + i);
			questionDTO.setAnswer2("answer2-" + i);
			questionDTO.setAnswer3("answer3-" + i);
			questionDTO.setAnswer4("answer4-" + i);
			questionDTO.setCorrectAnswer("answer1-" + i);
			
			byte [] imageBytes = new byte[] {1,2,3,4,5};
			
			createQuestion(questionDTO, imageBytes);
		}
	}
}
